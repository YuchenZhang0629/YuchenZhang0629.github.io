package cn.itcast.day04.demo02;

public class Demo03Param {
    public static void main(String[] args) {
        method2();
    }
    public static void multiply(int a, int b) {
        int result = a*b;
        System.out.println(result);
    }
    public static void method2() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Hello!");
        }
    }
}
