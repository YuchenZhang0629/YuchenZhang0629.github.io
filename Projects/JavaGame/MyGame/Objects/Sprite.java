package edu.uchicago.gerber._08final.MyGame.Objects;

import javax.swing.*;
import java.awt.*;

public class Sprite {
    protected int x,y;
    protected boolean visible;
    protected int height,width;
    protected Image image;

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
        visible = true;
    }

    protected void loadImage(String imageAddr) {
        ImageIcon ii = new ImageIcon(imageAddr);
        image = ii.getImage();
    }

    protected void getImageDimensions() {
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public Image getImage() {return image;}
    public int getX() {return x;}
    public int getY() {return y;}
    public boolean isVisible() {return visible;}
    public void setVisible(Boolean visible) {this.visible = visible;}
    public Rectangle getBounds() { return new Rectangle(x, y, width, height);}
}
