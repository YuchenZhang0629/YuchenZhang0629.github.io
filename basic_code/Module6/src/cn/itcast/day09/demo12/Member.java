package cn.itcast.day09.demo12;

import java.util.ArrayList;
import java.util.Random;

public class Member extends User {
    public Member() {
    }
    public Member(String name, int money) {
        super(name, money);
    }
    public void receive(ArrayList<Integer> list) {

        int delta = list.remove(0);
        int money = super.getMoney();
        super.setMoney(money+delta);
    }
}
