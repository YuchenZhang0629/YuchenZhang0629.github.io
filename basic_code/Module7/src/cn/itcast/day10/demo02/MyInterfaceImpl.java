package cn.itcast.day10.demo02;

public class MyInterfaceImpl implements MyInterfaceA, MyInterfaceB {
    @Override
    public void methodA() {
        System.out.println("Overrode method A");
    }

    @Override
    public void methodAbs() {
        System.out.println("Overrode shared abstract methodAbs");
    }

    @Override
    public void methodB() {
        System.out.println("Overrode method B");
    }

    @Override
    public void methodDefault() {
        System.out.println("Overrode conflicting default methods in multiple interfaces");
    }
}
