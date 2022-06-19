package cn.itcast.day09.demo04;

public class Demo01 {
    public static void main(String[] args) {
        Zi zi = new Zi();
        zi.methodZi();
        zi.methodFu();

        // Prioritize Zi class. Look upwards, never look downwards.
        zi.method();
    }
}
