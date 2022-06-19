package com.ucla.module5.demo02;

public class Demo02StringGet {
    public static void main(String[] args) {
        String original = "Rhjairaonrnovowa";
        int length = original.length();
        System.out.println(length);

        String str1 = "Hello";  // unchanged
        String str2 = "World";  // unchanged
        String str3 = str1.concat(str2);  // HelloWorld

        int index = original.indexOf("onrno");
        System.out.println(index );

    }
}
