package cn.itcast.day04.demo04;

public class OverloadPrint {
    public static void main(String[] args) {
        // println is itself an overload.
        myPrint(100);
        myPrint("100");
    }

    public static void myPrint(byte num) {
        System.out.println(num);
    }
    public static void myPrint(short num) {
        System.out.println(num);
    }
    public static void myPrint(int num) {
        System.out.println(num);
    }
    public static void myPrint(long num) {
        System.out.println(num);
    }
    public static void myPrint(float num) {
        System.out.println(num);
    }
    public static void myPrint(double num) {
        System.out.println(num);
    }
    public static void myPrint(char num) {
        System.out.println(num);
    }
    public static void myPrint(boolean num) {
        System.out.println(num);
    }
    public static void myPrint(String str) {
        System.out.println(str);
    }
}
