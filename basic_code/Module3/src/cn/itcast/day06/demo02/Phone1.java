package cn.itcast.day06.demo02;

public class Phone1 {
    public static void main(String[] args) {
        Phone phone1 = new Phone();
        phone1.brand = "Apple";
        phone1.price = 9299.0;
        phone1.color = "Matt Grey";

        phone1.call("Hao");
        phone1.send();
    }
}
