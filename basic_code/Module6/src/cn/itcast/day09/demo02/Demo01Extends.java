package cn.itcast.day09.demo02;

public class Demo01Extends {
    public static void main(String[] args) {
        Fu fu = new Fu();
        System.out.println(fu.numFu);

        Zi zi = new Zi();
        System.out.println(zi.numFu);
        System.out.println(zi.numZi);

        System.out.println(zi.num); //200
        zi.methodZi();
        zi.methodFu();
    }
}
