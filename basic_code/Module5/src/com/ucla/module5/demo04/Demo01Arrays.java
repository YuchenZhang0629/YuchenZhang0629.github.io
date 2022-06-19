package com.ucla.module5.demo04;

import java.util.Arrays;

public class Demo01Arrays {
    public static void main(String[] args) {
        int[] intArray = {1,2,3};
        String intStr = Arrays.toString(intArray);

        int[] Array1 = {1,5,2,4,3,7,1,9,0};
        Arrays.sort(Array1);

        String[] Array2 = {"cce","bbb","aaa","ccd"};
        Arrays.sort(Array2);
        System.out.println(Array2[2]);
    }
}
