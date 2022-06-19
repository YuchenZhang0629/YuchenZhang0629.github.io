package cn.itcast.day09.demo07;

public class Child extends Parent {
    public Child() {
        // equivalent to having super();
        super(20);
        System.out.println("Child class constructor");
    }
}
