package cn.itcast.day10.demo01;

public interface MyInterfacePrivateB {
    public static void methodStatic1() {
        System.out.println("This is static method 1");
        methodStaticCommon();
    }
    public static void methodStatic2() {
        System.out.println("This is static method2");
        methodStaticCommon();
    }

    private static void methodStaticCommon() {
        System.out.println("BBB");
        System.out.println("BBB");
        System.out.println("BBB");
    }
}
