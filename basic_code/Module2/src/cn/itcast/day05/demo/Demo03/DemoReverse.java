package cn.itcast.day05.demo.Demo03;

public class DemoReverse {
    public static void main(String[] args) {
        int[] array = {1,4,2,8,5,7,6,0,9,3};
        int max = array.length-1;
        for (int i = 0; i < (array.length+1)/2; i++) {
            int temp = array[i];
            array[i] = array[max-i];
            array[max-i] = temp;
        }
        for (int i = 0; i<array.length-1; i++){
            System.out.print(array[i]);
        }
    }
}
