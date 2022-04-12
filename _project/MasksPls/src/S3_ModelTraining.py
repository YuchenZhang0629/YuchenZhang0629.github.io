# First we need to read in the required packages and the already defined function
import matplotlib.pyplot as plt
import numpy as np
from sklearn.model_selection import train_test_split
import tensorflow as tf
from tensorflow.keras import layers
import matplotlib.image as mpimg
from S2_DataPreprocessing import DataPreprocess

# loading the saved numpy arrays in the previous code
data=np.load('data.npy')
target=np.load('target.npy')

# Performing the train test split of the dataset
train_data,test_data,train_target,test_target=train_test_split(data,target,test_size=0.1)


# Practice with data augmentation
RandomFlip = tf.keras.layers.RandomFlip()
plt.figure(figsize=(10, 10))
image = mpimg.imread("data/without_mask/23.jpg")
for i in range(9):
    ax = plt.subplot(3, 3, i + 1)
    augmented_image = RandomFlip(tf.expand_dims(image, 0))
    plt.imshow(augmented_image[0] / 255)
    plt.axis('off')

RandomRotation = tf.keras.layers.RandomRotation(0.5)
plt.figure(figsize=(10, 10))
image = mpimg.imread("data/with_mask/000 copy 36.jpg")
for i in range(9):
    ax = plt.subplot(3, 3, i + 1)
    augmented_image = RandomRotation(tf.expand_dims(image, 0))
    plt.imshow(augmented_image[0] / 255)
    plt.axis('off')



# Model building
"""
In our model we include:
1. a preprocessor
2. data augmentation: randomly flipping & rotating the images
3. two convolutional 2D layers
4. two max pooling layers
5. two dropout layers to avoid overfitting 
6. a flatten layer
7. and finally two dense layers to match the number of classes in the output
"""
# create a preprocessing layer
i = tf.keras.Input(shape=(100, 100, 3))
x = tf.keras.applications.mobilenet_v2.preprocess_input(i)
preprocessor = tf.keras.Model(inputs = [i], outputs = [x])

model = tf.keras.Sequential([
    # preprocessing
    preprocessor,
    # data augmentation with flip and rotation
    layers.RandomFlip('horizontal'), 
    layers.RandomRotation(0.05),
    # The first CNN layer is a Convolution layer of a kernel size 3*3
    # It learns the base features and applies'relu' nonlinear transformation.
    # Also specifying the input shape here to be 100
    layers.Conv2D(100,(3,3), activation=layers.LeakyReLU(), input_shape=data.shape[1:]),
    # MaxPooling2D((2, 2)) 2*2 the size of window to find the max
    layers.MaxPooling2D((2, 2)),
    # layers.Dropout(0.5) force the model to not fit too closely, help relieve overfitting
    layers.Dropout(0.5),
    # The second convolution layer
    layers.Conv2D(100,(3,3), activation=layers.LeakyReLU()),
    # MaxPooling layer
    layers.MaxPooling2D((2, 2)),
    # Flatten layer to stack the output convolutions from second convolution layer
    layers.Flatten(),
    # layers.Dropout(0.5) force the model to not fit too closely, help relieve overfitting
    layers.Dropout(0.5),
    # Dense layer of 25 neurons
    layers.Dense(25, activation=layers.LeakyReLU()),
    layers.Dense(2,activation='softmax')
])
model.summary()

# compile the model
model.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])
# fit the model on 80% of the training set, evaluate on the rest
history = model.fit(train_data,
                     train_target, 
                     epochs=20,
                     validation_split=0.2)
"""
Our results proved to be pretty precise and there
is no apparent sign of overfitting.
"""
plt.plot(history.history["accuracy"], label = "training")
plt.plot(history.history["val_accuracy"], label = "validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy") # gca: get current axis 
plt.legend()

print(model.evaluate(test_data,test_target))


# Testing Model on data sets of different racial group
categories = ['without_mask', 'with_mask']
def testing(data_path, model):
    """
    This function will preprocess the data set, test the model on the data set, 
    and create visualization
    """
    # preprocess the data
    data_test, target_test = DataPreprocess(data_path)
    # Test model on the data set
    print(model.evaluate(data_test, target_test))
    # visualize
    y_pred = model.predict(data_test)
    labels_pred = y_pred.argmax(axis = 1)
    plt.figure(figsize=(10,10))
    for i in range(30):
        if i < 15:
            plt.subplot(6,5,i+1)
            plt.xticks([])
            plt.yticks([])
            plt.grid(False)
            plt.imshow(data_test[i])
            plt.xlabel(categories[labels_pred[i]])
        else:
            plt.subplot(6,5,i+1)
            plt.xticks([])
            plt.yticks([])
            plt.grid(False)
            plt.imshow(data_test[i+25])
            plt.xlabel(categories[labels_pred[i+25]])
testing("data/Testing_Black")
testing("data/Testing_Asian")
testing("data/Testing_White")