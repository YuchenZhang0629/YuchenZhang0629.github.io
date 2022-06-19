package com.ucla.module5.demo02;

public class Demo04StringConvert {
    public static void main(String[] args) {
        char[] chars = "ahgoirauhg0aojpva".toCharArray();
        byte[] bytes = "ahgoirauhg0aojpva".getBytes();

        String str1 = "How Are You Doing";
        String str2 = str1.replace("o","O");
        System.out.println(str1);
        System.out.println(str2);
    }
}
