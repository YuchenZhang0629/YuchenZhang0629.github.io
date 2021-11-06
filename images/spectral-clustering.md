---
layout: post
title: Blog4 - Spectral Clustering
---

##### Introduction 
#### In this blog post, I'll write a tutorial on how to perform a spectral clustering algorithm. In each part, I'll cover some important procedures respectively, and in the end, I'll show you a more holistic integration of the algorithm. But before we start, let's get the necessary packages ready and establish the setup for our project.


```python
import numpy as np
from sklearn import datasets
from matplotlib import pyplot as plt
import sklearn
from sklearn.cluster import KMeans
import scipy
```

##### This is what the traditional K_means clustering excels at. Round shaped datasets are easy to classify with such method. 
```python
n = 200
np.random.seed(1111)
X, y = datasets.make_blobs(n_samples=n, shuffle=True, random_state=None, centers = 2, cluster_std = 2.0)
plt.scatter(X[:,0], X[:,1])
```
![normal_kmeans.png](/images/normal_kmeans.png)
    


```python
km = KMeans(n_clusters = 2)
km.fit(X)

plt.scatter(X[:,0], X[:,1], c = km.predict(X))
```
![kmeans_result.png](/images/kmeans_result.png)


##### However, when the distribution is harder to classify based on their means and distance to it, K_means begins to perform badly. As we could see, many of the outcomes does not fit with what we would actually consider the appropriate clustering. Here we generate the datapoints X and the actual clustering y for illustrative purposes.
```python
np.random.seed(1234)
n = 200
X, y = datasets.make_moons(n_samples=n, shuffle=True, noise=0.05, random_state=None)
plt.scatter(X[:,0], X[:,1])
```
![moon.png](/images/moon.png)
    

```python
km = KMeans(n_clusters = 2)
km.fit(X)
plt.scatter(X[:,0], X[:,1], c = km.predict(X))
```
![kmeans_moon.png](/images/kmeans_moon.png)
#### As we could see, K_means algorithm does have its limits. That's why we should learn about spectral clustering! Now let's jump into the main part of the project.



##### Part A: The Similarity Matrix
##### The first thing we need is what called the similarity matrix. Here's how we get it. We first calculate the euclidian distance of a set of observations by taking the square root of the square of the difference between each pair of observations, and transform the indexes of the distance matrix by whether it is larger than a particular threshold we define ourselves. The threshold whereby we call epsilon. In this demonstration I'll use epsilon = 0.4.

```python
# Using sklearn to compute the distances
A = sklearn.metrics.pairwise_distances(X, Y=None, metric='euclidean')
epsilon = 0.4
for i in range(200):
    for j in range(200):
        if i != j:
            if A[i,j] < epsilon:
                A[i,j] = 1
            else:
                A[i,j] = 0
        else: 
            A[i,j] = 0

A
```

    array([[0., 0., 0., ..., 0., 0., 0.],
           [0., 0., 0., ..., 0., 0., 0.],
           [0., 0., 0., ..., 0., 1., 0.],
           ...,
           [0., 0., 0., ..., 0., 1., 1.],
           [0., 0., 1., ..., 1., 0., 1.],
           [0., 0., 0., ..., 1., 1., 0.]])


##### Part B: The Cut and Volume Terms
##### The next thing we need to do is to see how much the divide is between such two clusters. For each pair of datapoints where the first belongs to the first cluster and the second belongs to the second, we add the corresponding value in the similarity matrix, and in this case, 1 if the distance is smaller than epsilon, or 0 otherwise.

```python
def cut(A,y):
    cut = 0
    for i in range(n):
        for j in range(n):
            if y[i] == 0 and y[j] == 1:
                cut = cut + A[i,j]
    return cut
```
##### Now let's compute the cut for our dataset.
```python
cut(A, y)
```
    13.0


##### With those in mind let's move on to the volume term. Frankly speaking, the volume terms are the total number of significant data pairs that are smaller than the threshold in the similarity matrix. It measures how large the clusters are. A good clustering should give the volume for clusters approximately equally large. Here we implement a vols function to calculate the volumes for the two clusters.

```python
def vols(A, y):
    cluster_0 = sum([sum(i) for i in A[y == 0]])
    cluster_1 = sum([sum(i) for i in A[y == 1]])
    return cluster_0, cluster_1
```

##### Now we define a term called normcut. It is calculated as follows. A pair of clusters are considered to be a good partition if the norm cut is small
```python
"""
It is apparent that the normcut for the actual labels is 
smaller than that of the fake labels
"""
fake_y = km.predict(X)
def normcut(A,y):
    return cut(A, y) * (1/vols(A, y)[0] + 1/vols(A, y)[1])
round(normcut(A,y), 3), round(normcut(A,fake_y), 3)
```

    (0.012, 0.138)



##### Part C: Finding the Cluster Vector
#### We need to find a cluster vector y such that the normcut of A with y is small. To do this, we need to construct a vector Z that contains the information of each observation's clustering. The sign corresponds to the cluster, and the value is the reciprocal of the volume of the cluster.

```python
def transform(A,y):
    z_val = []
    for i in y:
        if i == 0:
            z_val.append(1/vols(A, y)[0])
        else:
            z_val.append(-1/vols(A, y)[1])
    return z_val
```

##### We construct a diagonal matrix D whose entries correspond to the row sums of the similarity matrix. After checking, it is apparent that this method yields a really accurate outcome when compared to the real clustering.
```python
D = np.diag([sum(i) for i in A])
Z = np.array(transform(A,y))
norm = (Z@(D-A)@Z)/(Z@D@Z)
np.isclose(normcut(A,y), norm), np.isclose(Z@D@np.ones(n), 0)
```

    (True, True)



##### Part D: Minimizing the Normcut
#### Mathematically, we could substituting for Z its orthogonal complement relative to D1 . In the code below, the orth_obj function which handles this. The specific minimizing process is handled by the 'minimize' function in 'scipy.optimize'.

```python
def orth(u, v):
    return (u @ v) / (v @ v) * v

e = np.ones(n) 

d = D @ e

def orth_obj(z):
    z_o = z - orth(z, d)
    return (z_o @ (D - A) @ z_o)/(z_o @ D @ z_o)
```

```python
Z_ = scipy.optimize.minimize(orth_obj, Z, method = "Nelder-Mead")["x"]
```


##### Part E: Visualization of Our Clustering
#### Plot the original data in X and apply colors corresponding to the clusters we just obtained
```python
plt.scatter(X[:,0], X[:,1], c = (Z_<0).astype(int) + 1)
```
![moon_true.png](/images/moon_true.png)
    


##### Part F: The Laplacian Matrix
#### In this part, we should attempt to get the eigenvector corresponding to the second smallest eigenvalue, because the one corresponding to the smallest eigenvalue is np.ones(n). The Rayleigh-Ritz Theorem states that the minimizing $\mathbf{z}$ must be the solution with smallest eigenvalue of the generalized eigenvalue problem $$ (\mathbf{D} - \mathbf{A}) \mathbf{z} = \lambda \mathbf{D}\mathbf{z}\;, \quad \mathbf{z}^T\mathbf{D}\mathbb{1} = 0$$. It is equivalent to solving the problem $$ \mathbf{D}^{-1}(\mathbf{D} - \mathbf{A}) \mathbf{z} = \lambda \mathbf{z}\;, \quad \mathbf{z}^T\mathbb{1} = 0\;.$$.

```python
L = np.linalg.inv(D)@(D-A)
Eig = np.linalg.eig(L)
eigenvector = Eig[1][:, np.where(Eig[0] == np.sort(Eig[0])[1])[0][0]]
plt.scatter(X[:,0], X[:,1], c = (eigenvector<0).astype(int) + 1)
```
![Lap_output.png](/images/Lap_output.png)

    


#### To check the fact that z_eig should be proportional to z_min, we divide these two vectors. The the result won't be exact because minimization has limited precision by default, but it should be close.
```python
(eigenvector/Z_)[:6]
# It is apparent that they are roughly proportional but not exact
```

    array([-223.31988358, -169.03214032, -144.19190858, -144.59840003,
           -165.29314041, -155.80226008])



##### Part G: Synthesis
#### In this part, we integrate all the methods we implemented in previous parts to generalize one function that does all the work.
```python
def spectral_clustering(X, epsilon):
    """
    Constructing the simiality matrix by first getting
    the euclidian matrix then finding the entries that
    are smaller than the given epsilon
    """
    A = sklearn.metrics.pairwise_distances(X, Y=None, metric='euclidean')
    n = len(A)
    # Transform the matrix into 0 and 1
    for i in range(n):
        for j in range(n):
            if i != j:
                if A[i,j] < epsilon:
                    A[i,j] = 1
                else:
                    A[i,j] = 0
            else: 
                A[i,j] = 0
    
    """
    Constructing the Laplacian Matrix from the Diagonal Matrix 
    D and the similarity matrix A we just got
    """
    D = np.diag([sum(i) for i in A])
    L = np.linalg.inv(D)@(D-A)
    
    """
    Computing the eigenvector corresponding to the second 
    smallest eigenvalue of the Laplacian Matrix
    """
    Eig = np.linalg.eig(L)
    eigenvector = Eig[1][:, np.where(Eig[0] == np.sort(Eig[0])[1])[0][0]]
    
    """
    From the eigenvectors' signs, we could get the clustering
    of the observations. To do this, we turn Booleans into 
    integers, and return the Clusters values.
    """
    Cluster = (eigenvector<0).astype(int)
    
    return Cluster
```


##### Part H: Test the result on more moon datasets
#### In this part, I tested the function on datasets generated by different parameters. I tweaked the noise, and altered the epsilon to see how to perform best.

```python
# Here, I increased the noise
n = 1000
X, y = datasets.make_moons(n_samples=n, shuffle=True, noise=0.1, random_state=None)
Clustering = spectral_clustering(X, 0.4)
plt.scatter(X[:,0], X[:,1], c = Clustering)
```
![high_noise.png](/images/high_noise.png)

    


```python
# When I increased the noise even more, it helps if I decrease the epsilon.
n = 1000
X, y = datasets.make_moons(n_samples=n, shuffle=True, noise=0.15, random_state=None)
Clustering = spectral_clustering(X, 0.3)
plt.scatter(X[:,0], X[:,1], c = Clustering)
```
![highnoise_outcome.png](/images/highnoise_outcome.png)

    


##### Part I: The Bull's Eye dataset
#### Now we're testing on a completely different shape -- The Bull's Eye! Turns out it works out pretty fine
```python
n = 1000
X, y = datasets.make_circles(n_samples=n, shuffle=True, noise=0.05, random_state=None, factor = 0.4)
plt.scatter(X[:,0], X[:,1])
```   
![bullseye.png](/images/bullseye.png)
    
##### The inaccurate classification given by K_means:
```python
km = KMeans(n_clusters = 2)
km.fit(X)
plt.scatter(X[:,0], X[:,1], c = km.predict(X))
``` 
![bullseye_kmeans.png](/images/bullseye_kmeans.png)
    

##### The correct classification given by spectral clustering:
```python
# The value epsilon of 0.3 does a perfect job in clustering.
Clustering = spectral_clustering(X, 0.3)
plt.scatter(X[:,0], X[:,1], c = Clustering)
``` 
![bullseye_spectral.png](/images/bullseye_spectral.png)
    


##### Part J: Conclusion
#### In this project, I demonstrated the most important steps of spectral clustering, and some of the mathematical ideas behind it. It turns out to be quite useful in many cases. I'll see you again next time!
