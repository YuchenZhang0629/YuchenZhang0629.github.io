package cn.itcast.day09.demo02;

public class Zi extends Fu {
    int numZi = 20;
    int num = 200;

    public void methodZi() {
        // num from Zi class
        // find upwards only if there's none in the local class
        System.out.println(num);
    }
}
