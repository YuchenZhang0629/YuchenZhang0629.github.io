package cn.itcast.day10.demo01;

public interface MyInterfaceDefault {
    public abstract void abs();
    public default void methodDefault(){
        System.out.println("Newly added default method");
    }
}
