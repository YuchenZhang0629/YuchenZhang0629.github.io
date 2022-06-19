package com.ucla.module5.demo03;

public class Demo01StaticField {
    public static void main(String[] args) {
        Student one = new Student("YucHen", 21);
        one.room = "101";
        Student two = new Student("HaO", 22);
        two.room = "102";

        System.out.println(one.room);
    }
}
