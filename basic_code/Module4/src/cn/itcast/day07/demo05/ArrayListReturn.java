package cn.itcast.day07.demo05;

import java.util.ArrayList;
import java.util.Random;

public class ArrayListReturn {
    public static void main(String[] args) {
        ArrayList<Integer> List = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 20; i++) {
            int temp = r.nextInt(100) + 1;
            List.add(temp);
        }
        ArrayList<Integer> small = getSmaller(List);
        for (int i = 0; i < small.size(); i++) {
            System.out.println(small.get(i));
        }
    }
    public static ArrayList<Integer> getSmaller(ArrayList<Integer> l) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            int temp = l.get(i);
            if (temp % 2 == 0) {
                list.add(temp);
            }
        }
        return list;
    }
}
