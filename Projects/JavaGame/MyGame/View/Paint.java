package edu.uchicago.gerber._08final.MyGame.View;


import edu.uchicago.gerber._08final.MyGame.Objects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Paint extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private final ArrayList<Creeper> creepersLV1 = new ArrayList<>();
    private final ArrayList<SpiderJockey> spiderJockeysLV2 = new ArrayList<>();
    private final ArrayList<Creeper> creepersLV3 = new ArrayList<>();
    private final ArrayList<SpiderJockey> spiderJockeysLV3 = new ArrayList<>();
    private final ArrayList<Explosion> bombExplosions = new ArrayList<>(), iceBombExplosions = new ArrayList<>();
    private final Target targetStage2 = new Target(590,302,2);
    private final Target targetStage4 = new Target(590,302,4);
    public final Random random = new Random();
    private int game = 0;
    private long secondLastKill = -1, lastKill = -1, lastDoubleKill = -1, stage0Start, stage3Start, stage5Start;
    private int doubleKillCount = 0;
    public Paint() { initBoard();}
    private void initBoard() {
        addKeyListener(new TAdapter());
        //setBackground(Color.black);
        setFocusable(true);
        player = new Player(110,296);

        initCreepersLV1();
        initSpiderJockeysLV2();
        initCreepersLV3();
        initSpiderJockeysLV3();

        int DELAY = 40;
        timer = new Timer(DELAY, this);
        timer.start();
        stage0Start = System.currentTimeMillis();
    }
    public void initCreepersLV1() {
        int[] xCords = random.ints(12, 800, 2200).toArray();
        int[] speeds = random.ints(12, 4, 7).toArray();
        if (xCords != null && speeds != null){
            for (int i = 0; i < xCords.length; i++) {
                creepersLV1.add(new Creeper(xCords[i], 300, speeds[i]));
            }
        }
    }
    public void initSpiderJockeysLV2() {
        int[] xCords = random.ints(15, 425, 725).toArray();
        int[] yCords = random.ints(15, -2000, -50).toArray();
        int[] verticalSpeeds = random.ints(17, 4, 7).toArray();
        int[] horizontalSpeeds = random.ints(17, 3, 6).toArray();
        if (xCords != null && verticalSpeeds != null && horizontalSpeeds != null){
            for (int i = 0; i < xCords.length; i++) {
                spiderJockeysLV2.add(new SpiderJockey(xCords[i], yCords[i], verticalSpeeds[i],horizontalSpeeds[i]));
            }
        }
    }
    public void initCreepersLV3() {
        int[] xCords = random.ints(20, 800, 6000).toArray();
        int[] speeds = random.ints(20, 4, 7).toArray();
        if (xCords != null && speeds != null){
            for (int i = 0; i < xCords.length; i++) {
                creepersLV3.add(new Creeper(xCords[i], 300, speeds[i]));
            }
        }
    }
    public void initSpiderJockeysLV3() {
        int[] xCords = random.ints(20, 425, 725).toArray();
        int[] yCords = random.ints(20, -5000, -50).toArray();
        int[] verticalSpeeds = random.ints(20, 4, 7).toArray();
        int[] horizontalSpeeds = random.ints(20, 3, 6).toArray();
        if (xCords != null && verticalSpeeds != null && horizontalSpeeds != null){
            for (int i = 0; i < xCords.length; i++) {
                spiderJockeysLV3.add(new SpiderJockey(xCords[i], yCords[i], verticalSpeeds[i],horizontalSpeeds[i]));
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game == 0) {
            drawObjectsStage0(g);
        }
        if (game == 1) {
            drawObjectsStage1(g);
        }
        if (game == 2) {
            drawObjectsStage2(g);
        }
        if (game == 3) {
            drawObjectsStage3(g);
        }
        if (game == 4) {
            drawObjectsStage4(g);
        }
        if (game == 5) {
            drawObjectsStage5(g);
        }
        if (game == 6) {
            drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }
    private void drawObjectsStage0(Graphics g) {
        String msg1 = "GET READY!";
        String msg2 = "The Game Will Start In " + (5 - (System.currentTimeMillis() - stage0Start) / 1000) + " Seconds";
        g.setColor(Color.BLACK);
        Font small1 = new Font("Times New Roman", Font.BOLD, 30);
        Font small2 = new Font("Times New Roman", Font.PLAIN,20);
        FontMetrics fm1 = getFontMetrics(small1);
        FontMetrics fm2 = getFontMetrics(small2);
        g.setColor(Color.BLACK);
        g.setFont(small1);
        g.drawString(msg1, (800 - fm1.stringWidth(msg1)) / 2, 450 / 2 - 10);
        g.setFont(small2);
        g.drawString(msg2, (800 - fm2.stringWidth(msg2)) / 2, 450 / 2 + 20);
        if (System.currentTimeMillis() - stage0Start > 5000) {
            game = 1;
        }
    }
    private void drawObjectsStage1(Graphics g) {
        paintBackground(g,1);
        paintLevel(g,1);
        Font regular = new Font("Times New Roman", Font.BOLD, 15);
        g.setFont(regular);
        if (player.isVisible()) {
            if (player.isMoving()) {
                int key = getPlayerKey(System.currentTimeMillis());
                g.drawImage(player.getImageMap().get(key), player.getX(), player.getY(), this);
            } else {
                g.drawImage(player.getImageMap().get(3), player.getX(), player.getY(), this);
            }
            // Draw the player's health
            g.setColor(Color.white);
            g.fillRect(player.getX()-23,player.getY()-20,100,8);
            g.setColor(Color.red);
            g.fillRect(player.getX()-22,player.getY()-19,98,6);
            g.setColor(Color.blue);
            g.fillRect(player.getX()-22,player.getY()-19,(int) Math.round(0.2*98*player.getHealth()),6);
        }
        List<Bullet> bullets = player.getBullets();
        for (Bullet bullet : bullets) {
            if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
            }
        }
        List<Bomb> bombs = player.getBombs();
        for (Bomb bomb : bombs) {
            if (bomb.isVisible()) {
                g.drawImage(bomb.getImage(), bomb.getX(), bomb.getY(), this);
            }
        }
        List<IceBomb> iceBombs = player.getIceBombs();
        for (IceBomb iceBomb : iceBombs) {
            if (iceBomb.isVisible()) {
                g.drawImage(iceBomb.getImage(), iceBomb.getX(), iceBomb.getY(), this);
            }
        }
        for (Creeper creeper : creepersLV1) {
            if (creeper.isVisible()) {
                int key = getCreeperKey(System.currentTimeMillis(),creeper.getSpeed());
                g.drawImage(creeper.getImageHashMap().get(key), creeper.getX(), creeper.getY(), this);
                // Paint the creepers' health bar
                g.setColor(Color.white);
                g.fillRect(creeper.getX()+5,creeper.getY()-10,40,7);
                g.setColor(Color.green);
                g.fillRect(creeper.getX()+6,creeper.getY()-9,38,5);
                g.setColor(Color.red);
                if (creeper.getHealth() == 2) {
                    g.fillRect(creeper.getX()+31,creeper.getY()-9,13,5);
                } else if (creeper.getHealth() == 1) {
                    g.fillRect(creeper.getX()+18,creeper.getY()-9,26,5);
                }
            }
        }
        for (Explosion explosion: bombExplosions) {
            if (explosion.isVisible()) {
                int key = (int) ((System.currentTimeMillis() - explosion.getStartTime()) / 100);
                g.drawImage(explosion.getBombMap().get(key), explosion.getCurrX(), explosion.getCurrY(), this);
                if (System.currentTimeMillis() - explosion.getStartTime() >= 900) {
                    explosion.setVisible(false);
                }
            }
        }
        for (Explosion explosion: iceBombExplosions) {
            if (explosion.isVisible()) {
                int key = (int) ((System.currentTimeMillis() - explosion.getStartTime()) / 100);
                g.drawImage(explosion.getIceBombMap().get(key), explosion.getCurrX(), explosion.getCurrY(), this);
                if (System.currentTimeMillis() - explosion.getStartTime() >= 900) {
                    explosion.setVisible(false);
                }
            }
        }
        // Draw the remaining enemies
        g.setColor(Color.WHITE);
        g.drawString("Creepers left: " + creepersLV1.size(), 15, 25);
        g.drawString("Bullet Count: " + player.getBulletCount(), 675,45);
        // Draw the Bullet Cool Down
        g.setColor(Color.WHITE);
        g.drawString("Bullet CD: ", 15, 45);
        g.drawString("Bomb Count: " + player.getBombCount(), 675,65);
        g.setColor(Color.white);
        g.fillRect(100,35,60,10);
        g.setColor(Color.red);
        g.fillRect(101,36,58,8);
        g.setColor(Color.green);
        g.fillRect(101,36,Math.min(58,(int) Math.round(0.0033333*58*(System.currentTimeMillis()-player.getLastBullet()))),8);
        // Draw the Bomb Cool Down
        g.setColor(Color.WHITE);
        g.drawString("Bomb CD: ", 15, 65);
        g.setColor(Color.white);
        g.fillRect(100,55,60,10);
        g.setColor(Color.red);
        g.fillRect(101,56,58,8);
        g.setColor(Color.green);
        g.fillRect(101,56,Math.min(58,(int) Math.round(0.001*58*(System.currentTimeMillis()-player.getLastBomb()))),8);
        // Print the total Score
        g.setColor(null);
        g.drawString("TOTAL SCORE: " + player.getTotalScore(), 650, 25);
        // Print the text for double kill
        if (System.currentTimeMillis() - lastDoubleKill < 1000) {
            String msg1 = "DOUBLE KILL!";
            Font small1 = new Font("Papyrus", Font.BOLD, 30);
            g.setColor(new Color(196, 9, 80, 255));
            g.setFont(small1);
            g.drawString(msg1, 300, 110);
        }
    }
    private void drawObjectsStage2(Graphics g) {
        paintBackgroundBlurry(g);
        // Draw the player and his health
        if (player.isMoving()) {
            int key = getPlayerKey(System.currentTimeMillis());
            g.drawImage(player.getImageMap().get(key), player.getX(), player.getY(), this);
        } else {
            g.drawImage(player.getImageMap().get(3), player.getX(), player.getY(), this);
        }
        // Draw the player's health
        g.setColor(Color.white);
        g.fillRect(player.getX()-23,player.getY()-20,100,8);
        g.setColor(Color.red);
        g.fillRect(player.getX()-22,player.getY()-19,98,6);
        g.setColor(Color.blue);
        g.fillRect(player.getX()-22,player.getY()-19,(int) Math.round(0.2*98*player.getHealth()),6);

        String msg1 = "CONGRATULATIONS, YOU PASSED LEVEL1!";
        String msg2_1 = "Starting from level2";
        String msg2_2 = "There will be spider jockeys spawning from the ceiling";
        String msg2_3 = "Make sure to hit them as soon as possible";
        String msg2_4 = "Once they reach the floor, they are incredibly hard to kill!";
        String msg3 = "Beat the target to proceed!";
        g.setColor(Color.BLACK);
        Font small1 = new Font("Times New Roman", Font.BOLD, 30);
        Font small2 = new Font("Times New Roman", Font.BOLD,20);
        Font small3 = new Font("Times New Roman", Font.BOLD,25);
        FontMetrics fm1 = getFontMetrics(small1);
        FontMetrics fm2 = getFontMetrics(small2);
        FontMetrics fm3 = getFontMetrics(small3);
        //g.setColor(new Color(250,150,0));
        g.setColor(Color.WHITE);
        g.setFont(small1);
        g.drawString(msg1, (800 - fm1.stringWidth(msg1)) / 2, 450 / 2 - 115);
        g.setFont(small2);
        g.drawString(msg2_1, (800 - fm2.stringWidth(msg2_1)) / 2, 450 / 2 - 80);
        g.drawString(msg2_2, (800 - fm2.stringWidth(msg2_2)) / 2, 450 / 2 - 60);
        g.drawString(msg2_3, (800 - fm2.stringWidth(msg2_3)) / 2, 450 / 2 - 40);
        g.drawString(msg2_4, (800 - fm2.stringWidth(msg2_4)) / 2, 450 / 2 - 20);
        g.setFont(small3);
        g.drawString(msg3, (800 - fm3.stringWidth(msg3)) / 2, 450 / 2 + 12);

        if (targetStage2.isVisible()) {
            g.drawImage(targetStage2.getImage(), targetStage2.getX(), targetStage2.getY()-12, this);
            g.setColor(Color.white);
            g.fillRect(targetStage2.getX()-1,targetStage2.getY()-30,100,8);
            g.setColor(Color.red);
            g.fillRect(targetStage2.getX(),targetStage2.getY()-29,98,6);
            g.setColor(Color.GREEN);
            g.fillRect(targetStage2.getX(),targetStage2.getY()-29,(int) Math.round(0.1*98*targetStage2.getHealth()),6);
        } else {
            game = 3;
            setStage3Start(System.currentTimeMillis());
        }

        // Now we are attempting to beat the target
        List<Bullet> bullets = player.getBullets();
        for (Bullet bullet : bullets) {
            if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
            }
        }
    }
    private void drawObjectsStage3(Graphics g) {
        paintBackground(g,2);
        paintLevel(g, 2);
        g.setColor(Color.WHITE);
        Font regular = new Font("Times New Roman", Font.BOLD, 15);
        g.setFont(regular);
        g.drawString("Creepers left: 0", 15, 25);
        g.drawString("Spider Jockeys left: " + spiderJockeysLV2.size(), 15, 45);
        g.drawString("Bullet Count: " + player.getBulletCount(), 675,45);
        g.drawString("Bomb Count: " + player.getBombCount(), 675,65);
        // Draw the Bullet Cool Down
        g.drawString("Bullet CD: ", 15, 65);
        g.setColor(Color.white);
        g.fillRect(95,55,60,10);
        g.setColor(Color.red);
        g.fillRect(96,56,58,8);
        g.setColor(Color.green);
        g.fillRect(96,56,Math.min(58,(int) Math.round(0.0033333*58*(System.currentTimeMillis()-player.getLastBullet()))),8);
        // Draw the Bomb Cool Down
        g.setColor(Color.WHITE);
        g.drawString("Bomb CD: ", 15, 85);
        g.setColor(Color.white);
        g.fillRect(95,75,60,10);
        g.setColor(Color.red);
        g.fillRect(96,76,58,8);
        g.setColor(Color.green);
        g.fillRect(96,76,Math.min(58,(int) Math.round(0.001*58*(System.currentTimeMillis()-player.getLastBomb()))),8);
        // Print the total Score
        g.setColor(null);
        g.drawString("TOTAL SCORE: " + player.getTotalScore(), 650, 25);
        // Print the text for double kill

        if (player.isVisible()) {
            if (player.isMoving()) {
                int key = getPlayerKey(System.currentTimeMillis());
                g.drawImage(player.getImageMap().get(key), player.getX(), player.getY(), this);
            } else {
                g.drawImage(player.getImageMap().get(3), player.getX(), player.getY(), this);
            }
            // Draw the player's health
            g.setColor(Color.white);
            g.fillRect(player.getX()-23,player.getY()-20,100,8);
            g.setColor(Color.red);
            g.fillRect(player.getX()-22,player.getY()-19,98,6);
            g.setColor(Color.blue);
            g.fillRect(player.getX()-22,player.getY()-19,(int) Math.round(0.2*98*player.getHealth()),6);
        }
        // Draw the player's health

        List<Bullet> bullets = player.getBullets();
        for (Bullet bullet : bullets) {
            if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
            }
        }
        List<Bomb> bombs = player.getBombs();
        for (Bomb bomb : bombs) {
            if (bomb.isVisible()) {
                g.drawImage(bomb.getImage(), bomb.getX(), bomb.getY(), this);
            }
        }
        List<IceBomb> iceBombs = player.getIceBombs();
        for (IceBomb iceBomb : iceBombs) {
            if (iceBomb.isVisible()) {
                g.drawImage(iceBomb.getImage(), iceBomb.getX(), iceBomb.getY(), this);
            }
        }
        for (SpiderJockey spiderJockey : spiderJockeysLV2) {
            if (spiderJockey.isVisible()) {
                g.drawImage(spiderJockey.getImage(), spiderJockey.getX(), spiderJockey.getY(), this);
                // Paint the spider jockey's health bar
                g.setColor(Color.white);
                g.fillRect(spiderJockey.getX()+30,spiderJockey.getY()-10,40,7);
                g.setColor(Color.green);
                g.fillRect(spiderJockey.getX()+31,spiderJockey.getY()-9,38,5);
                g.setColor(Color.red);
                g.fillRect(spiderJockey.getX()+69-(int) ((5-spiderJockey.getHealth())*7.6),spiderJockey.getY()-9,
                        (int) ((5-spiderJockey.getHealth())*7.6),5);
            }
        }
        for (Explosion explosion: bombExplosions) {
            if (explosion.isVisible()) {
                int key = (int) ((System.currentTimeMillis() - explosion.getStartTime()) / 100);
                g.drawImage(explosion.getBombMap().get(key), explosion.getCurrX(), explosion.getCurrY(), this);
                if (System.currentTimeMillis() - explosion.getStartTime() >= 900) {
                    explosion.setVisible(false);
                }
            }
        }
        for (Explosion explosion: iceBombExplosions) {
            if (explosion.isVisible()) {
                int key = (int) ((System.currentTimeMillis() - explosion.getStartTime()) / 100);
                g.drawImage(explosion.getIceBombMap().get(key), explosion.getCurrX(), explosion.getCurrY(), this);
                if (System.currentTimeMillis() - explosion.getStartTime() >= 900) {
                    explosion.setVisible(false);
                }
            }
        }
        if (System.currentTimeMillis() - lastDoubleKill < 1000) {
            String msg1 = "DOUBLE KILL!";
            Font small1 = new Font("Papyrus", Font.BOLD, 30);
            g.setColor(new Color(196, 9, 80, 255));
            g.setFont(small1);
            g.drawString(msg1, 300, 110);
        }
    }
    private void drawObjectsStage4(Graphics g) {
        paintBackground2Blurry(g);
        // Draw the player and his health
        if (player.isMoving()) {
            int key = getPlayerKey(System.currentTimeMillis());
            g.drawImage(player.getImageMap().get(key), player.getX(), player.getY(), this);
        } else {
            g.drawImage(player.getImageMap().get(3), player.getX(), player.getY(), this);
        }
        // Draw the player's health
        g.setColor(Color.white);
        g.fillRect(player.getX()-23,player.getY()-20,100,8);
        g.setColor(Color.red);
        g.fillRect(player.getX()-22,player.getY()-19,98,6);
        g.setColor(Color.blue);
        g.fillRect(player.getX()-22,player.getY()-19,(int) Math.round(0.2*98*player.getHealth()),6);

        Font regular = new Font("Times New Roman", Font.BOLD, 15);
        g.setFont(regular);
        String msg1 = "CONGRATULATIONS, YOU PASSED LEVEL2!";
        String msg2_1 = "In the next and final level";
        String msg2_2 = "You will be facing both Creepers and Spider Jockeys";
        String msg2_3 = "Good luck and make every shot count!";
        String msg3 = "Beat the target to proceed!";
        g.setColor(Color.WHITE);
        Font small1 = new Font("Times New Roman", Font.BOLD, 30);
        Font small2 = new Font("Times New Roman", Font.BOLD,20);
        Font small3 = new Font("Times New Roman", Font.BOLD,25);
        FontMetrics fm1 = getFontMetrics(small1);
        FontMetrics fm2 = getFontMetrics(small2);
        FontMetrics fm3 = getFontMetrics(small3);
        //g.setColor(new Color(250,150,0));
        g.setColor(Color.WHITE);
        g.setFont(small1);
        g.drawString(msg1, (800 - fm1.stringWidth(msg1)) / 2, 450 / 2 - 105);
        g.setFont(small2);
        g.drawString(msg2_1, (800 - fm2.stringWidth(msg2_1)) / 2, 450 / 2 - 70);
        g.drawString(msg2_2, (800 - fm2.stringWidth(msg2_2)) / 2, 450 / 2 - 50);
        g.drawString(msg2_3, (800 - fm2.stringWidth(msg2_3)) / 2, 450 / 2 - 30);
        g.setFont(small3);
        g.drawString(msg3, (800 - fm3.stringWidth(msg3)) / 2, 450 / 2 + 2);

        if (targetStage4.isVisible()) {
            g.drawImage(targetStage4.getImage(), targetStage4.getX()+10, targetStage4.getY()-12, this);
            g.setColor(Color.white);
            g.fillRect(targetStage4.getX()+9,targetStage4.getY()-30,100,8);
            g.setColor(Color.red);
            g.fillRect(targetStage4.getX()+10,targetStage4.getY()-29,98,6);
            g.setColor(Color.GREEN);
            g.fillRect(targetStage4.getX()+10,targetStage4.getY()-29,(int) Math.round(0.1*98*targetStage4.getHealth()),6);
        } else {
            game = 5;
            setStage5Start(System.currentTimeMillis());
        }

        // Now we are attempting to beat the target
        List<Bullet> bullets = player.getBullets();
        for (Bullet bullet : bullets) {
            if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
            }
        }

    }
    public void drawObjectsStage5(Graphics g) {
        paintBackground(g,3);
        paintLevel(g, 3);
        g.setColor(Color.WHITE);
        Font regular = new Font("Times New Roman", Font.BOLD, 15);
        g.setFont(regular);
        g.drawString("Bullet Count: " + player.getBulletCount(), 675,45);
        g.drawString("Bomb Count: " + player.getBombCount(), 675,65);
        g.drawString("Creepers left: " + creepersLV3.size(), 15, 25);
        g.drawString("Spider Jockeys left: " + spiderJockeysLV3.size(), 15, 45);
        // Draw the Bullet Cool Down
        g.drawString("Bullet CD: ", 15, 65);
        g.setColor(Color.white);
        g.fillRect(95,55,60,10);
        g.setColor(Color.red);
        g.fillRect(96,56,58,8);
        g.setColor(Color.green);
        g.fillRect(96,56,Math.min(58,(int) Math.round(0.0033333*58*(System.currentTimeMillis()-player.getLastBullet()))),8);
        // Draw the Bomb Cool Down
        g.setColor(Color.WHITE);
        g.drawString("Bomb CD: ", 15, 85);
        g.setColor(Color.white);
        g.fillRect(95,75,60,10);
        g.setColor(Color.red);
        g.fillRect(96,76,58,8);
        g.setColor(Color.green);
        g.fillRect(96,76,Math.min(58,(int) Math.round(0.001*58*(System.currentTimeMillis()-player.getLastBomb()))),8);
        // Print the total Score
        g.setColor(null);
        g.drawString("TOTAL SCORE: " + player.getTotalScore(), 650, 25);
        // Print the text for double kill
        if (System.currentTimeMillis() - lastDoubleKill < 1000) {
            String msg1 = "DOUBLE KILL!";
            Font small1 = new Font("Papyrus", Font.BOLD, 30);
            g.setColor(new Color(196, 9, 80, 255));
            g.setFont(small1);
            g.drawString(msg1, 300, 110);
        }
        if (player.isVisible()) {
            if (player.isMoving()) {
                int key = getPlayerKey(System.currentTimeMillis());
                g.drawImage(player.getImageMap().get(key), player.getX(), player.getY(), this);
            } else {
                g.drawImage(player.getImageMap().get(3), player.getX(), player.getY(), this);
            }
            // Draw the player's health
            g.setColor(Color.white);
            g.fillRect(player.getX()-23,player.getY()-20,100,8);
            g.setColor(Color.red);
            g.fillRect(player.getX()-22,player.getY()-19,98,6);
            g.setColor(Color.blue);
            g.fillRect(player.getX()-22,player.getY()-19,(int) Math.round(0.2*98*player.getHealth()),6);
        }
        List<Bullet> bullets = player.getBullets();
        for (Bullet bullet : bullets) {
            if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
            }
        }
        List<Bomb> bombs = player.getBombs();
        for (Bomb bomb : bombs) {
            if (bomb.isVisible()) {
                g.drawImage(bomb.getImage(), bomb.getX(), bomb.getY(), this);
            }
        }
        List<IceBomb> iceBombs = player.getIceBombs();
        for (IceBomb iceBomb : iceBombs) {
            if (iceBomb.isVisible()) {
                g.drawImage(iceBomb.getImage(), iceBomb.getX(), iceBomb.getY(), this);
            }
        }
        for (SpiderJockey spiderJockey : spiderJockeysLV3) {
            if (spiderJockey.isVisible()) {
                g.drawImage(spiderJockey.getImage(), spiderJockey.getX(), spiderJockey.getY(), this);
                // Paint the spider jockey's health bar
                g.setColor(Color.white);
                g.fillRect(spiderJockey.getX()+30,spiderJockey.getY()-10,40,7);
                g.setColor(Color.green);
                g.fillRect(spiderJockey.getX()+31,spiderJockey.getY()-9,38,5);
                g.setColor(Color.red);
                g.fillRect(spiderJockey.getX()+69-(int) ((5-spiderJockey.getHealth())*7.6),spiderJockey.getY()-9,
                        (int) ((5-spiderJockey.getHealth())*7.6),5);
            }
        }
        for (Creeper creeper : creepersLV3) {
            if (creeper.isVisible()) {
                int key = getCreeperKey(System.currentTimeMillis(),creeper.getSpeed());
                g.drawImage(creeper.getImageHashMap().get(key), creeper.getX(), creeper.getY(), this);
                // Paint the creepers' health bar
                g.setColor(Color.white);
                g.fillRect(creeper.getX()+5,creeper.getY()-10,40,7);
                g.setColor(Color.green);
                g.fillRect(creeper.getX()+6,creeper.getY()-9,38,5);
                g.setColor(Color.red);
                if (creeper.getHealth() == 2) {
                    g.fillRect(creeper.getX()+31,creeper.getY()-9,13,5);
                } else if (creeper.getHealth() == 1) {
                    g.fillRect(creeper.getX()+18,creeper.getY()-9,26,5);
                }
            }
        }
        for (Explosion explosion: bombExplosions) {
            if (explosion.isVisible()) {
                int key = (int) ((System.currentTimeMillis() - explosion.getStartTime()) / 100);
                g.drawImage(explosion.getBombMap().get(key), explosion.getCurrX(), explosion.getCurrY(), this);
                if (System.currentTimeMillis() - explosion.getStartTime() >= 900) {
                    explosion.setVisible(false);
                }
            }
        }
        for (Explosion explosion: iceBombExplosions) {
            if (explosion.isVisible()) {
                int key = (int) ((System.currentTimeMillis() - explosion.getStartTime()) / 100);
                g.drawImage(explosion.getIceBombMap().get(key), explosion.getCurrX(), explosion.getCurrY(), this);
                if (System.currentTimeMillis() - explosion.getStartTime() >= 900) {
                    explosion.setVisible(false);
                }
            }
        }
    }
    private void drawGameOver(Graphics g) {
        String msg1Win = "CONGRATULATIONS, YOU HAVE WON!!!", msg1Lose = "GAME OVER";

        String msg2 = "YOUR FINAL SCORE IS " + player.getTotalScore();
        String msg3 = player.getDeathMessage();
        String msg3_1 = "Total bullets shot: " + player.getBulletCount();
        String msg3_2 = "Total bombs shot: " + player.getBombCount();
        String msg3_3 = "Total damage taken: " + (5-player.getHealth());
        String msg3_4 = "Your total game time: " + (System.currentTimeMillis() - stage0Start)/1000 + " seconds";
        String msg4 = "Your total double kill count is " + doubleKillCount;
        Font small1 = new Font("Times New Roman", Font.BOLD, 30);
        Font small2 = new Font("Times New Roman", Font.PLAIN, 20);
        FontMetrics fm1 = getFontMetrics(small1);
        FontMetrics fm2 = getFontMetrics(small2);
        g.setColor(Color.BLACK);
        g.setFont(small1);
        if (msg3 == null) {
            g.drawString(msg1Win, (800 - fm1.stringWidth(msg1Win)) / 2, 450 / 2 - 110);
            g.setFont(small2);
            g.drawString(msg3_1, (800 - fm2.stringWidth(msg3_1)) / 2, 450 / 2 - 80);
            g.drawString(msg3_2, (800 - fm2.stringWidth(msg3_2)) / 2, 450 / 2 - 55);
            g.drawString(msg3_3, (800 - fm2.stringWidth(msg3_3)) / 2, 450 / 2 - 30);
            g.drawString(msg4, (800 - fm2.stringWidth(msg4)) / 2, 450 / 2 - 5);
            g.drawString(msg3_4, (800 - fm2.stringWidth(msg3_4)) / 2, 450 / 2 + 20);
            g.setFont(small1);
            g.drawString(msg2, (800 - fm1.stringWidth(msg2)) / 2, 450 / 2 + 55);
        } else {
            g.drawString(msg1Lose, (800 - fm1.stringWidth(msg1Lose)) / 2, 450 / 2 - 60);
            g.setFont(small2);
            g.drawString(msg3, (800 - fm2.stringWidth(msg3)) / 2, 450 / 2 - 32);
            g.drawString(msg4, (800 - fm2.stringWidth(msg4)) / 2, 450 / 2 - 5);
            g.setFont(small1);
            msg2 = "YOUR FINAL SCORE IS " + Math.max(player.getTotalScore() - 300, 0);
            g.drawString(msg2, (800 - fm1.stringWidth(msg2)) / 2, 450 / 2 + 30);
        }
    }
    public void paintBackground(Graphics g, int i) {
        ImageIcon ii = new ImageIcon();
        String base = System.getProperty("user.dir");
        if (i == 1) {
            ii = new ImageIcon(base + "/src/main/resources/images/background/Background.jpg");
        } else if (i == 2) {
            ii = new ImageIcon(base + "/src/main/resources/images/background/Background2.png");
        } else if (i == 3) {
            ii = new ImageIcon(base + "/src/main/resources/images/background/Background3.png");
        }
        Image background = ii.getImage();
        g.drawImage(background, 0, 0, null);
    }
    public void paintBackgroundBlurry(Graphics g) {
        String base = System.getProperty("user.dir");
        ImageIcon ii = new ImageIcon(base + "/src/main/resources/images/background/BackgroundBlur.jpg");
        Image background = ii.getImage();
        g.drawImage(background, 0, 0, null);
    }
    public void paintBackground2Blurry(Graphics g) {
        String base = System.getProperty("user.dir");
        ImageIcon ii = new ImageIcon(base + "/src/main/resources/images/background/BackgroundBlur2.png");
        Image background = ii.getImage();
        g.drawImage(background, 0, 0, null);
    }
    public void paintLevel(Graphics g, int level) {
        ImageIcon board;
        String base = System.getProperty("user.dir");
        if (level == 1) {
            board = new ImageIcon(base + "/src/main/resources/images/levels/Level1.png");
        } else if (level == 2) {
            board = new ImageIcon(base + "/src/main/resources/images/levels/Level2.png");
        } else {
            board = new ImageIcon(base + "/src/main/resources/images/levels/Level3.png");
        }
        g.drawImage(board.getImage(), 350,15,null);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        inGame();
        updateBullets();
        updateIceBombs();
        updateBombs();
        updatePlayer();
        if (game == 1) {
            checkCollisionsLV1();
            updateCreepersLV1();
        }
        if (game == 2) {
            checkCollisions24(2);
        }
        if (game == 3) {
            checkCollisionsLV2();
            updateSpiderJockeysLV2();
        }
        if (game == 4) {
            checkCollisions24(4);
        }
        if (game == 5) {
            checkCollisionsLV3();
            updateEnemiesLV3();
        }
        repaint();
    }
    private void inGame() { if (game == 6) { timer.stop();}}
    private void updateBullets() {
        List<Bullet> bullets = player.getBullets();
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.isVisible()) {
                bullet.move();
            } else {
                bullets.remove(i);
            }
        }
    }
    private void updateBombs() {
        List<Bomb> bombs = player.getBombs();
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if (bomb.isVisible()) {
                bomb.move();
            } else {
                bombs.remove(i);
            }
        }
    }
    private void updateIceBombs() {
        List<IceBomb> iceBombs = player.getIceBombs();
        for (int i = 0; i < iceBombs.size(); i++) {
            IceBomb iceBomb = iceBombs.get(i);
            if (iceBomb.isVisible()) {
                iceBomb.move();
            } else {
                iceBombs.remove(i);
            }
        }
    }
    private void updatePlayer() {if (player.isVisible()) { player.move();}}
    private void updateCreepersLV1() {
        if (creepersLV1.isEmpty()) {
            if (player.getHealth() > 0) {
                //stage2Start = System.currentTimeMillis();
                bombExplosions.clear();
                iceBombExplosions.clear();
                game = 2;
            } else {
                game = 6;
            }
        }
        for (int i = 0; i < creepersLV1.size(); i++) {
            Creeper a = creepersLV1.get(i);
            if (a.isVisible()) {
                if (System.currentTimeMillis() - stage0Start > 5000) {
                    a.move();
                }
            } else {
                creepersLV1.remove(i);
            }
            if (System.currentTimeMillis() - a.getHitByIceBomb() > 2500) {
                a.restoreSpeed();
            }
        }
    }
    public void updateSpiderJockeysLV2() {
        if (spiderJockeysLV2.isEmpty()) {
            if (player.getHealth() > 0) {
                // sta = System.currentTimeMillis();
                bombExplosions.clear();
                iceBombExplosions.clear();
                game = 4;
            } else {
                game = 6;
            }
        }
        for (int i = 0; i < spiderJockeysLV2.size(); i++) {
            SpiderJockey spiderJockey = spiderJockeysLV2.get(i);
            if (spiderJockey.isVisible()) {
                if (System.currentTimeMillis() - stage3Start > 2000) {
                    spiderJockey.move();
                }
            } else {
                spiderJockeysLV2.remove(i);
            }
            if (System.currentTimeMillis() - spiderJockey.getHitByIceBomb() > 2500) {
                spiderJockey.restoreSpeed();
            }
        }
    }
    public void checkCollisionsLV1() {
        for (Creeper creeper : creepersLV1) {
            //Rectangle r2 = creeper.getBounds();
            if (player.getX()+player.getWidth() > creeper.getX()-30) {
                player.decreaseHealth();
                player.changeScore(-100);
                creeper.setVisible(false);
                if (player.getHealth() == 0) {
                    player.setVisible(false);
                    player.setDeathMessage("You Are Blown Up By A Creeper!");
                    bombExplosions.clear();
                    iceBombExplosions.clear();
                    game = 6;
                }
            }
        }
        List<Bullet> bullets = player.getBullets();
        for (Bullet bullet : bullets) {
            Rectangle r1 = bullet.getBounds();
            for (Creeper creeper : creepersLV1) {
                Rectangle r2 = creeper.getBounds();
                if (r1.intersects(r2)) {
                    bullet.setVisible(false);
                    creeper.decreaseHealth(1);
                    if (creeper.getHealth() <= 0) {
                        creeper.setVisible(false);
                        player.changeScore(35);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(50);
                        }
                    }
                }
            }
        }
        List<Bomb> bombs = player.getBombs();
        for (Bomb bomb : bombs) {
            Rectangle r1 = bomb.getBounds();
            for (Creeper creeper : creepersLV1) {
                Rectangle r2 = creeper.getBounds();
                if (r1.intersects(r2)) {
                    bomb.setVisible(false);
                    bombExplosions.add(new Explosion((int) r1.getX()-20, (int) r1.getY()-35));
                    creeper.decreaseHealth(2);
                    if (creeper.getHealth() <= 0) {
                        creeper.setVisible(false);
                        player.changeScore(35);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(50);
                        }
                    }
                }
            }
        }

        List<IceBomb> iceBombs = player.getIceBombs();
        for (IceBomb iceBomb : iceBombs) {
            Rectangle r1 = iceBomb.getBounds();
            for (Creeper creeper : creepersLV1) {
                Rectangle r2 = creeper.getBounds();
                if (r1.intersects(r2)) {
                    iceBomb.setVisible(false);
                    iceBombExplosions.add(new Explosion((int) r1.getX()-20, (int) r1.getY()-25));
                    creeper.decreaseHealth(1);
                    creeper.decreaseSpeed(2);
                    if (creeper.getHealth() <= 0) {
                        creeper.setVisible(false);
                        player.changeScore(35);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(50);
                        }
                    } else {
                        creeper.setHitByIceBomb(System.currentTimeMillis());
                    }
                }
            }
        }
    }
    public void checkCollisions24(int i) {
        List<Bullet> bullets = player.getBullets();
        Target target;
        if (i == 2) {
            target = targetStage2;
        } else {
            target = targetStage4;
        }
        // Rectangle rTarget = targetStage2.getBounds();
        for (Bullet bullet: bullets) {
            if (bullet.getX() > target.getX() + 20) {
                bullet.setVisible(false);
                target.decreaseHealth(1);
                if (target.getHealth() <= 0) {
                    target.setVisible(false);
                    player.changeScore(20);
                    game = i+1;
                }
            }
        }
    }
    public void checkCollisionsLV2() {
        for (SpiderJockey spiderJockey : spiderJockeysLV2) {
            //Rectangle r2 = creeper.getBounds();
            if (player.getX()+player.getWidth() > spiderJockey.getX() + 20) {
                player.decreaseHealth();
                player.changeScore(-100);
                spiderJockey.setVisible(false);
                if (player.getHealth() == 0) {
                    player.setVisible(false);
                    player.setDeathMessage("You Are Stung By A Spider Jockey!");
                    game = 6;
                }
            }
        }
        List<Bullet> bullets = player.getBullets();
        List<Bomb> bombs = player.getBombs();
        List<IceBomb> iceBombs = player.getIceBombs();
        for (Bullet bullet : bullets) {
            Rectangle r1 = bullet.getBounds();
            for (SpiderJockey spiderJockey : spiderJockeysLV2) {
                Rectangle r2 = spiderJockey.getBounds();
                if (r1.intersects(r2)) {
                    bullet.setVisible(false);
                    spiderJockey.decreaseHealth(1);
                    if (spiderJockey.getHealth() <= 0) {
                        spiderJockey.setVisible(false);
                        player.changeScore(40);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(55);
                        }
                    }
                }
            }
        }
        for (Bomb bomb : bombs) {
            Rectangle r1 = bomb.getBounds();
            for (SpiderJockey spiderJockey : spiderJockeysLV2) {
                Rectangle r2 = spiderJockey.getBounds();
                if (hit(r1,r2)) {
                    bombExplosions.add(new Explosion((int) r1.getX()-20, (int) r1.getY()-35));
                    bomb.setVisible(false);
                    if (spiderJockey.isDescending()) {
                        spiderJockey.setVisible(false);
                        player.changeScore(40);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(55);
                        }
                    } else {
                        spiderJockey.decreaseHealth(2);
                        if (spiderJockey.getHealth() <= 0) {
                            spiderJockey.setVisible(false);
                            player.changeScore(40);
                            secondLastKill = lastKill;
                            lastKill = System.currentTimeMillis();
                            if (lastKill - secondLastKill <= 200) {
                                lastDoubleKill = lastKill;
                                doubleKillCount += 1;
                                player.changeScore(55);
                            }
                        }
                    }
                }
            }
        }
        for (IceBomb iceBomb : iceBombs) {
            Rectangle r1 = iceBomb.getBounds();
            for (SpiderJockey spiderJockey : spiderJockeysLV2) {
                Rectangle r2 = spiderJockey.getBounds();
                if (hit(r1,r2)) {
                    iceBombExplosions.add(new Explosion((int) r1.getX()-20, (int) r1.getY()-35));
                    iceBomb.setVisible(false);
                    if (spiderJockey.isDescending()) {
                        spiderJockey.setVisible(false);
                        player.changeScore(40);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(55);
                        }
                    } else {
                        spiderJockey.decreaseHealth(2);
                        if (spiderJockey.getHealth() <= 0) {
                            spiderJockey.setVisible(false);
                            player.changeScore(40);
                            secondLastKill = lastKill;
                            lastKill = System.currentTimeMillis();
                            if (lastKill - secondLastKill <= 200) {
                                lastDoubleKill = lastKill;
                                doubleKillCount += 1;
                                player.changeScore(55);
                            }
                        }
                    }
                }
            }
        }
    }
    public void checkCollisionsLV3() {
        for (SpiderJockey spiderJockey : spiderJockeysLV3) {
            //Rectangle r2 = creeper.getBounds();
            if (player.getX()+player.getWidth() > spiderJockey.getX() + 20) {
                player.decreaseHealth();
                player.changeScore(-100);
                spiderJockey.setVisible(false);
                if (player.getHealth() == 0) {
                    bombExplosions.clear();
                    iceBombExplosions.clear();
                    player.setVisible(false);
                    player.setDeathMessage("You Are Stung By A Spider Jockey!");
                    game = 6;
                }
            }
        }

        for (Creeper creeper : creepersLV3) {
            //Rectangle r2 = creeper.getBounds();
            if (player.getX()+player.getWidth() > creeper.getX() + 5) {
                player.decreaseHealth();
                player.changeScore(-100);
                creeper.setVisible(false);
                if (player.getHealth() == 0) {
                    bombExplosions.clear();
                    iceBombExplosions.clear();
                    player.setVisible(false);
                    player.setDeathMessage("You Are Blown Up By A Creeper!");
                    game = 6;
                }
            }
        }

        List<Bullet> bullets = player.getBullets();
        List<Bomb> bombs = player.getBombs();
        List<IceBomb> iceBombs = player.getIceBombs();
        for (Bullet bullet : bullets) {
            Rectangle r1 = bullet.getBounds();
            for (Creeper creeper : creepersLV3) {
                Rectangle r2 = creeper.getBounds();
                if (r1.intersects(r2)) {
                    bullet.setVisible(false);
                    creeper.decreaseHealth(1);
                    if (creeper.getHealth() <= 0) {
                        creeper.setVisible(false);
                        player.changeScore(40);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(55);
                        }
                    }
                }
            }

            for (SpiderJockey spiderJockey : spiderJockeysLV3) {
                Rectangle r2 = spiderJockey.getBounds();
                if (r1.intersects(r2)) {
                    bullet.setVisible(false);
                    spiderJockey.decreaseHealth(1);
                    if (spiderJockey.getHealth() <= 0) {
                        spiderJockey.setVisible(false);
                        player.changeScore(45);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(60);
                        }
                    }
                }
            }
        }
        for (Bomb bomb : bombs) {
            Rectangle r1 = bomb.getBounds();
            for (SpiderJockey spiderJockey : spiderJockeysLV3) {
                Rectangle r2 = spiderJockey.getBounds();
                if (hit(r1,r2)) {
                    bombExplosions.add(new Explosion((int) r1.getX()-20, (int) r1.getY()-35));
                    bomb.setVisible(false);
                    if (spiderJockey.isDescending()) {
                        spiderJockey.setVisible(false);
                        player.changeScore(45);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(60);
                        }
                    } else {
                        spiderJockey.decreaseHealth(2);
                        if (spiderJockey.getHealth() <= 0) {
                            spiderJockey.setVisible(false);
                            player.changeScore(45);
                            secondLastKill = lastKill;
                            lastKill = System.currentTimeMillis();
                            if (lastKill - secondLastKill <= 200) {
                                lastDoubleKill = lastKill;
                                doubleKillCount += 1;
                                player.changeScore(60);
                            }
                        }
                    }
                }
            }

            for (Creeper creeper : creepersLV3) {
                Rectangle r2 = creeper.getBounds();
                if (hit(r1,r2)) {
                    bombExplosions.add(new Explosion((int) r1.getX()-20, (int) r1.getY()-35));
                    bomb.setVisible(false);
                    creeper.decreaseHealth(2);
                    if (creeper.getHealth() <= 0) {
                        creeper.setVisible(false);
                        player.changeScore(40);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(55);
                        }
                    }
                }
            }

        }
        for (IceBomb iceBomb : iceBombs) {
            Rectangle r1 = iceBomb.getBounds();
            for (SpiderJockey spiderJockey : spiderJockeysLV3) {
                Rectangle r2 = spiderJockey.getBounds();
                if (hit(r1,r2)) {
                    iceBombExplosions.add(new Explosion((int) r1.getX()-20, (int) r1.getY()-35));
                    iceBomb.setVisible(false);
                    if (spiderJockey.isDescending()) {
                        spiderJockey.setVisible(false);
                        player.changeScore(45);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(60);
                        }
                    } else {
                        spiderJockey.setHitByIceBomb(System.currentTimeMillis());
                        spiderJockey.decreaseHealth(1);
                        spiderJockey.decreaseSpeed(2);
                        if (spiderJockey.getHealth() <= 0) {
                            spiderJockey.setVisible(false);
                            player.changeScore(45);
                            secondLastKill = lastKill;
                            lastKill = System.currentTimeMillis();
                            if (lastKill - secondLastKill <= 200) {
                                lastDoubleKill = lastKill;
                                doubleKillCount += 1;
                                player.changeScore(60);
                            }
                        }
                    }
                }
            }

            for (Creeper creeper : creepersLV3) {
                Rectangle r2 = creeper.getBounds();
                if (hit(r1,r2)) {
                    iceBombExplosions.add(new Explosion((int) r1.getX()-20, (int) r1.getY()-35));
                    iceBomb.setVisible(false);
                    creeper.decreaseHealth(1);
                    creeper.decreaseSpeed(2);
                    creeper.setHitByIceBomb(System.currentTimeMillis());
                    if (creeper.getHealth() <= 0) {
                        creeper.setVisible(false);
                        player.changeScore(40);
                        secondLastKill = lastKill;
                        lastKill = System.currentTimeMillis();
                        if (lastKill - secondLastKill <= 200) {
                            lastDoubleKill = lastKill;
                            doubleKillCount += 1;
                            player.changeScore(44);
                        }
                    }
                }
            }
        }
    }
    public void updateEnemiesLV3() {
        if (spiderJockeysLV3.isEmpty() && creepersLV3.isEmpty()) {
            bombExplosions.clear();
            iceBombExplosions.clear();
            game = 6;
        }
        for (int i = 0; i < spiderJockeysLV3.size(); i++) {
            SpiderJockey spiderJockey = spiderJockeysLV3.get(i);
            if (spiderJockey.isVisible()) {
                if (System.currentTimeMillis() - stage5Start > 2000) {
                    spiderJockey.move();
                }
            } else {
                spiderJockeysLV3.remove(i);
            }
            if (System.currentTimeMillis() - spiderJockey.getHitByIceBomb() > 2500) {
                spiderJockey.restoreSpeed();
            }
        }

        for (int i = 0; i < creepersLV3.size(); i++) {
            Creeper creeper = creepersLV3.get(i);
            if (creeper.isVisible()) {
                if (System.currentTimeMillis() - stage5Start > 2000) {
                    creeper.move();
                }
            } else {
                creepersLV3.remove(i);
            }
            if (System.currentTimeMillis() - creeper.getHitByIceBomb() > 2500) {
                creeper.restoreSpeed();
            }
        }

    }
    public int getPlayerKey(Long currTimeMill) {
        if ((currTimeMill / 50) % 12 == 0) {
            return 0;
        } else if ((currTimeMill / 50) % 12 == 1 || (currTimeMill / 50) % 12 == 11) {
            return 1;
        } else if ((currTimeMill / 50) % 12 == 2 || (currTimeMill / 50) % 12 == 10) {
            return 2;
        } else if ((currTimeMill / 50) % 12 == 3 || (currTimeMill / 50) % 12 == 9) {
            return 3;
        } else if ((currTimeMill / 50) % 12 == 4 || (currTimeMill / 50) % 12 == 8) {
            return 4;
        } else if ((currTimeMill / 50) % 12 == 5 || (currTimeMill / 50) % 12 == 7) {
            return 5;
        } else {
            return 6;
        }
    }
    public int getCreeperKey(Long currTimeMill, int speed) {
        if ((currTimeMill / (45 - 5*speed)) % 16 == 0) {
            return 0;
        } else if ((currTimeMill / (45 - 5*speed)) % 16 == 1 || (currTimeMill / (45 - 5*speed)) % 16 == 15) {
            return 1;
        } else if ((currTimeMill / (45 - 5*speed)) % 16 == 2 || (currTimeMill / (45 - 5*speed)) % 16 == 14) {
            return 2;
        } else if ((currTimeMill / (45 - 5*speed)) % 16 == 3 || (currTimeMill / (45 - 5*speed)) % 16 == 13) {
            return 3;
        } else if ((currTimeMill / (45 - 5*speed)) % 16 == 4 || (currTimeMill / (45 - 5*speed)) % 16 == 12) {
            return 4;
        } else if ((currTimeMill / (45 - 5*speed)) % 16 == 5 || (currTimeMill / (45 - 5*speed)) % 16 == 11) {
            return 5;
        } else if ((currTimeMill / (45 - 5*speed)) % 16 == 6 || (currTimeMill / (45 - 5*speed)) % 16 == 10) {
            return 6;
        }  else if ((currTimeMill / (45 - 5*speed)) % 16 == 7 || (currTimeMill / (45 - 5*speed)) % 16 == 9) {
            return 7;
        }  else {
            return 8;
        }
    }
    public boolean hit(Rectangle r1, Rectangle r2) {
        return Math.abs(r2.getX() + r2.getWidth() / 2 - r1.getX() - r1.getWidth() / 2) <= 30 &&
                Math.abs(r2.getY() + r2.getHeight() / 2 - r1.getY() - r1.getHeight() / 2) <= 30;
    }
    public int getGame() { return game;}
    public void setGame(int game) { this.game = game;}
    public long getStage3Start() { return stage3Start;}
    public void setStage3Start(long stage3Start) { this.stage3Start = stage3Start;}
    public long getStage5Start() { return stage5Start;}
    public void setStage5Start(long stage5Start) { this.stage5Start = stage5Start;}
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) { player.keyReleased(e);}
        @Override
        public void keyPressed(KeyEvent e) { player.keyPressed(e);}
    }
}
