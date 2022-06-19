package com.ucla.module5.demo02;

import java.util.Scanner;

public class Demo07Count {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input a string");
        String input = sc.next();

        int countLower = 0;
        int countUpper = 0;
        int countNumber = 0;
        int countOther = 0;

        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if ('A' <= ch && ch <= 'Z') {
                countUpper ++;
            } else if ('a' <= ch && ch <= 'z') {
                countLower ++;
            } else if ('0' <= ch && ch <= '9') {
                countNumber ++;
            } else {
                countOther ++;
            }
        }
        System.out.println("Count Upper: " + countUpper);
        System.out.println("Count Lower: " + countLower);
        System.out.println("Count Number: " + countNumber);
        System.out.println("Count Other: " + countOther);
    }
}
