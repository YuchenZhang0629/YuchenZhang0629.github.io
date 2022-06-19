package cn.itcast.day07.demo03;

import java.util.Random;
import java.util.Scanner;

public class DemoRandom4 {
    public static void main(String[] args) {
        Random rand = new Random();
        int num = 1 + rand.nextInt(100);

        while (true) {
            Scanner sc = new Scanner(System.in);
            int guess_temp = sc.nextInt();
            if (guess_temp < num) {
                System.out.println("Too Small");
            } else if (guess_temp > num) {
                System.out.println("Too large");
            } else {
                System.out.println("Congrats! You guessed it");
                break;
            }
        }
        System.out.println("Game Over.");
    }
}
