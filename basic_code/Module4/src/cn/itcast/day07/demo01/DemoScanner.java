package cn.itcast.day07.demo01;

import java.util.Scanner;

public class DemoScanner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        String str = sc.next();
        System.out.println("The input number is " + num);
        System.out.println("The input string is " + str);
    }
}
