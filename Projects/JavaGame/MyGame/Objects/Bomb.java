package edu.uchicago.gerber._08final.MyGame.Objects;

import java.util.Random;

public class Bomb extends Sprite {
    Random random = new Random();
    private final int boardWidth = 800;
    private final float initialTime = System.currentTimeMillis();
    private final int xSpeed = 20;
    private int ySpeed = random.nextInt(5)-42;

    public Bomb(int x, int y) {
        super(x, y);
        initBomb();
    }

    private void initBomb() {
        loadImage(System.getProperty("user.dir") + "/src/main/resources/images/bomb/Bomb.png");
        getImageDimensions();
    }

    public void move() {
        ySpeed += 3;
        y += ySpeed;
        x += xSpeed;
        if (y < 0 || y > 450 || x > 800) { visible = false;}
    }
}
