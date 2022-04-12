# MasksPlease
This repository contains code for PIC16B project of face mask detection in real time.

```
X..
.OX
.OX
```

Here will be the place for our Proposal!

# Abstract
#### The problem: 
During the period of pandemic, face masks and social distance are necessary for everyone’s safety concerns; however, as shown on the school’s website, there are still new COVID cases being discovered almost every day in UCLA while we can still see many students walking around the campus without the mask on.  
If we can build a real-time face mask detection program, we could provide friendly reminders to those who didn’t put their masks on so to create a safer environment on campus to solve the problem that it is difficult to check and make sure everyone has followed the guidance.
#### The overall approach:
The goal of this face mask detection project is to create an image recognition system by executing image classification accurately.        
In order to achieve detecting masks in images first and then later try to detect masks in real-time video streams, we will need to build and train our face mask detector on a training set then test the detector on a test set, also making any further modifications if necessary.

# Planned Deliverables
We are aiming to create a real-time face mask detection system that can accurately classify people who have worn the masks and those who haven't so to provide a friendly reminder to those haven't in order to create a safer campus environment.  
We will be making a face mask detection system and the core will be building a successful face mask detector.

#### “Partial success.”
Even if our real-time face masks detection system is not perfect, we would still expect a decent deliver of a face mask detection system working accurately on images. Ideally it should give a low false positive rate (where people get warned for not wearing a mask when they actually do. Subjectively speaking, this is definitely what people wouldn't want to see).
The interface in this case would be a Jupyter notebook containing the necessary code.

#### “Full success.” 
We will be making a real-time face masks detection system that can accurately detect whether a person has worn a mask or not even taken consideration of variables such as gender, race, and even the pattern, size, color, fit, etc, of the masks.    
The interface in this case would be a Jupyter notebook containing the necessary code.

#### “Potential applications in a broader scope”
The current facial recognition systems in unlocking mobile devices, signing into our personal online accounts, and at some security checks, require the user to take off his mask and reveal his entire face. This is not only burdensome, but also indirectly increases the chances of COVID spread because it increases one's exposure and might discourage one from wearing masks. As a result, we believe that if the facial recognition system could be improved to being able to recognize an individual only by his exposed part of his face with mask on, for instance, eyes, nostrills, and eyebrows, it could not only bring convenience but also further help decreasing chances of covid spread.

# Resources Required
Data sets are the essential resources required for this project.   
We would need data that can be seperated into three groups: 1) images of people wearing the masks correctly, 2) images of people wearing the masks incorrectly, and 3) images of people not wearing masks     
Link to data set:     
- 67,049 images of the Correctly Masked Face Dataset: https://github.com/cabani/MaskedFace-Net    
- 66,734 images of the Incorrectly Masked Face Dataset: https://github.com/cabani/MaskedFace-Net
- 20k+ images of the Not Masked Face Dataset: https://susanqq.github.io/UTKFace/

# Tools and Skills Required
The tools and skills that are required for this project would be TensorFlow, OpenCV and an understanding of the rationale behind machine learning or deep learning.
- TensorFlow: will be needed to build our model. (From the schedule, we will learned about TensorFlow in PIC16B during week 6)
- OpenCV: images inside the library will be needed to train the face detection system
- Will need to understand the core pricinples of machine learning or deep learning to decide on the best algorithm to train

# What We Will Learn
First, we will learn all the tools and skills required in this project, as mentioned in the previous section. These include TensorFlow, OpenCV, and an overall understanding of the rationale behind machine learning / deep learning. In addition, we have to get familiar with designing the best algorithms that would make an otherwise hard task as accessible as possible. Furthermore, new platforms like GitHub will also contribute to our coherence in not only doing research but also presenting our findings to the public. Last but not least, we will learn how to collaborate!

# Risks and Limitations
First, due to our limited knowledge in machine learning / deep learning, we may not be able to select the best type of algorithm/neural network for our task. Second, even if our face mask detection algorithm works great on our datasets, its applicability to real-life situations is uncertain and is contingent on several external factors, such as how representative our training/testing datasets are, and quality of images captured by the camera and delivered to the algorithm (suppose our face mask detection algorithm will eventually be implemented in hardwares). Besides, the data related to our topic is not easily quantifiable. Data usually comes in the form of images instead of numerical or categorical values which we could easily capitalize in model building or machine learning.

# Ethics
1. What groups of people have the potential to benefit from the existence of our product?
People of races that are well represented in our training datasets may benefit from our algorithm. Since the algorithm would be well trained on recognizing face masks on faces similar to theirs, the algorithm would have higher classification accuracy for these individuals and thus protect the spread of COVID-19 more effectively in these populations. For regions with face mask mandates, an algorithm like ours that can automatically identify people without masks can reduce the cost of law enforcement (e.g. people who are trying to hide the fact that they are not wearing a mask will be more easily spotted), and significantly reduce the instances where it end up being the people beside someone to warn him to wear the mask instead of him knowing to do it himself.

2. What groups of people have the potential to be harmed from the existence of our product?
People of races that are not well represented in our training datasets may be harmed or may not benefit from the algorithm to the same extent than other racial groups. Because the algorithm would be worse at recognizing face masks on their faces, if false positives occur (i.e. the algorithm detects a face mask when there isn't one) the whole population would have a greater risk for infection, but if false negatives occur (i.e. the algorithm fails to detect a face mask when there is one) the individual would attract unnecessary attention from others. However, since it is easy for humans to tell whether someone is wearing a mask or not, such false negatives should be quickly resolved and should be unlikely to result in serious consequences. But still, such unnecessary troubles should have been avoided by having more representative training datasets. 

3. Will the world become an overall better place because of the existence of our product?
Yes, we believe so. Even though the algorithm may not work equally well on all populations, since the spread of COVID-19 is not race-sensitive, we would contribute to slowing down the pandemic even if we were only able to correctly spot one person without a mask. There are two key assumptions that lead us to conclude in this way:
- Face masks prevent the spread of COVID-19
- If the aforementioned applications will be widely adopted, even the smallest details could make people's lives easier significantly 
- The world is a better place when there are fewer people who have contracted COVID-19

# Tentative Timeline
- After two weeks: Be familiar with the types of machine learning algorithms / deep neural networks that are commonly used in computer vision. Decide on which model to use. Presentation: an overview of all types of algorithms in computer vision, and the rationale behind our choice of algorithm.
- After four weeks: Have the machine learning pipeline / neural network set up, aim for (at least) an above-chance accuracy.
- After six weeks: Have the final pipeline/neural network ready, achieve 90%+ accuracy. 



