package cn.itcast.day07.demo05;

import java.util.ArrayList;

public class ArrayListStudent {
    public static void main(String[] args) {
        ArrayList<Student> list = new ArrayList<>();

        Student one = new Student("YucHen", 21);
        Student two = new Student("HaoYu", 22);
        Student three = new Student("HaoTiAn", 22);
        Student four = new Student("Lew", 23);

        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);

        for (int i = 0; i < list.size(); i++) {
            Student temp = list.get(i);
            System.out.println("Name is " + temp.getName() + " and age is " + temp.getAge());
        }
    }
}
