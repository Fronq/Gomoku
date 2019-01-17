/*
 * Frank Chen
 * SidePanel.java
 * Initializes and adds functionality to the SidePanel object
 * ICS4U1
 * November 28, 2018
 */
package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class SidePanel extends JPanel implements ActionListener{

    //Objects used to customize JLabels and JButtons
    private Font body = new Font("Arial", Font.PLAIN, 24);
    private Font sub = new Font("Arial", Font.PLAIN, 32);
    private Font head = new Font("Arial", Font.PLAIN, 48);
    private Color lightGray = new Color(200,200,200);
    private Color darkGray = new Color(50,50,50);

    //Variables and objects used for the cardLayout
    private final String PREGAME_PANEL = "pregame";
    private final String DURINGGAME_PANEL = "game";
    private final String HELP_PANEL = "help";
    private final String WIN_PANEL = "win";
    private CardLayout cl = new CardLayout();
    private JPanel card1, card2, card3, card4;

    //Declares an instance of the GamePanel object
    private GamePanel game;

    //Variables used for the size slider
    private final int MAX_SIZE = 47;
    private final int MIN_SIZE = 0;
    private final int START_SIZE = 5;
    private Hashtable labels;
    private int size = 15;
    private JPanel sizePanel;

    //JComponents and variables used in the pregame panel
    private JButton startButton;
    private JButton helpButton1;
    private JSlider sizeSlider;
    private JLabel sizeLabel;
    private JButton versusButton;
    private boolean isVersusAI;

    //JComponents and variables used in the during game panel
    private JPanel gamePanelContainer;
    private JButton undoButton;
    private JButton resetButton;
    private JLabel scoreLabel;
    private JButton helpButton2;
    private JButton menuButton2;
    private JButton historyButton;
    private boolean showHistory;

    //JComponents and variables used in the help panel
    private JTextArea instructionsLabel;
    private JScrollPane helpScroll;
    private JButton backButton;
    private boolean fromPregame, fromGame;

    //JComponents used in the win panel
    private JLabel winLabel;
    private JLabel movesLabel;
    private JButton menuButton1;
    private JButton againButton;

    public SidePanel(GamePanel game){
        //Instantiates the GamePanel class
        this.game = game;

        //Instantiates the JPanels (cards) that will be added to the parent JPanel (this)
        card1 = new JPanel(new GridLayout(4,1));
        card2 = new JPanel(new GridLayout(5,1));
        card3 = new JPanel(new BorderLayout());
        card4 = new JPanel(new GridLayout(4,1));

        //Initializes the parent JPanel (this) and adds the other cards to it
        initializeCard();

        //Initializes each card and its components
        initializePregamePanel();
        initializeGamePanel();
        initializeHelpPanel();
        initializeWinPanel();

        //Shows the pregame panel
        changeCard1();

    }

    /**
     * Changes to the first card (pregame panel) and updates fromPregame and fromGame
     * pre: none
     * post: The pregame panel is shown on the card layout, fromPregame is set to true and fromGame is set to false
     */
    private void changeCard1(){
        cl.show(this, PREGAME_PANEL);
        fromPregame = true;
        fromGame = false;
    }

    /**
     * Changes to the second card (duringgame panel) and updates fromPregame and fromGame
     * pre: none
     * post: The duringgame panel is shown on the card layout, fromPregame is set to false and fromGame is set to true
     */
    private void changeCard2(){
        cl.show(this,DURINGGAME_PANEL);
        fromPregame = false;
        fromGame = true;
    }

    /**
     * Changes to the third card (help panel)
     * pre: none
     * post: The help panel is shown on the card layout
     */
    private void changeCard3(){
        cl.show(this, HELP_PANEL);
    }

    /**
     * Changes to the fourth card (win panel)
     * pre: none
     * post: The win panel is shown on the card layout
     */
    private void changeCard4(){
        cl.show(this, WIN_PANEL);
    }

    /**
     * Used in the help panel to return to the previous card using the boolean value stored in changeCard1() and
     * changeCard2()
     * pre: none
     * post: The previous panel is shown on the card layout
     */
    private void changePrevious(){
        if(fromPregame){
            changeCard1();
        }
        else if (fromGame){
            changeCard2();
        }
    }

    /**
     * Displays the winner and shows the win panel
     * pre: color is "black" or "white"
     * post: The winner is displayed and the win panel is shown
     */
    public void showGameWin(String color){
        updateWinLabel(color);
        updateMovesLabel(game.getBoard().piecesPlaced);
        changeCard4();
    }

    /**
     * Displays the winner
     * pre: color is "black" or "white"
     * post: The winner is updated on a JLabel in the win panel
     */
    private void updateWinLabel(String color){
        winLabel.setText(color.toUpperCase() + " WINS!");
    }

    /**
     * Displays the amount of moves used to win the game
     * pre: piecesPlayed > 9
     * post: The amount of moves used to win the game is displayed
     */
    private void updateMovesLabel(int piecesPlayed){
        movesLabel.setText("# OF MOVES: " + piecesPlayed);
    }

    /**
     * Displays the size of the board
     * pre: size > 0
     * post: The size of the board is updated on a JLabel in the pregame panel
     */
    private void updateSizeLabel(int size){
        sizeLabel.setText("SIZE: " + size + "x" + size);
    }

    //Changes the versusButton text based on a boolean controlled by the button press
    /**
     * Displays who the player is versing
     * pre: none
     * post: The JLabel displaying the opponent is updated in the pregame panel and the button color is changed
     */
    private void updateVersusButton(){
        if (isVersusAI){
            versusButton.setText("VS AI");
            versusButton.setBackground(new Color(0,0,100));
        }
        else{
            versusButton.setText("VS PLAYER");
            versusButton.setBackground(new Color(100,0,0));
        }
    }

    //Changes the historyButton colour based on a boolean controlled by the button press
    /**
     * Changes the history
     * pre: none
     * post: The color of the history button is changed
     */
    private void updateHistoryButton(){
        if (showHistory){
            historyButton.setBackground(new Color(0,100,0));
        }
        else{
            historyButton.setBackground(darkGray);
        }
    }

    //Displays the score of both sides based on the parameter
    /**
     * Displays the score
     * pre: none
     * post: The score is updated on a JLabel in the duringgame panel
     */
    public void updateScoreLabel(int blackWins, int whiteWins){
        scoreLabel.setText("B: " + blackWins + " | W: " + whiteWins);
    }

    //Changes parent JFrame (this) properties and adds the cards to it
    /**
     * Changes the parent JFrame (this) properties and adds the cards to it
     * pre: none
     * post: The layout and size of this is set, and the cards are added to this
     */
    private void initializeCard(){
        setLayout(cl);
        setPreferredSize(new Dimension(287,0));

        add(card1, PREGAME_PANEL);
        add(card2, DURINGGAME_PANEL);
        add(card3, HELP_PANEL);
        add(card4, WIN_PANEL);
    }

    /**
     * Initializes JComponents used in the pregame panel and adds them to it
     * pre: none
     * post:  JComponents are initialized and added to the pregame panel
     */
    private void initializePregamePanel(){
        helpButton1 = helpButton();
        initializeStartButton();
        initializeSizePanel();
        initializeVersusButton();

        card1.add(startButton);
        card1.add(helpButton1);
        card1.add(versusButton);
        card1.add(sizePanel);
    }

    /**
     * Initializes JComponents used in the duringgame panel and adds them to it
     * pre: none
     * post: JComponents are initialized and added to the duringame panel
     */
    private void initializeGamePanel(){
        initializeGamePanelContainer();
        initializeScoreLabel();
        initializeHistoryButton();
        helpButton2 = helpButton();
        menuButton2 = menuButton();

        card2.add(gamePanelContainer);
        card2.add(historyButton);
        card2.add(menuButton2);
        card2.add(helpButton2);
        card2.add(scoreLabel);
    }

    /**
     * Initializes JComponents used in the help panel and adds them to it
     * pre: none
     * post: JComponents are initialized and added to the help panel
     */
    private void initializeHelpPanel(){
        initializeBackButton();
        initializeInstructionsLabel();
        initializeHelpScroll();

        card3.add(backButton, BorderLayout.NORTH);
        card3.add(helpScroll, BorderLayout.CENTER);
    }

    /**
     * Initializes JComponents used in the win panel and adds them to it
     * pre: none
     * post: JComponents are initialized and added to the win panel
     */
    private void initializeWinPanel(){
        initializeWinLabel();
        initializeMovesLabel();
        initializeAgainButton();
        menuButton1 = menuButton();

        card4.add(winLabel);
        card4.add(movesLabel);
        card4.add(menuButton1);
        card4.add(againButton);
    }

    //Initializes the versusButton
    private void initializeVersusButton(){
        isVersusAI = false;
        versusButton = new JButton();
        versusButton.addActionListener(this);

        versusButton.setFocusPainted(false);
        versusButton.setForeground(lightGray);
        versusButton.setFont(sub);
        updateVersusButton();
    }

    //Initializes the backButton
    private void initializeBackButton(){
        backButton = new JButton("BACK");
        backButton.addActionListener(this);

        backButton.setFocusPainted(false);
        backButton.setBackground(darkGray);
        backButton.setForeground(lightGray);
        backButton.setFont(body);
    }

    //Initializes the helpButton
    private JButton helpButton(){
        JButton helpButton = new JButton("HELP");
        helpButton.addActionListener(this);

        helpButton.setFocusPainted(false);
        helpButton.setBackground(darkGray);
        helpButton.setForeground(lightGray);
        helpButton.setFont(sub);
        return helpButton;
    }

    //Initializes the menuButton
    private JButton menuButton(){
        JButton menuButton = new JButton("MENU");
        menuButton.addActionListener(this);

        menuButton.setFocusPainted(false);
        menuButton.setBackground(darkGray);
        menuButton.setForeground(lightGray);
        menuButton.setFont(sub);
        return menuButton;
    }

    //Initializes the againButton
    private void initializeAgainButton(){
        againButton = new JButton("AGAIN");
        againButton.addActionListener(this);

        againButton.setFocusPainted(false);
        againButton.setBackground(darkGray);
        againButton.setForeground(lightGray);
        againButton.setFont(sub);
    }

    private void initializeWinLabel(){
        winLabel = new JLabel();
        winLabel.setFont(sub);
        winLabel.setOpaque(true);
        winLabel.setForeground(lightGray);
        winLabel.setBackground(darkGray);
        winLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    private void initializeMovesLabel(){
        movesLabel = new JLabel();
        movesLabel.setFont(sub);
        movesLabel.setOpaque(true);
        movesLabel.setForeground(lightGray);
        movesLabel.setBackground(darkGray);
        movesLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    //Initializes the historyButton
    private void initializeHistoryButton(){
        historyButton = new JButton("HISTORY");
        historyButton.addActionListener(this);
        showHistory = false;

        historyButton.setFocusPainted(false);
        historyButton.setBackground(darkGray);
        historyButton.setForeground(lightGray);
        historyButton.setFont(sub);
    }

    //Initializes the undoButton
    private void initializeUndoButton(){
        undoButton = new JButton("UNDO");
        undoButton.addActionListener(this);

        undoButton.setFocusPainted(false);
        undoButton.setBackground(darkGray);
        undoButton.setForeground(lightGray);
        undoButton.setFont(sub);
    }

    //Initializes the resetButton
    private void initializeResetButton(){
        resetButton = new JButton("RESET");
        resetButton.addActionListener(this);

        resetButton.setFocusPainted(false);
        resetButton.setBackground(darkGray);
        resetButton.setForeground(lightGray);
        resetButton.setFont(sub);
    }

    //Initializes the gamePanelContainer
    private void initializeGamePanelContainer(){
        gamePanelContainer = new JPanel(new GridLayout(1,2));
        initializeUndoButton();
        initializeResetButton();

        gamePanelContainer.add(undoButton);
        gamePanelContainer.add(resetButton);
    }

    //Initializes the scoreLabel
    private void initializeScoreLabel(){
        scoreLabel = new JLabel("B: 0 | W: 0");
        scoreLabel.setFont(sub);
        scoreLabel.setOpaque(true);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setForeground(lightGray);
        scoreLabel.setBackground(darkGray);
    }

    //Initializes the sizeLabel
    private void initializeSizeLabel(){
        sizeLabel = new JLabel("SIZE: 15x15");
        sizeLabel.setFont(body);
        sizeLabel.setBorder(new EmptyBorder(20,0,0,0));
        sizeLabel.setForeground(lightGray);
    }

    //Initializes the sizePanel
    private void initializeSizePanel(){
        sizePanel = new JPanel();
        sizePanel.setBackground(darkGray);
        initializeSizeLabel();
        initializeSizeSlider();

        sizePanel.add(sizeLabel);
        sizePanel.add(sizeSlider);
    }

    //Initializes the startButton
    private void initializeStartButton(){
        startButton = new JButton("START");
        startButton.addActionListener(this);

        startButton.setFocusPainted(false);
        startButton.setBackground(darkGray);
        startButton.setForeground(lightGray);
        startButton.setFont(head);
    }

    //Initializes the sizeSlider, adds labels to the slider and adds a ChangeListener to get input
    private void initializeSizeSlider(){
        initializeSliderLabels();
        sizeSlider = new JSlider(MIN_SIZE,MAX_SIZE,START_SIZE);
        sizeSlider.setPreferredSize(new Dimension(275,40));
        sizeSlider.setBackground(darkGray);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setMinorTickSpacing(1);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setLabelTable(labels);
        sizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                    size = 5+(source.getValue()*2);
                    game.setSize(size);
                    updateSizeLabel(size);
            }
        });

    }

    //Initializes the labels for the sizeSlider
    private void initializeSliderLabels(){

        Font font = new Font("Arial", Font.PLAIN, 12);

        labels = new Hashtable();

        JLabel label1 = new JLabel("5");
        JLabel label2 = new JLabel("15");
        JLabel label3 = new JLabel("99");

        label1.setFont(font);
        label2.setFont(font);
        label3.setFont(font);

        label1.setForeground(lightGray);
        label2.setForeground(lightGray);
        label3.setForeground(lightGray);

        label2.setForeground(Color.WHITE);

        labels.put(0,label1);
        labels.put(5,label2);
        labels.put(47,label3);
    }

    //Initializes the helpScroll
    private void initializeHelpScroll(){
        helpScroll = new JScrollPane(instructionsLabel);

        //Changes the color of the slider and the track of the vertical scroll bar
        helpScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI()
        {
            @Override
            protected void configureScrollBarColors(){
                this.thumbColor = lightGray;
                this.trackColor = darkGray;
            }

        });
    }

    //Initializes the instructionsLabel
    private void initializeInstructionsLabel(){
        instructionsLabel = new JTextArea();
        instructionsLabel.setMargin(new Insets(10,10,10,0));
        instructionsLabel.setEditable(false);
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionsLabel.setForeground(lightGray);
        instructionsLabel.setBackground(darkGray);

        instructionsLabel.setText("OVERVIEW----------------------------------");

        instructionsLabel.append("\n\nGomoku is a strategy board game" +
                "\n(also known as Five in a Row)" +
                "\nThe players in the game take turns" +
                "\nplacing pieces onto empty intersections" +
                "\nin the board. The winner is the first" +
                "\nplayer to place 5 of the same coloured" +
                "\npieces in an unbroken row either" +
                "\nhorizontally, vertically, or diagonally.");

        instructionsLabel.append("\n\nINSTRUCTIONS------------------------------");

        instructionsLabel.append("\n\nPress the start button to begin." +
                "\nMove the mouse to where you want the" +
                "\npiece to be placed, and click to place" +
                "\nthe piece.");

        instructionsLabel.append("\n\nFEATURES----------------------------------");

        instructionsLabel.append("\n\nRESIZE: The size of the board can" +
                "\nbe changed using the slider before" +
                "\nstarting the game.");

        instructionsLabel.append("\n\nCHOOSE OPPONENT: Choose to play" +
                "\nagainst the computer or another player" +
                "\nusing the VS button before starting" +
                "\nthe game.");

        instructionsLabel.append("\n\nUNDO: During the game, the undo" +
                "\nbutton can be used to undo the last" +
                "\nmove made.");

        instructionsLabel.append("\n\nRESET: During the game, the reset" +
                "\nbutton can be used to reset all the" +
                "\nmoves made ina round, and clearing" +
                "\nthe board.");

        instructionsLabel.append("\n\nHISTORY: During the game, the" +
                "\nhistory button can be see the order" +
                "\nthat the pieces were placed in.");
    }

    //Returns isVersusAI
    public boolean getIsVersusAI(){
        return isVersusAI;
    }

    /**
     * Assigns actions when a button is pressed
     * pre: none
     * post: The function of each button is assigned
     */
    public void actionPerformed(ActionEvent e){

        //Pressing the startButton will show the duringgame panel and run the game
        if (e.getSource() == startButton){
            changeCard2();
            game.run();
        }

        //Pressing the resetButton will start the game from the beginning of the round
        if (e.getSource() == resetButton){
            game.clearGame(false);
            game.startGame();
        }

        //Pressing the undoButton will undo the last piece placed on the board
        if (e.getSource() == undoButton){
            game.getBoard().undoPlace();
        }

        //Pressing the againButton will clear and start the game and show the duringgame panel
        if (e.getSource() == againButton){
            changeCard2();
            game.clearGame(false);
            game.startGame();
        }

        //Pressing the menuButton will clear the game and show the pregame panel
        if (e.getSource() == menuButton1 || e.getSource() == menuButton2){
            game.clearGame(true);
            changeCard1();
        }

        //Pressing either helpButton will show the help panel
        if (e.getSource() == helpButton1 || e.getSource() == helpButton2){
            changeCard3();
        }

        //Pressing the backButton will show the previous panel
        if (e.getSource() == backButton){
            changePrevious();
        }

        //Pressing the versusButton will toggle between playing against a player or computer
        if (e.getSource() == versusButton){
            if (isVersusAI){
                isVersusAI = false;
                updateVersusButton();
            }
            else{
                isVersusAI = true;
                updateVersusButton();
            }
        }

        //Pressing the historyButton will toggle between showing or not showing the history
        if(e.getSource() == historyButton){
            if (showHistory){
                showHistory = false;
            }
            else{
                showHistory = true;
            }
            updateHistoryButton();
            game.getBoard().setShowHistory(showHistory);
        }

    }

}
