package edu.uchicago.gerber._08final.MyGame.Objects;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Creeper extends Sprite {
    private int health = 3;
    private int speed;
    private final int originalSpeed;
    private long hitByIceBomb = -1;
    private final HashMap<Integer, Image> imageHashMap = new HashMap<>();

    public Creeper(int x, int y, int speed) {
        super(x, y);
        initCreeper();
        this.speed = speed;
        this.originalSpeed = speed;
    }
    private void initCreeper() {
        loadImage(System.getProperty("user.dir") + "/src/main/resources/images/creeper/Creeper1.png");
        getImageDimensions();
        String base = System.getProperty("user.dir");
        ImageIcon frame1 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper1.png");
        ImageIcon frame2 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper2.png");
        ImageIcon frame3 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper3.png");
        ImageIcon frame4 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper4.png");
        ImageIcon frame5 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper5.png");
        ImageIcon frame6 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper6.png");
        ImageIcon frame7 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper7.png");
        ImageIcon frame8 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper8.png");
        ImageIcon frame9 = new ImageIcon(base + "/src/main/resources/images/creeper/Creeper9.png");

        imageHashMap.put(0,frame1.getImage());
        imageHashMap.put(1,frame2.getImage());
        imageHashMap.put(2,frame3.getImage());
        imageHashMap.put(3,frame4.getImage());
        imageHashMap.put(4,frame5.getImage());
        imageHashMap.put(5,frame6.getImage());
        imageHashMap.put(6,frame7.getImage());
        imageHashMap.put(7,frame8.getImage());
        imageHashMap.put(8,frame9.getImage());
    }
    public void move() {
        x -= speed;
        if (x < -20) {
            visible = false;
        }
    }

    public int getHealth() {
        return health;
    }
    public int getSpeed() {
        return speed;
    }
    public void decreaseHealth(int damage) {
        health -= damage;
    }
    public void decreaseSpeed(int dec) { speed -= dec; speed = Math.max(speed,1);}
    public void restoreSpeed() { speed = originalSpeed;}
    public long getHitByIceBomb() {
        return hitByIceBomb;
    }
    public void setHitByIceBomb(long hitByIceBomb) {
        this.hitByIceBomb = hitByIceBomb;
    }
    public HashMap<Integer, Image> getImageHashMap() { return imageHashMap;}
}
