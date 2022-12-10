package edu.uchicago.gerber._08final.MyGame.Objects;

import javax.swing.*;

public class Target extends Sprite {
    private int health = 10;
    public Target(int x, int y, int i) {
        super(x, y);
        initTarget(i);
    }
    private void initTarget(int i) {
        this.health = 10;
        if (i == 2) {
            loadImage(System.getProperty("user.dir") + "/src/main/resources/images/target/Target.png");
        } else if (i == 4) {
            loadImage(System.getProperty("user.dir") + "/src/main/resources/images/target/Target2.png");
        }
        getImageDimensions();
    }
    public int getHealth() {
        return health;
    }
    public void decreaseHealth(int damage) {
        health -= damage;
    }

}
