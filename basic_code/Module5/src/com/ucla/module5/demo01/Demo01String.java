package com.ucla.module5.demo01;

public class Demo01String {
    public static void main(String[] args) {
        String str1 = new String();  // Nothing
        System.out.println("The first string is " + str1);

        char[] charArray = { 'A', 'B', 'C'};
        String str2 = new String(charArray);
        System.out.println(str2);

        byte[] byteArray = {97, 98, 99};
        String str3 = new String(byteArray);
        System.out.println(str3);
    }
}
