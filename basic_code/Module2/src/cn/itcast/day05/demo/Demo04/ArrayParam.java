package cn.itcast.day05.demo.Demo04;

public class ArrayParam {
    public static void main(String[] args) {
        int[] array = {1,4,2,7,5,6,3};
        printArray(array);
        System.out.println();
        printArray(array);
        System.out.println();
        printArray(array);
    }
    public static void printArray(int[] array) {
        for (int i = 0; i <  array.length; i++) {
            System.out.print(array[i]);
        }
    }
}
