---
layout: post
title: Blog5 - Image Classification
---

##### Introduction 
#### In this blog post, I will demonstrate how to use keras in Tensorflow to perform the task of image classification. I would show how we progress from building simple sequential models to including data augmentation, preprocessing, and transfer learning, to gradually achieve a higher and higher accuracy.

#### First of all,  let's load in the data and relevant packages.

```python
# Import packages
import matplotlib.pyplot as plt
import numpy as np
import os
import tensorflow as tf
from tensorflow.keras import layers
from tensorflow.keras import utils 
import random
```

```python
# location of data
_URL = 'https://storage.googleapis.com/mledu-datasets/cats_and_dogs_filtered.zip'

# download the data and extract it
path_to_zip = utils.get_file('cats_and_dogs.zip', origin=_URL, extract=True)

# construct paths
PATH = os.path.join(os.path.dirname(path_to_zip), 'cats_and_dogs_filtered')

train_dir = os.path.join(PATH, 'train')
validation_dir = os.path.join(PATH, 'validation')

# parameters for datasets
BATCH_SIZE = 32
IMG_SIZE = (160, 160)

# construct train and validation datasets 
train_dataset = utils.image_dataset_from_directory(train_dir,
                                                   shuffle=True,
                                                   batch_size=BATCH_SIZE,
                                                   image_size=IMG_SIZE)

validation_dataset = utils.image_dataset_from_directory(validation_dir,
                                                        shuffle=True,
                                                        batch_size=BATCH_SIZE,
                                                        image_size=IMG_SIZE)

# construct the test dataset by taking every 5th observation out of the validation dataset
val_batches = tf.data.experimental.cardinality(validation_dataset)
test_dataset = validation_dataset.take(val_batches // 5)
validation_dataset = validation_dataset.skip(val_batches // 5)
```

#### Here, we define a function that allows us to have a first glimpse of the dataset by constructing two rows of each species of animal with a given number of images.
```python
# Visualization
class_names = train_dataset.class_names

def Visualize_Cats_Dogs(num_img):
    plt.figure(figsize=(10, 10))
    for images, labels in train_dataset.take(1):
        num_dogs = 0
        dogs_ind = []
        num_cats = 0
        cats_ind = []
        
        for i in range(len(labels)):
            if class_names[labels[i]] == "dogs":
                dogs_ind.append(i)
        dogs_temp = random.sample(dogs_ind, k = num_img)
        for i in range(len(labels)):
            if class_names[labels[i]] == "cats":
                cats_ind.append(i)
        cats_temp = random.sample(cats_ind, k = num_img)
    
        for j in range(len(dogs_temp) + len(cats_temp)):
            ax = plt.subplot(2, num_img, j + 1)
            if j <= num_img-1:
            
                plt.imshow(images[dogs_temp[j]].numpy().astype("uint8"))
                plt.title(class_names[labels[dogs_temp[j]]])
                plt.axis("off")
            else:
                plt.imshow(images[cats_temp[j-num_img]].numpy().astype("uint8"))
                plt.title(class_names[labels[cats_temp[j-num_img]]])
                plt.axis("off")
                
Visualize_Cats_Dogs(3)
```
![cats_dogs.png](/images/cats_dogs.png)



```python
# Check Label Frequencies
AUTOTUNE = tf.data.AUTOTUNE

train_dataset = train_dataset.prefetch(buffer_size=AUTOTUNE)
validation_dataset = validation_dataset.prefetch(buffer_size=AUTOTUNE)
test_dataset = test_dataset.prefetch(buffer_size=AUTOTUNE)


labels_iterator = train_dataset.unbatch().map(lambda image, label: label).as_numpy_iterator()
```

#### We can see that both of our labels have frequency of 1000, and probability of 1/2.
```python
# Check frequency  
sum(np.array(iterator_list) == 0), sum(np.array(iterator_list) == 1)
```
(1000, 1000)


##### Part 2: Building our first model
#### This is where we use multiple Convolutional and Max Pooling, and drop out layers to construct a brief model of the dataset. We include a Flatten and a Dense layer at the end. 
```python
"""
at least two Conv2D layers, at least two MaxPooling2D layers, 
at least one Flatten layer, at least one Dense layer, 
and at least one Dropout layer
"""
model = tf.keras.models.Sequential([
    layers.Conv2D(50, (3, 3), activation='relu', input_shape=(160, 160, 3)),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.3),
    layers.Conv2D(50, (3, 3), activation='relu'),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.2),
    layers.Flatten(),
    layers.Dense(500, activation='relu'),
])

model.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

history = model.fit(train_dataset, 
                    epochs=20, 
                    validation_data=validation_dataset,
                   verbose = 0)
```


```python
plt.plot(history.history["accuracy"], label = "training")
plt.plot(history.history["val_accuracy"], label = "validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy")
plt.legend()
```
#### Our first model has a test accuracy that stabilizes to more than 65%.
![First_model.png](/images/First_model.png)
    


##### Part 3: Data Augmentation
#### In this part, we augment our data by either flipping or rotating our image by a certain degree. The inclusion of such transformed versions of the image in our training process could help our model learn invariant features within.
```python
augmentation_flip = tf.keras.Sequential([
    tf.keras.layers.RandomFlip('horizontal'),
])

for image, _ in train_dataset.take(1):
    plt.figure(figsize=(10, 10))
    first_image = image[0]
    for i in range(9):
        if i == 0:
            ax = plt.subplot(3, 3, i + 1)
            plt.imshow(first_image / 255)
            plt.title("original")
            plt.axis('off')
        else:
            ax = plt.subplot(3, 3, i + 1)
            augmented_image = augmentation_flip(tf.expand_dims(first_image, 0))
            plt.imshow(augmented_image[0] / 255)
            plt.title("random flipped")
            plt.axis('off')
```
![flip.png](/images/flip.png)
    



```python
augmentation_rotate = tf.keras.Sequential([
    tf.keras.layers.RandomRotation(0.5),
])

for image, _ in train_dataset.take(1):
    plt.figure(figsize=(10, 10))
    first_image = image[0]
    for i in range(9):
        if i == 0:
            ax = plt.subplot(3, 3, i + 1)
            plt.imshow(first_image / 255)
            plt.title("original")
            plt.axis('off')
        else:
            ax = plt.subplot(3, 3, i + 1)
            augmented_image = augmentation_rotate(tf.expand_dims(first_image, 0))
            plt.imshow(augmented_image[0] / 255)
            plt.title("random rotated")
            plt.axis('off')
```
![spin.png](/images/spin.png)
    



```python
model2 = tf.keras.models.Sequential([
    layers.Conv2D(50, (3, 3), activation='relu', input_shape=(160, 160, 3)),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.3),
    layers.Conv2D(50, (3, 3), activation='relu'),
    layers.MaxPooling2D((3, 3)),
    layers.RandomRotation(0.5),
    layers.RandomFlip('horizontal'),
    layers.Dropout(0.2),
    layers.Flatten(),
    layers.Dense(500, activation='relu'),
])

model2.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

history = model2.fit(train_dataset, 
                    epochs=20, 
                    validation_data=validation_dataset, 
                    verbose = 0)
```


```python
plt.plot(history.history["accuracy"], label = "training")
plt.plot(history.history["val_accuracy"], label = "validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy")
plt.legend()
```
#### Our test accuracy stabilizes to more than 60%.
![augmentation.png](/images/augmentation.png)
    


##### Part 4: Data Preprocessing
#### Transforming our input data could help enhancing the model's accuray as well. In our dataset, the original data has pixels with RGB values between 0 and 255, but if we transform them to between 1 and -1, we can train the mathematically same data with faster speed.
```python
i = tf.keras.Input(shape=(160, 160, 3))
x = tf.keras.applications.mobilenet_v2.preprocess_input(i)
preprocessor = tf.keras.Model(inputs = [i], outputs = [x])

model3 = tf.keras.models.Sequential([
    preprocessor,
    layers.Conv2D(25, (3, 3), activation='relu', input_shape=(160, 160, 3)),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.2),
    layers.Conv2D(25, (3, 3), activation='relu'),
    layers.MaxPooling2D((3, 3)),
    layers.RandomFlip('horizontal'),
    layers.RandomRotation(0.4),
    layers.Dropout(0.2),
    layers.Flatten(),
    layers.Dense(200, activation='relu'),
])

model3.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

history = model3.fit(train_dataset, 
                     epochs=20, 
                     validation_data=validation_dataset, verbose = 0)
```


```python
plt.plot(history.history["accuracy"], label = "training")
plt.plot(history.history["val_accuracy"], label = "validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy")
plt.legend()
```
#### As we can see, our test accuracy stabilizes to more than 70%.
![preprocessing.png](/images/preprocessing.png)
    


##### Part 5: Transfer Learning
#### In this part, we access a pre-existing model that have already proven to be efficient. Then, we incorporate it into the pre-existing model we constructed for our task to improve its accuracy.
```python
IMG_SHAPE = IMG_SIZE + (3,)
base_model = tf.keras.applications.MobileNetV2(input_shape=IMG_SHAPE,
                                               include_top=False,
                                               weights='imagenet')
base_model.trainable = False

i = tf.keras.Input(shape=IMG_SHAPE)
x = base_model(i, training = False)
base_model_layer = tf.keras.Model(inputs = [i], outputs = [x])
```

```python
i = tf.keras.Input(shape=(160, 160, 3))
x = tf.keras.applications.mobilenet_v2.preprocess_input(i)
preprocessor = tf.keras.Model(inputs = [i], outputs = [x])


model4 = tf.keras.models.Sequential([ 
    preprocessor,
    base_model_layer,
    
    layers.Conv2D(50, (3, 3), activation='relu', input_shape=(160, 160, 3)),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.3),
    layers.RandomFlip('horizontal'),
    layers.RandomRotation(0.4),

    layers.Flatten(),
    layers.Dense(2, activation='relu'),
])

model4.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

history = model4.fit(train_dataset, 
                     epochs=20, 
                     validation_data=validation_dataset, verbose = 0)
```

```python
plt.plot(history.history["accuracy"], label = "training")
plt.plot(history.history["val_accuracy"], label = "validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy")
plt.legend()
```
![transfer.png](/images/transfer.png)
    

##### Part 6: Score on Test Data
#### In this part, I tweaked the settings a little bit to see how well it works in those conditions.

```python
# Add more layers
model_test_1 = tf.keras.models.Sequential([ 
    preprocessor,
    base_model_layer,
    
    layers.Conv2D(50, (3, 3), activation='relu', input_shape=(160, 160, 3)),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.3),
    layers.RandomFlip('horizontal'),
    layers.RandomRotation(0.4),

    layers.Flatten(),
    layers.Dense(50, activation='relu'),
])

model_test_1.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

history_1 = model_test_1.fit(train_dataset, 
                     epochs=20, 
                     validation_data=validation_dataset, verbose = 0)
```


```python
# More dropout layers
model_test_2 = tf.keras.models.Sequential([ 
    preprocessor,
    base_model_layer,
    
    layers.Conv2D(50, (3, 3), activation='relu', input_shape=(160, 160, 3)),
    layers.Dropout(0.2),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.2),
    layers.RandomFlip('horizontal'),
    layers.RandomRotation(0.4),
    layers.Dropout(0.2),

    layers.Flatten(),
    layers.Dense(2, activation='relu'),
])

model_test_2.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

history_2 = model_test_1.fit(train_dataset, 
                     epochs=20, 
                     validation_data=validation_dataset, verbose = 0)
```


```python
# More Conv2D
model_test_3 = tf.keras.models.Sequential([ 
    preprocessor,
    base_model_layer,
    
    layers.Conv2D(150, (3, 3), activation='relu', input_shape=(160, 160, 3)),
    layers.Dropout(0.2),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.2),
    layers.RandomFlip('horizontal'),
    layers.RandomRotation(0.4),
    layers.Dropout(0.2),

    layers.Flatten(),
    layers.Dense(2, activation='relu'),
])

model_test_3.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

history_3 = model_test_1.fit(train_dataset, 
                     epochs=20, 
                     validation_data=validation_dataset, verbose = 0)
```


```python
# Change Data Augmentation
model_test_4 = tf.keras.models.Sequential([ 
    preprocessor,
    base_model_layer,
    
    layers.Conv2D(150, (3, 3), activation='relu', input_shape=(160, 160, 3)),
    layers.Dropout(0.2),
    layers.MaxPooling2D((3, 3)),
    layers.Dropout(0.2),
    layers.RandomFlip('vertical'),
    layers.RandomRotation(1),
    layers.Dropout(0.2),

    layers.Flatten(),
    layers.Dense(2, activation='relu'),
])

model_test_4.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

history_4 = model_test_1.fit(train_dataset, 
                     epochs=20, 
                     validation_data=validation_dataset, verbose = 0)
```


```python
# Visualization of the results
fig, axs = plt.subplots(2, 2, sharey = True)
fig.set_size_inches(5.5, 4.5)

axs[0,0].plot(history_1.history["accuracy"], label = "training")
axs[0,0].plot(history_1.history["val_accuracy"], label = "validation")
axs[0,0].set(xlabel = "epoch", ylabel = "accuracy", title = "Test Model 1")
axs[0,0].legend()

axs[0,1].plot(history_2.history["accuracy"], label = "training")
axs[0,1].plot(history_2.history["val_accuracy"], label = "validation")
axs[0,1].set(xlabel = "epoch", ylabel = "accuracy", title = "Test Model 2")
axs[0,1].legend()

axs[1,0].plot(history_3.history["accuracy"], label = "training")
axs[1,0].plot(history_3.history["val_accuracy"], label = "validation")
axs[1,0].set(xlabel = "epoch", ylabel = "accuracy", title = "Test Model 3")
axs[1,0].legend()

axs[1,1].plot(history_4.history["accuracy"], label = "training")
axs[1,1].plot(history_4.history["val_accuracy"], label = "validation")
axs[1,1].set(xlabel = "epoch", ylabel = "accuracy", title = "Test Model 4")
axs[1,1].legend()
plt.tight_layout()
```
#### Performance wise, all the four test models achieved a very high test accuracy. However, it does seem that the first and fourth model are more consistent. 
![test_models.png](/images/test_models.png)


##### Part 7: Conclusion
#### In this project, I got familiar with the methods of image classification. From augmentation to numerical processing to transfer learning, each of them, when applied wisely, could all contribute to an overall higher performance.