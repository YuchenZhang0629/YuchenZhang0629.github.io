package com.ucla.module5.demo03;

public class Demo02StaticMethod {
    public static void main(String[] args) {
        // Only then can we use things without "static" keyword.
        MyClass obj = new MyClass();
        MyClass.methodStatic();

        myPrint();
    }

    public static void myPrint() {
        System.out.println("Our Own Method");
    }
}
