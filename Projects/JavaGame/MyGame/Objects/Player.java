package edu.uchicago.gerber._08final.MyGame.Objects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Sprite {
    private int totalScore;
    private Image player;
    private int health = 5, bulletCount = 0, bombCount = 0;
    private double lastBullet, lastBomb;
    private int dx, dy, width,height;
    private int x = 110;
    private int y = 296;
    // construct an arraylist of bullets
    private List<Bullet> bullets;
    private List<Bomb> bombs;
    private List<IceBomb> iceBombs;
    private String deathMessage;
    private final Map<Integer, Image> imageMap = new HashMap<>();
    public Player(int x, int y) {
        super(x,y);
        loadPlayer();
        totalScore = 0;
    }
    private void loadPlayer() {
        loadImage(System.getProperty("user.dir") + "/src/main/resources/images/player/PlayerFrame1.png");
        getImageDimensions();
        lastBullet = -1;
        lastBomb = -1;
        bullets = new ArrayList<>();
        bombs = new ArrayList<>();
        iceBombs = new ArrayList<>();
        String base = System.getProperty("user.dir");
        ImageIcon frame1 = new ImageIcon(base + "/src/main/resources/images/player/PlayerFrame1.png");
        ImageIcon frame2 = new ImageIcon(base + "/src/main/resources/images/player/PlayerFrame2.png");
        ImageIcon frame3 = new ImageIcon(base + "/src/main/resources/images/player/PlayerFrame3.png");
        ImageIcon frame4 = new ImageIcon(base + "/src/main/resources/images/player/PlayerFrame4.png");
        ImageIcon frame5 = new ImageIcon(base + "/src/main/resources/images/player/PlayerFrame5.png");
        ImageIcon frame6 = new ImageIcon(base + "/src/main/resources/images/player/PlayerFrame6.png");
        ImageIcon frame7 = new ImageIcon(base + "/src/main/resources/images/player/PlayerFrame7.png");

        imageMap.put(0,frame1.getImage());
        imageMap.put(1,frame2.getImage());
        imageMap.put(2,frame3.getImage());
        imageMap.put(3,frame4.getImage());
        imageMap.put(4,frame5.getImage());
        imageMap.put(5,frame6.getImage());
        imageMap.put(6,frame7.getImage());

    }

    public List<Bullet> getBullets() {
        return bullets;
    }
    public List<Bomb> getBombs() {
        return bombs;
    }
    public List<IceBomb> getIceBombs() {return iceBombs; }

    public void move() {
        x += dx;
        y += dy;
        if (x <= 10) { dx = 0;}
        if (x > 250) { dx = 0;}
    }
    // Set of getter functions
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Image getImage() {
        return player;
    }
    // Move the player
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            if (x > 16) {
                dx = -6;
            } else {
                dx = 0;
            }
        }
        if (key == KeyEvent.VK_D) {
            if (x < 244) {
                dx = 6;
            } else {
                dx = 0;
            }
        }
        if (key == KeyEvent.VK_S) { dx = 0;}
        if (key == KeyEvent.VK_SPACE) {
            if (lastBullet == -1) {
                lastBullet = System.currentTimeMillis();
                fire();
                bulletCount += 1;
                totalScore -= 2;
            } else if (System.currentTimeMillis() - lastBullet > 300) {
                fire();
                lastBullet = System.currentTimeMillis();
                bulletCount += 1;
                totalScore -= 2;
            }
        }

        if (key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_ENTER) {
            if (lastBomb == -1) {
                lastBomb = System.currentTimeMillis();
                if (key == KeyEvent.VK_ENTER) {
                    tossBomb();
                } else {
                    tossIceBomb();
                }
                totalScore -= 7;
                bombCount += 1;
            } else if (System.currentTimeMillis() - lastBomb > 1000) {
                if (key == KeyEvent.VK_ENTER) {
                    tossBomb();
                } else {
                    tossIceBomb();
                }
                totalScore -= 5;
                bombCount += 1;
                lastBomb = System.currentTimeMillis();
            }
        }
    }
    public void fire() {
        bullets.add(new Bullet(x+width/2,y+height/2));
    }
    public void tossBomb() {
        bombs.add(new Bomb(x+width/2,y+height/2));
    }
    public void tossIceBomb() {
        iceBombs.add(new IceBomb(x+width/2,y+height/2));
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) { dx = 0;}
        if (key == KeyEvent.VK_RIGHT) { dx = 0;}
    }
    public double getLastBullet() {
        return lastBullet;
    }
    public double getLastBomb() {
        return lastBomb;
    }
    public int getHealth(){
        return health;
    }
    public void decreaseHealth() {
        health -= 1;
    }
    public String getDeathMessage() {
        return deathMessage;
    }
    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }
    public int getBulletCount() { return bulletCount;}
    public int getBombCount() { return bombCount;}
    public int getTotalScore() { return Math.max(totalScore,0);}
    public void changeScore(int change) {
        this.totalScore += change;
        this.totalScore = Math.max(this.totalScore, 0);
    }
    public boolean isMoving() { return dx != 0;}
    public Map<Integer, Image> getImageMap() { return imageMap;}
}
