package cn.itcast.day10.demo01;

public interface MyInterfacePrivateA {
    public default void methodDefault1() {
        System.out.println("This is method 1");
        methodCommon();
    }
    public default void methodDefault2() {
        System.out.println("This is method2");
        methodCommon();
    }

    private void methodCommon() {
        System.out.println("BBB");
        System.out.println("BBB");
        System.out.println("BBB");
    }
}
