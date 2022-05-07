# First we need to read in the required packages
import matplotlib.pyplot as plt
import matplotlib.image as mpimg

# List of file names
nomasks_list1 = ["data/without_mask/23.jpg", 
               "data/without_mask/29.jpg", 
               "data/without_mask/96.jpg", 
               "data/without_mask/22.jpg",
               "data/without_mask/0_0_zhangluyi_0082.jpg",
               "data/without_mask/74.jpg"]

# Loop through all images, display them
for i in range(6):   
    ax = plt.subplot(2, 3, i + 1)
    # read the image and show it 
    plt.imshow(mpimg.imread(nomasks_list1[i]))
    plt.axis("off")
    
# List of file names
masks_list1 = ["data/with_mask/000 copy 36.jpg", 
               "data/with_mask/0_0_0 copy 21.jpg", 
               "data/with_mask/2398.png", 
               "data/with_mask/with_mask381.jpg",
               "data/with_mask/with_mask316.jpg",
               "data/with_mask/0502.png"]

# Loop through all images, display them
for i in range(6):   
    ax = plt.subplot(2, 3, i + 1)
    # read the image and show it 
    plt.imshow(mpimg.imread(masks_list1[i]))
    plt.axis("off")
    