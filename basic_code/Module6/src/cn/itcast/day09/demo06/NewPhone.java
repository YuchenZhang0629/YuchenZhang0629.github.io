package cn.itcast.day09.demo06;

public class NewPhone extends Phone {
    @Override
    public void show() {
        super.show(); // reuse the parent class method
        System.out.println("Displaying avatar");
        System.out.println("Displaying contact name");
    }
}
