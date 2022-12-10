package edu.uchicago.gerber._08final.MyGame.Objects;

import java.util.Random;

public class IceBomb extends Sprite {
    Random random = new Random();
    private final int xSpeed = 20;
    private int ySpeed = random.nextInt(5)-42;
    public IceBomb(int x, int y) {
        super(x, y);
        initIceBomb();
    }
    public void initIceBomb() {
        loadImage(System.getProperty("user.dir") + "/src/main/resources/images/bomb/IceBomb.png");
        getImageDimensions();
    }
    public void move() {
        ySpeed += 3;
        y += ySpeed;
        x += xSpeed;
        if (y < 0 || y > 450 || x > 800) { visible = false;}
    }
}
