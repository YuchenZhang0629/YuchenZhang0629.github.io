package cn.itcast.day07.demo02;

import java.util.Scanner;

public class Anonymous2 {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int num = sc.nextInt();
//        System.out.println("The first input is " + num);

//        int num2 = new Scanner(System.in).nextInt();
//        System.out.println("The second input is " + num2);

//        methodParam(new Scanner(System.in));
        Scanner sc = methodReturn();
        int num = sc.nextInt();
        System.out.println("The input is " + num);
    }

    public static void methodParam(Scanner sc) {
        int i = sc.nextInt();
        System.out.println("The input is " + i);
    }
    public static Scanner methodReturn() {
//        Scanner sc = new Scanner(System.in);
//        return sc;
        return new Scanner(System.in);
    }
}
