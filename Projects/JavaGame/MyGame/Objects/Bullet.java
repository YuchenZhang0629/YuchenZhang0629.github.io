package edu.uchicago.gerber._08final.MyGame.Objects;

public class Bullet extends Sprite {
    public Bullet(int x, int y) {
        super(x+10, y+30);
        initBullet();
    }

    private void initBullet() {
        loadImage(System.getProperty("user.dir") + "/src/main/resources/images/bullet/Bullet_large.png");
        getImageDimensions();
    }

    public void move() {
        x += 15;
        if (x > 800) { visible = false;}
    }
}
