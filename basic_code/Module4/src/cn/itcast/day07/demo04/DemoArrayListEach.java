package cn.itcast.day07.demo04;

import java.util.ArrayList;

public class DemoArrayListEach {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        for (int i=0; i<list.size();i++) {
            System.out.println(list.get(i));
        }

        System.out.println(list.size());
    }
}
