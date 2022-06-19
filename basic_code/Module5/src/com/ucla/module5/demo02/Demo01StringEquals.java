package com.ucla.module5.demo02;

public class Demo01StringEquals {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = "Hello";
        char[] Array = {'H','e','l','l','o'};
        String str3 = new String(Array);

        System.out.println(str1.equals(str2));
        System.out.println("Hello".equals(str3));

        String strA = "Java";
        String strB = "java";
        System.out.println(strA.equalsIgnoreCase(strB));

    }
}
