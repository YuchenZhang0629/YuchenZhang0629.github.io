package edu.uchicago.gerber._08final.MyGame.Objects;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Explosion extends Sprite{
    public static HashMap<Integer, Image> bombMap, iceBombMap;
    private int frame, currX, currY;
    private long startTime;
    public Explosion(int x, int y) {
        super(x,y);
        bombMap = new HashMap<>();
        iceBombMap = new HashMap<>();
        frame = 0;
        loadImagesBomb();
        loadImagesIceBomb();
        startTime = System.currentTimeMillis();
        this.currX = x;
        this.currY = y;
    }
    public void loadImagesBomb() {

        String base = System.getProperty("user.dir");
        ImageIcon frame1 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/1.png");
        ImageIcon frame2 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/2.png");
        ImageIcon frame3 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/3.png");
        ImageIcon frame4 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/4.png");
        ImageIcon frame5 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/5.png");
        ImageIcon frame6 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/6.png");
        ImageIcon frame7 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/7.png");
        ImageIcon frame8 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/8.png");
        ImageIcon frame9 = new ImageIcon(base + "/src/main/resources/images/explosion/Bomb/9.png");

        bombMap.put(0,frame1.getImage());
        bombMap.put(1,frame2.getImage());
        bombMap.put(2,frame3.getImage());
        bombMap.put(3,frame4.getImage());
        bombMap.put(4,frame5.getImage());
        bombMap.put(5,frame6.getImage());
        bombMap.put(6,frame7.getImage());
        bombMap.put(7,frame8.getImage());
        bombMap.put(8,frame9.getImage());
    }
    public void loadImagesIceBomb() {

        String base = System.getProperty("user.dir");
        ImageIcon frame1 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/1.png");
        ImageIcon frame2 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/2.png");
        ImageIcon frame3 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/3.png");
        ImageIcon frame4 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/4.png");
        ImageIcon frame5 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/5.png");
        ImageIcon frame6 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/6.png");
        ImageIcon frame7 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/7.png");
        ImageIcon frame8 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/8.png");
        ImageIcon frame9 = new ImageIcon(base + "/src/main/resources/images/explosion/IceBomb/9.png");

        iceBombMap.put(0,frame1.getImage());
        iceBombMap.put(1,frame2.getImage());
        iceBombMap.put(2,frame3.getImage());
        iceBombMap.put(3,frame4.getImage());
        iceBombMap.put(4,frame5.getImage());
        iceBombMap.put(5,frame6.getImage());
        iceBombMap.put(6,frame7.getImage());
        iceBombMap.put(7,frame8.getImage());
        iceBombMap.put(8,frame9.getImage());
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getCurrX() {
        return currX;
    }

    public void setCurrX(int currX) {
        this.currX = currX;
    }

    public int getCurrY() {
        return currY;
    }

    public void setCurrY(int currY) {
        this.currY = currY;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public HashMap<Integer, Image> getBombMap() {
        return bombMap;
    }

    public void setBombMap(HashMap<Integer, Image> bombMap) {
        this.bombMap = bombMap;
    }

    public HashMap<Integer, Image> getIceBombMap() {
        return iceBombMap;
    }

    public void setIceBombMap(HashMap<Integer, Image> iceBombMap) {
        this.iceBombMap = iceBombMap;
    }
}
