package cn.itcast.day09.demo12;

import java.util.ArrayList;
import java.util.Scanner;

public class Manager extends User {
    public Manager() {
    }
    public Manager(String name, int money) {
        super(name, money);
    }
    public ArrayList<Integer> send(int total, int count) {
        // Need an array to store the values
        ArrayList<Integer> list = new ArrayList<>();
        // First: see how much left
        int leftMoney = super.getMoney();
        if (total > leftMoney) {
            System.out.println("Not enough deposit");
            return list;
        }

        System.out.println("Please input the amount received by each member: ");
        Scanner sc = new Scanner(System.in);
        int sum = 0;
        ArrayList<Integer> amounts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int temp = sc.nextInt();
            amounts.add(temp);
            sum += temp;
        }
        if (sum != total) {
            System.out.println("Numbers do not match");
            return list;
        }

        super.setMoney(leftMoney-total);

        return amounts;
    }
}
