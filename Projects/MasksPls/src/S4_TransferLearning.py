# First we need to read in the required packages and the already defined function
import matplotlib.pyplot as plt
import numpy as np
from sklearn.model_selection import train_test_split
import tensorflow as tf
from tensorflow.keras import layers, models
from S3_ModelTraining import testing

categories = ['without_mask', 'with_mask']
# loading the saved numpy arrays in the previous code
data=np.load('data.npy')
target=np.load('target.npy')

# Performing the train test split of the dataset
train_data,test_data,train_target,test_target=train_test_split(data,target,test_size=0.1)

# create a preprocessing layer
i = tf.keras.Input(shape=(100, 100, 3))
x = tf.keras.applications.mobilenet_v2.preprocess_input(i)
preprocessor = tf.keras.Model(inputs = [i], outputs = [x])

# we sought to use a more sophisticated, pre-trained model to address the observed racial bias. We used MobileNet V2.
IMG_SHAPE = (160, 160, 3)
# load MobileNetV2 model
base_model = tf.keras.applications.MobileNetV2(input_shape=IMG_SHAPE,
                                               include_top=False,
                                               weights='imagenet')
# freeze the base model
base_model.trainable = False
# build a layer using the imported MobileNetV2 model
i = tf.keras.Input(shape=IMG_SHAPE)
x = base_model(i, training = False)
base_model_layer = tf.keras.Model(inputs = [i], outputs = [x])

# build a new model using transfer learning
model_transfer = models.Sequential([
    # preprocessing layer
    preprocessor,
    # data augmentation
    layers.RandomFlip('horizontal'), 
    layers.RandomRotation(0.05),
    # MobileNetV2 layer
    base_model_layer,
    # GlobalMaxPooling layer to reduce dimensionality
    layers.GlobalMaxPooling2D(),
    # Dense layer
    layers.Dense(2),
])

# compile the model
model_transfer.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])
# from_logits=True compute softmax when evaluting loss function
# metrics=['accuracy'] want to see how accurate on the data

# fit the model on 80% of the training set, evaluate on the rest
history_transfer = model_transfer.fit(train_data,
                     train_target, 
                     epochs=20,
                     validation_split=0.2)


# Our results proved to be pretty precise and there is no apparent sign of overfitting.

plt.plot(history_transfer.history["accuracy"], label = "training")
plt.plot(history_transfer.history["val_accuracy"], label = "validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy") # gca: get current axis 
plt.legend()

# testing on black people
testing("data/Testing_Black", model_transfer)

# testing on Asian people
testing("data/Testing_Asian", model_transfer)

# testing on white people
testing("data/Testing_White", model_transfer)
