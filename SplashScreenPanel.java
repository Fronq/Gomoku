/*
 * Frank Chen
 * SplashScreenPanel.java
 * Initializes and adds functionality to the SplashScreenPanel object
 * ICS4U1
 * November 28, 2018
 */
package com.company;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class SplashScreenPanel extends JPanel {

    //Declare objects
    private ImageIcon image;
    private JLabel bg;

    //Initialize components
    public SplashScreenPanel(){
        //Adds a background image by adding a scaled imageIcon to a JLabel
        image = new ImageIcon(new ImageIcon("Banner.png").getImage().getScaledInstance(767, 487, Image.SCALE_SMOOTH));
        //Creates a JLabel using the image
        bg = new JLabel(image);
        //Adds the JLabel to the JPanel
        add(bg);
    }

    /**
     * Delay the program
     * pre: none
     * post: The program is delayed
     */
    public void load(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
