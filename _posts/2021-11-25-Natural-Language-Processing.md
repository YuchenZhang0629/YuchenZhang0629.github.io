---
layout: post
title: Blog6 - Natural Language Processing
---

##### Introduction
#### In this blog post, I am going to show how to use Tensorflow to determine whether a piece of news is fake or not. We achieve this by looking at either the title, the text of the information itself, or both. With the assistance of enbedding, standardization, and our familiar techniques like max poooling, dense, and droo out, we can achieve this goal efficiently. But first, let's load the required packages.

```python
import pandas as pd
from sklearn.feature_extraction import text
import tensorflow as tf
import numpy as np
from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras.layers.experimental.preprocessing import TextVectorization
from tensorflow.keras.layers.experimental.preprocessing import StringLookup
import re
import string
from tensorflow.keras import layers
from tensorflow.keras import losses
from matplotlib import pyplot as plt
from sklearn.decomposition import PCA
import plotly.express as px
```

##### Part 1: Acquiring the dataset
```python
train_url = "https://github.com/PhilChodrow/PIC16b/blob/master/datasets/fake_news_train.csv?raw=true"
dataset = pd.read_csv(train_url)
dataset.drop("Unnamed: 0", inplace = True, axis=1)
```

##### Part 2: Making datasets
#### In the second part, we do some brief data cleaning. In order to speed up the training process, we need to delete the uninformative words like "the", "and", and "a", etc. In addition, we remove the syntax symbols and leave only the text for Tensorflow to interpret. 
#### We create a dictionary with two parts: an input involving the title and text, and the output part which indexes whether the news is fake or not.
```python
# Here we are excluding the stopwords
stop = text.ENGLISH_STOP_WORDS

"""
For each column of the dataset, we exclude the non-informative words. 
Then, we create a tensor that have the input and output sections. We
then divide it into train and test according to the specifieed p, 
which is in this case, 0.2
"""
def make_dataset(data, p):
    ncol = data.shape[1]
    for i in range(ncol-1):
        data.iloc[:,i] = data.iloc[:,i].apply(lambda x: ' '.join(
            [word for word in x.split() if word not in (stop)]))
    
    dataset_tf = tf.data.Dataset.from_tensor_slices(
        (
            {
                "title": dataset[["title"]], 
                "text": dataset[["text"]]
            },
            {
                "fake": dataset[["fake"]]
            }
        )
    )
    dataset_tf = dataset_tf.shuffle(buffer_size = len(dataset_tf))
    train_size = int((1-p)*len(dataset_tf))
    
    training = dataset_tf.take(train_size).batch(20)
    validation = dataset_tf.skip(train_size).batch(20)
    
    return training, validation

training, validation = make_dataset(dataset, 0.2)
```


##### Part 3: Creating models
#### In our first model, we use only the title for training and modeling. We would like to convert the text into integers for the program to interpret. To do this, we convert them to their rank in frequency.

```python
size_vocabulary = 3000

def standardization(input_data):
    lowercase = tf.strings.lower(input_data)
    no_punctuation = tf.strings.regex_replace(lowercase,
                                  '[%s]' % re.escape(string.punctuation),'')
    return no_punctuation 

vectorize_layer = TextVectorization(
    standardize=standardization,
    max_tokens=size_vocabulary, # only consider this many words
    output_mode='int',
    output_sequence_length=500) 

vectorize_layer.adapt(training.map(lambda Input, Output: Input["title"]))
```

```python
# Specifying the input
title_input = keras.Input(
    shape = (1,), 
    name = "title",
    dtype = "string"
)

# layers for processing the titles
"""
We first create a pipeline for the title using embedding and our other
familiar layers. The output dense layer should match the classes in our
output, whch is 2.
"""
title_features = vectorize_layer(title_input)
title_features = layers.Embedding(3000, 3, name = "embedding")(title_features)
title_features = layers.Dropout(0.2)(title_features)
title_features = layers.GlobalAveragePooling1D()(title_features)
title_features = layers.Dropout(0.2)(title_features)
title_features = layers.Dense(32, activation='relu')(title_features)

main = layers.concatenate([title_features], axis = 1)
main = layers.Dense(32, activation='relu')(main)
output = layers.Dense(2, name = "fake")(main)

model1 = keras.Model(
    inputs = [title_input],
    outputs = output
)

model1.compile(optimizer = "adam",
              loss = losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy']
)

history1 = model1.fit(training, 
                     validation_data=validation,
                     epochs = 20, 
                     verbose = False)

plt.plot(history1.history["accuracy"], label="training")
plt.plot(history1.history["val_accuracy"], label="validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy")
plt.legend()
```
    
![model1.png](/images/model1.png)
    


##### The second model
#### In the second model, we use only the text as input. We also use an embedding, two dropouts, a pooling, and a dense layer, same as our first model.
```python
vectorize_layer.adapt(training.map(lambda Input, Output: Input["text"]))

# Specifying the input
text_input = keras.Input(
    shape = (1,), 
    name = "text",
    dtype = "string"
)

# layers for processing the titles
text_features = vectorize_layer(text_input)
text_features = layers.Embedding(3000, 3, name = "embedding")(text_features)
text_features = layers.Dropout(0.2)(text_features)
text_features = layers.GlobalAveragePooling1D()(text_features)
text_features = layers.Dropout(0.2)(text_features)
text_features = layers.Dense(32, activation='relu')(text_features)

main = layers.concatenate([text_features], axis = 1)
main = layers.Dense(32, activation='relu')(main)
output = layers.Dense(2, name = "fake")(main)
```

```python
model2 = keras.Model(
    inputs = [text_input],
    outputs = output
)

model2.compile(optimizer = "adam",
              loss = losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy']
)

history2 = model2.fit(training, 
                     validation_data=validation,
                     epochs = 20, 
                     verbose = False)

plt.plot(history2.history["accuracy"], label="training")
plt.plot(history2.history["val_accuracy"], label="validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy")
plt.legend()
```
 
![model2.png](/images/model2.png)
    


##### The third model
#### In our third model we use both the title and text as input. The difference with the first two moels is that we create two separate vectorization layers for the title and text, each consisting roughly the same as their individual counterparts.

```python
# vectorize_layer.adapt(train.map(lambda x, y: x[input]))
title_input = keras.Input(
    shape = (1,),
    name = "title",
    dtype = "string"
)

text_input = keras.Input(
    shape = (1,), 
    name = "text",
    dtype = "string"
)

"""
In order for us to take in both title and text in the input, we construct
the two vectorizations as follows. Since the adapt method is not directly
callable, we need to define another function to store it.
"""
def vectorization(train,input):
    vectorize_layer.adapt(train.map(lambda x, y: x[input]))
    return vectorize_layer

vectorize_layer_title = vectorization(training,"title")
title_features = vectorize_layer_title(title_input)
title_features = layers.Embedding(size_vocabulary, 10, name="title_embedding")(title_features)
title_features = layers.Dropout(0.2)(title_features)
title_features = layers.GlobalAveragePooling1D()(title_features)
title_features = layers.Dropout(0.2)(title_features)
title_features = layers.Dense(32,activation='relu')(title_features)

vectorize_layer_text = vectorization(training,"text")
text_features = vectorize_layer_text(text_input)
text_features = layers.Embedding(size_vocabulary, 10, name="text_embedding")(text_features)
text_features = layers.Dropout(0.2)(text_features)
text_features = layers.GlobalAveragePooling1D()(text_features)
text_features = layers.Dropout(0.2)(text_features)
text_features = layers.Dense(32,activation='relu')(text_features)

main = layers.concatenate([title_features, text_features], axis = 1)
main = layers.Dense(32, activation='relu')(main)
output = layers.Dense(2, name = "fake")(main)
```


```python
model3 = keras.Model(
    inputs = [title_input, text_input],
    outputs = output
)

model3.compile(optimizer = "adam",
              loss = losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy']
)

history3 = model3.fit(training, 
                      validation_data=validation,
                      epochs = 20, 
                      verbose = False)

plt.plot(history3.history["accuracy"], label="training")
plt.plot(history3.history["val_accuracy"], label="validation")
plt.gca().set(xlabel = "epoch", ylabel = "accuracy")
plt.legend()
```
![model3.png](/images/model3.png)
    


##### Part 4: Model Evaluation
#### For this part, I decide to use the second model involving only the text because it shows less overfitting and is less complicated in nature. We could see that the validation acc

```python
train_url = "https://github.com/PhilChodrow/PIC16b/blob/master/datasets/fake_news_train.csv?raw=true"
testing = pd.read_csv(train_url)
testing.drop("Unnamed: 0", inplace = True, axis=1)
test_train, test_val = make_dataset(testing, 0.2)

testing[['title']] = testing['title'].apply(lambda x: ' '.join([word for word in x.split() if word not in (stop)]))
testing[['text']] = testing['text'].apply(lambda x: ' '.join([word for word in x.split() if word not in (stop)]))

test_tf = tf.data.Dataset.from_tensor_slices(
    (
        {
            "title" : testing[["title"]],
            "text"  : testing[["text"]]
        },
        {
            "fake"  : testing[["fake"]]
        }
    )
)

test_tf = test_tf.batch(100)
model2.evaluate(test_tf)

```

    225/225 [==============================] - 1s 5ms/step - loss: 0.0237 - accuracy: 0.9941

    [0.02366838976740837, 0.9940754771232605]



##### Part 5: Embedding Visualizations
#### In principle component analyses, perform change of basis on the data, so that we could express roughly as much variability in the original dataset by mereging them into fewer new variables. The graph of our embedding is roughly round, which shows that there is no apparent association. However, it is apparent that many of the focus point or controversial topics like protrump, China, India, and US occupy many of the large absolute values on both these axes, which means they are prone to being faked news. 

```python
weights = model2.get_layer('embedding').get_weights()[0]
vocab = vectorize_layer.get_vocabulary()
pca = PCA(n_components=2)
weights = pca.fit_transform(weights)

embedding_df = pd.DataFrame({
    'word' : vocab,
    'x0'   : weights[:,0],
    'x1'   : weights[:,1]
})
fig = px.scatter(embedding_df,
                 x = "x0",
                 y = "x1",
                 size = list(np.ones(len(embedding_df))),
                 size_max = 5,
                 hover_name = "word",
                 color = "x0")

fig.show()
fig.write_html("test.html")
```
{% include test.html %}