package cn.itcast.day09.demo11;

// Hence, the child class is also abstract
public abstract class Dog extends Animal {
    @Override
    public void sleep() {
        System.out.println("The dog is sleeping");
    }

}
