package com.ucla.module5.demo02;

public class Demo05StringSplit {
    public static void main(String[] args) {
        String str1 = "aaa,bbb,ccc";
        String[] array1 = str1.split(",",str1.length());
        System.out.println(array1);

        String str2 = "aaa.bbb.ccc";
        String[] array2 = str2.split("\\.",str1.length());
    }
}
