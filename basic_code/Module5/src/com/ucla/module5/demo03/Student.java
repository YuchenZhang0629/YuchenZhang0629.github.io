package com.ucla.module5.demo03;

public class Student {

    private String name;
    private int id;
    private int age;
    static String room;
    private static int counter = 0;
    public Student() {
        this.id = ++counter;
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        this.id = ++counter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
