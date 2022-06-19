package cn.itcast.day06.demo01;

public class DemoStudent {
    public static void main(String[] args) {
        Student stu = new Student();
        System.out.println(stu.age);
        stu.name = "Yuchen Zhang";
        stu.age = 22;
        System.out.println(stu.name);
        stu.eat();
    }
}
