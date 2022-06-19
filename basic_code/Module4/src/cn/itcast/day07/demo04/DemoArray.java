package cn.itcast.day07.demo04;

// Use an array to store three Person objects
public class DemoArray {
    public static void main(String[] args) {
        Person[] array = new Person[3];

        Person one = new Person("YuChen", 21);
        Person two = new Person("Hao", 22);
        Person three = new Person("Lew", 23);

        array[0] = one;
        array[1] = two;
        array[2] = three;

        System.out.println(array[0]);  // location value
        System.out.println(array[1]);
        System.out.println(array[2]);

        System.out.println(array[1].getAge());  // 22

    }
}
