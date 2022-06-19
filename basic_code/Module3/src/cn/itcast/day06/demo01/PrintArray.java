package cn.itcast.day06.demo01;
import java.util.Arrays;

public class PrintArray {
    public static void main(String[] args) {
        int[] array = {1,2,3,4,5};
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            if (i == array.length-1) {
                System.out.print(array[i]+"]");
            } else {
                System.out.print(array[i]+", ");
            }
        }
        System.out.println("==============");


        System.out.println(Arrays.toString(array));
    }
}
