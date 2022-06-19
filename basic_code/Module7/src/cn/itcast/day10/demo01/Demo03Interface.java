package cn.itcast.day10.demo01;

public class Demo03Interface {
    public static void main(String[] args) {
        MyInterfaceStaticImpl impl = new MyInterfaceStaticImpl();
        // Access static method directly from static methods
        MyInterfaceStatic.methodStatic();
    }
}
