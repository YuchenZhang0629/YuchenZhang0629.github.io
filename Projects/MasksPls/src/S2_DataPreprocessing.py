# First we need to read in the required packages
import cv2,os
import numpy as np

# Define a preprocessing function
def DataPreprocess(data_path):
    """
    This function will loop through the data set, preprocess each image
    The input is the path to the data set
    We resize the image to a smaller size to speed up training
    The output will be images and targets in array form
    """
    # Create an empty dictionary to store data
    data_path  = data_path
    categories = ["without_mask", "with_mask"]
    # labels: without_mask, with_mask
    labels = [i for i in range(0, len(categories))]
    # Create an empty dictionary with keys = labels
    label_dict = dict(zip(categories,labels))
    print(label_dict)
    print(categories)
    print(labels)
    # Now loop through the datasets, preprocess each image, 
    # and append the preprocessed image to the empty dictionary created above
    img_size=100
    data=[]
    target=[]
    for category in categories:
        # Construct path to each folder ('without_mask', 'with_mask')
        folder_path=os.path.join(data_path,category)
        img_names=os.listdir(folder_path)
        # Remove ".DS_Store" from the list of image names
        if '.DS_Store' in img_names:
            img_names.remove('.DS_Store')
        # Loop through images in each folder
        for img_name in img_names:
            img_path=os.path.join(folder_path,img_name)
            img=cv2.imread(img_path)
            try:                
                # Resizing the image into 100x100         
                resized=cv2.resize(img,(img_size,img_size))
                # appending the image and the label(categorized) into the list (dataset)
                data.append(resized)
                target.append(label_dict[category])
            except Exception as e:
                print(img_name)
                print('Exception:',e)
                #if any exception rasied, the exception will be printed here.
   
    # now in this part we convert the images and targets into array form, 
    data=np.array(data)
    target=np.array(target)
    return data, target


# Construct our data and target variable
path = "/Users/yuchenzhang/Desktop/2021-2022/FALL/PIC16B/MasksPlease/data"
data, target = DataPreprocess(path)


# baseline performance of our data: see what proportion is with masl
sum(target)/len(target)


# Saving our data and target
np.save("data", data)
np.save("target", target)