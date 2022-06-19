package cn.itcast.day07.demo04;

import java.util.ArrayList;

public class DemoArrayListMethods {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();

        boolean success = list.add("YakHen");
        System.out.println(list);
        System.out.println("Successfully added? " + success);

        list.add("Hao");
        list.add("HaoYu");
        list.add("Lew");

        String name2 = list.get(2);
        System.out.println("Name at index 2 is " + name2);

        String rm = list.remove(1);
        System.out.println("The deleted person is " + rm);
        System.out.println(list);

    }
}
