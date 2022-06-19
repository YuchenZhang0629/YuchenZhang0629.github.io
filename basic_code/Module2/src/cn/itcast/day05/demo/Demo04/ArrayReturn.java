package cn.itcast.day05.demo.Demo04;

public class ArrayReturn {
    public static void main(String[] args) {
        //
    }

    public static int[] avg(int a, int b, int c) {
        int sum = a+b+c;
        int avg = sum/3;
        int[] array = {sum,avg};
        return array;
    }
}
