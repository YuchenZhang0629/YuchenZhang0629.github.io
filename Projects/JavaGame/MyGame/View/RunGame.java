package edu.uchicago.gerber._08final.MyGame.View;

import javax.swing.*;
import java.awt.*;

public class RunGame extends JFrame {
    public RunGame() {
        initInterface();
    }

    private void initInterface() {

        add(new Paint());
        setTitle("Prototype Game");
        setSize(800, 450);

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            RunGame ex = new RunGame();
            ex.setVisible(true);
        });
    }
}