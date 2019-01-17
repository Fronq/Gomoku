/*
 * Frank Chen
 * Main.java
 * Initializes the JFrame
 * ICS4U1
 * November 28, 2018
 */
package com.company;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    public static void main(String[] args) {
	// write your code here

        //Initializes frame size variables
        int windowHeight = 520;
        int windowWidth = 780;
        int windowHeightOffset = -3;

        //Creates JFrame
        JFrame frame = new JFrame("Gomoku");
        
        //Creates the JPanels in the JFrame
        SplashScreenPanel splash = new SplashScreenPanel();
        JPanel container = new JPanel();
        GamePanel game = new GamePanel();
        SidePanel side = new SidePanel(game);
        game.setSidePanel(side);

        //Initializes the cardlayout to switch from the splash panel to the main panel
        String SPLASH_PANEL = "splash";
        String MAIN_PANEL = "main";
        CardLayout cl = new CardLayout();
        JPanel cards = new JPanel(cl);
        cards.add(splash, SPLASH_PANEL);
        cards.add(container,MAIN_PANEL);
        cl.show(cards, SPLASH_PANEL);

        //Change frame properties
        frame.setIconImage(new ImageIcon("Icon.png").getImage());
        frame.setSize(new Dimension(windowWidth,windowHeight+windowHeightOffset));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        //Add the cards panel to the frame
        frame.getContentPane().add(cards);

        //Add the game panel and side panel to the container panel
        container.setLayout(new BorderLayout());
        container.add(game);
        container.add(side, BorderLayout.EAST);

        //Switches to the main panel after waiting for the splash panel
        splash.load();
        cl.show(cards, MAIN_PANEL);

    }
}
