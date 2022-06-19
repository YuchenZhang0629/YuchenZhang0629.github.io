package cn.itcast.day09.demo10;

public class Zi extends Fu{

    public Zi(){
        System.out.println("Executed child class constructor");
    }

    @Override
    public void eat() {
        System.out.println("Eat");
    }

}
