package cn.itcast.day10.demo02;

public class Zi extends Fu implements MyInterface {
    public static void main(String[] args) {
        Zi zi = new Zi();
        // Parent class always comes prior to interface
        zi.method();
    }
}
