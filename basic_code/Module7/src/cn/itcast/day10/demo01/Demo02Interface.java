package cn.itcast.day10.demo01;

public class Demo02Interface {
    public static void main(String[] args) {
        MyInterfaceDefaultA A = new MyInterfaceDefaultA();
        A.abs();
        A.methodDefault();
        System.out.println("......................");
        MyInterfaceDefaultB B = new MyInterfaceDefaultB();
        B.methodDefault();
        B.abs();
    }
}
