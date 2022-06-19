package cn.itcast.day07.demo05;

import java.util.ArrayList;
import java.util.Random;

public class ArrayListRand {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            int temp = 1 + rand.nextInt(33);
            list.add(temp);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
