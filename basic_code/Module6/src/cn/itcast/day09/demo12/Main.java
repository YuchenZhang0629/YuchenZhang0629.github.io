package cn.itcast.day09.demo12;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager("YucHen", 100);
        Member one = new Member("A", 0);
        Member two = new Member("B", 0);
        Member three = new Member("C", 0);

        manager.show();
        one.show();
        two.show();
        three.show();
        System.out.println("......................");
        System.out.println("");

        Scanner sc = new Scanner(System.in);
        System.out.println("Please input the total amount: ");
        int total = sc.nextInt();

        ArrayList<Integer> redList = manager.send(total,3);
        one.receive(redList);
        two.receive(redList);
        three.receive(redList);

        manager.show();
        one.show();
        two.show();
        three.show();
    }
}
