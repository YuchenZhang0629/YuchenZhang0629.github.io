package com.ucla.module5.demo03;

public class Person {
    static {
        System.out.println("Static code executed");
    }
    public Person() {
        System.out.println("Constructor executed");
    }
}
