package cn.itcast.day07.demo05;

import java.util.ArrayList;

public class ArrayListPrint {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("YucHen");
        list.add("ForeverJZ");
        System.out.println(list);

    }
    public static void printArrayList(ArrayList<String> l) {
        System.out.println("{");
        for (int i = 0; i < l.size(); i++) {
            String name = l.get(i);
            if (i == l.size()-1) {
                System.out.println(name + "}");
            } else {
                System.out.print(name + "@");
            }
        }
    }
}
