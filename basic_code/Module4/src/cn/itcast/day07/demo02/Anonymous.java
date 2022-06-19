package cn.itcast.day07.demo02;

public class Anonymous {
    public static void main(String[] args) {
        Person one = new Person();
        one.name = "YuChen Zhang";
        one.showName();
        System.out.println("====================");

        // Anonymous: If we're certain that we only use it once
        new Person().name = "Hao";
        new Person().showName();
    }
}
