package cn.itcast.day05.demo.Demo03;

public class Demo04Array {
    public static void main(String[] args) {
        int[] array = {1,2,3,4,5};
        int max = array[0];
        for (int i = 1; i < array.length; i++){
            if (array[i] > max) {
                max = array[i];
            }
        }
        System.out.println(max);
    }
}
