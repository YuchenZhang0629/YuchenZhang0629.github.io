package cn.itcast.day07.demo03;

import java.util.Random;
import java.util.Scanner;

public class DemoRandom3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        Random rand = new Random();

        for (int i = 0; i < 30; i++) {
            int num = 1 + rand.nextInt(n);
            System.out.println(num);
        }
    }
}