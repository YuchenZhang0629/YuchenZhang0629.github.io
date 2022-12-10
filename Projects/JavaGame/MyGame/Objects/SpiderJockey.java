package edu.uchicago.gerber._08final.MyGame.Objects;

public class SpiderJockey extends Sprite {
    private int health = 5;
    private int verticalSpeed, horizontalSpeed;
    private long hitByIceBomb = -1;
    private boolean isDescending = true;
    private final int originalSpeed;
    public SpiderJockey(int x, int y, int descendSpeed, int walkingSpeed) {
        super(x,y);
        initJockey();
        this.verticalSpeed = descendSpeed;
        this.horizontalSpeed = walkingSpeed;
        this.originalSpeed = walkingSpeed;
    }
    private void initJockey() {
        loadImage(System.getProperty("user.dir") + "/src/main/resources/images/spiderjockey/SpiderJockey.png");
        getImageDimensions();
    }
    public void move() {
        if (y < 300 ) {
            y += verticalSpeed;
        } else {
            y = 300;
            isDescending = false;
            x -= horizontalSpeed;
            if (x < -40) {
                setVisible(false);
            }
        }
    }
    public int getHealth() {
        return health;
    }
    public void decreaseHealth(int damage) {
        health -= damage;
    }
    public void decreaseSpeed(int dec) {
        horizontalSpeed -= dec;
        horizontalSpeed = Math.max(0,horizontalSpeed);
    }
    public void restoreSpeed() { horizontalSpeed = originalSpeed;}
    public boolean isDescending() { return isDescending;}
    public long getHitByIceBomb() {
        return hitByIceBomb;
    }
    public void setHitByIceBomb(long hitByIceBomb) {
        this.hitByIceBomb = hitByIceBomb;
    }

}
