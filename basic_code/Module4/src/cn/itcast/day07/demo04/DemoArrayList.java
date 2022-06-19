package cn.itcast.day07.demo04;

import java.util.ArrayList;

// length of arraylist could change
public class DemoArrayList {
    public static void main(String[] args) {
        // ArrayList containing strings
        ArrayList<String> list = new ArrayList<>();
        System.out.println(list);

        // The add method
        list.add("YakHen");
        System.out.println(list);
    }
}
