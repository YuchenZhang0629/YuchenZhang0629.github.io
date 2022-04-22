{
 "cells": [
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "# Arrays"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "### Question 4: Median of Two Sorted Arrays\n",
    "Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays. The overall run time complexity should be O(log (m+n))."
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 1,
   "metadata": {},
   "outputs": [],
   "source": [
    "def findMedianSortedArrays(nums1, nums2):\n",
    "    # Iterate over the shorter of the two\n",
    "    if len(nums1) < len(nums2):\n",
    "        A = nums1\n",
    "        B = nums2\n",
    "    else:\n",
    "        A = nums2\n",
    "        B = nums1\n",
    "    total_len = len(A) + len(B)\n",
    "    half = total_len // 2\n",
    "    \n",
    "    # Two pointers, pointing left or right\n",
    "    left = 0\n",
    "    right = len(A)-1\n",
    "    while True:\n",
    "        # Find the index that gives us the left and right partitions\n",
    "        i = (left+right)//2  # A\n",
    "        j = half - i - 2  # B\n",
    "        Aleft = A[i] if i >= 0 else float(\"-infinity\")\n",
    "        Aright = A[i+1] if i < len(A) else float(\"infinity\")\n",
    "        Bleft = B[j] if j >= 0 else float(\"-infinity\")\n",
    "        Bright = B[j+1] if j < len(B) else float(\"infinity\")\n",
    "        \n",
    "        if Aleft <= Bright and Bleft <= Aright:\n",
    "            if total % 2 == 0:\n",
    "                return min(Aright, Bright)\n",
    "            else:\n",
    "                return (max(Aleft, Bleft) + min(Aright, Bright))/2\n",
    "        elif Aleft > Bright:\n",
    "            right = i - 1\n",
    "        else:\n",
    "            left = i + 1"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "### Question 62: Unique Paths\n",
    "There is a robot on an m x n grid. The robot is initially located at the top-left corner (i.e., grid[0][0]). The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 11,
   "metadata": {},
   "outputs": [],
   "source": [
    "import numpy as np\n",
    "def uniquePaths(m, n):\n",
    "    grid = np.zeros([m,n]).astype(int)\n",
    "    for i in range(n):\n",
    "        grid[0][i] = 1\n",
    "    for i in range(m):\n",
    "        grid[i][0] = 1\n",
    "        \n",
    "    for i in range(1,m):\n",
    "        for j in range(1,n):\n",
    "            grid[i][j] = grid[i-1][j]+grid[i][j-1]\n",
    "    return grid[-1][-1]"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "### Question 63: Unique Paths with Obstacles\n",
    "An obstacle and space are marked as 1 or 0 respectively in grid. A path that the robot takes cannot include any square that is an obstacle."
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 30,
   "metadata": {},
   "outputs": [],
   "source": [
    "\"\"\"\n",
    "The first row and first column: if any of them is obstacle, then all following are zeros\n",
    "If an obstacle occurs: then outcome only comes from the other direction\n",
    "\"\"\"\n",
    "def uniquePathsWithObstacles(obstacleGrid):\n",
    "    row = len(obstacleGrid)\n",
    "    col = len(obstacleGrid[0])\n",
    "    grid = np.zeros([row,col]).astype(int)\n",
    "    grid[0][0] = 1-obstacleGrid[0][0]\n",
    "    for i in range(1,col):\n",
    "        if obstacleGrid[0][i] == 1:\n",
    "            grid[0][i] = 0\n",
    "        else:\n",
    "            grid[0][i] = grid[0][i-1]\n",
    "    for i in range(1,row):\n",
    "        if obstacleGrid[i][0] == 1:\n",
    "            grid[i][0] = 0\n",
    "        else:\n",
    "            grid[i][0] = grid[i-1][0]\n",
    "    for i in range(1,row):\n",
    "        for j in range(1,col):\n",
    "            if obstacleGrid[i][j] == 1:\n",
    "                grid[i][j] = 0\n",
    "            else:\n",
    "                if obstacleGrid[i-1][j] == 1:\n",
    "                    grid[i][j] = grid[i][j-1]\n",
    "                elif obstacleGrid[i][j-1] == 1:\n",
    "                    grid[i][j] = grid[i-1][j]\n",
    "                else:\n",
    "                    grid[i][j] = grid[i-1][j]+grid[i][j-1]\n",
    "    return grid[-1][-1]"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "### Question 2239: Find Closest Number to Zero\n",
    "Given an integer array nums of size n, return the number with the value closest to 0 in nums. If there are multiple answers, return the number with the largest value."
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 11,
   "metadata": {},
   "outputs": [],
   "source": [
    "\"\"\"\n",
    "max of an array compares the values in sequential order. will be useful in some occasions\n",
    "\"\"\"\n",
    "def findClosestNumber(nums):\n",
    "    return max([-abs(a), a] for a in nums)[1]"
   ]
  }
 ],
 "metadata": {
  "kernelspec": {
   "display_name": "Python 3",
   "language": "python",
   "name": "python3"
  },
  "language_info": {
   "codemirror_mode": {
    "name": "ipython",
    "version": 3
   },
   "file_extension": ".py",
   "mimetype": "text/x-python",
   "name": "python",
   "nbconvert_exporter": "python",
   "pygments_lexer": "ipython3",
   "version": "3.8.5"
  }
 },
 "nbformat": 4,
 "nbformat_minor": 4
}
