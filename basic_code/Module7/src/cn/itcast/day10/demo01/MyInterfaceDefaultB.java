package cn.itcast.day10.demo01;

public class MyInterfaceDefaultB implements MyInterfaceDefault {
    @Override
    public void abs() {
        System.out.println("Achieved abstract method B");
    }
    @Override
    public void methodDefault() {
        System.out.println("Class B overrode default interface method");
    }
}
