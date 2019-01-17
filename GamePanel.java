/*
 * Frank Chen
 * GamePanel.java
 * Initializes and adds functionality to the GamePanel object
 * ICS4U1
 * November 28, 2018
 */
package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

    private final String BLACK = "black";
    private final String WHITE = "white";

    //Declare variables and objects
    private Board board;
    private Piece blackPiece, whitePiece;
    private SidePanel side;
    private GameAI ai;
    private boolean blackTurn, whiteTurn;
    private boolean isVersusAI;
    private String lastWon = "";
    private int blackWins, whiteWins;

    //Instantiates classes
    public GamePanel(){
        board = new Board(this);
        blackPiece = new Piece(this,board,BLACK);
        whitePiece = new Piece(this, board, WHITE);
        ai = new GameAI(board,WHITE);
    }

    /**
     * Starts the game and initializes the input depending on whether the user is facing the computer
     * pre: none
     * post: The game is started and the input is initialized
     */
    public void run(){
        this.isVersusAI = side.getIsVersusAI();
        startGame();
        initializeInput();
    }

    /**
     * Starts the game
     * pre: none
     * post: whiteTurn or blackTurn is set to true
     */
    public void startGame(){

        //black starts the first game, the loser starts the next
        if (lastWon.isEmpty() || lastWon.equals(WHITE)){
            blackTurn = true;
        }
        else if (lastWon.equals(BLACK)){
            whiteTurn = true;
            if (isVersusAI){
                ai.firstMove();
            }
        }
        repaint();
    }

    /**
     * Adds mouse input and disables mouse input during the ai's turn
     * pre: none
     * post: Mouse input when moving, dragging, or releasing added
     */
    private void initializeInput(){

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (blackTurn) {
                    blackPiece.setMouseClick(e);
                }
                else if (whiteTurn && !isVersusAI){
                    whitePiece.setMouseClick(e);
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (blackTurn) {
                    blackPiece.setMouseLocation(e.getX(), e.getY());
                }
                else if (whiteTurn && !isVersusAI){
                    whitePiece.setMouseLocation(e.getX(),e.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (blackTurn) {
                    blackPiece.setMouseLocation(e.getX(), e.getY());
                }
                else if (whiteTurn && !isVersusAI){
                    whitePiece.setMouseLocation(e.getX(),e.getY());
                }
            }
        });
    }

    //Switches blackTurn to false and whiteTurn to true and vice versa
    //Also tells the ai to make a move
    /**
     * Switches blackTurn to false and whiteTurn to true if blackTurn is true, and vice versa
     * Also tells the ai to make a move
     * pre: none
     * post: blackTurn and whiteTurn are updated, the ai moves
     */
    public void changeTurn(){
        if (blackTurn){
            blackTurn = false;
            whiteTurn = true;
            blackPiece.clearHover();
            if (isVersusAI){
                ai.makeMove();
            }
        }
        else if (whiteTurn){
            whiteTurn = false;
            blackTurn = true;
            whitePiece.clearHover();
        }

    }

    //Overloaded version of the method that tells the ai to make a move if the board is empty
    public void changeTurn(boolean isUndoing){
        if (blackTurn){
            blackTurn = false;
            whiteTurn = true;
            blackPiece.clearHover();

            if (isVersusAI && isUndoing && board.isEmpty()){
                ai.firstMove();
            }
        }
        else if (whiteTurn){
            whiteTurn = false;
            blackTurn = true;
            whitePiece.clearHover();
        }

    }

    /**
     * Passes the size variable for use in other classes
     * pre: size > 0
     * post: The size variable is passed
     */
    public void setSize(int size){
        board.setSize(size);
        blackPiece.setSize(size);
        whitePiece.setSize(size);
        repaint();
    }

    //Disables gameplay and clears the board
    //Also clears the game stats based on the parameter

    /**
     * Disables gameplay and clears the board and clears the game stats based on the parameter
     * pre: none
     * post: The gameplay is disable, the board is cleared, and the game stats are cleared if clearGameStats is true
     */
    public void clearGame(boolean clearGameStats){
        endGame();
        board.clear();
        blackPiece.clearHover();
        whitePiece.clearHover();
        repaint();
        if (clearGameStats){
            blackWins = 0;
            whiteWins = 0;
            side.updateScoreLabel(blackWins, whiteWins);
            lastWon = "";
        }

    }

    /**
     * Ends the game and updates winner information
     * pre: color is "black" or "white"
     * post: The game is ended, the winner is displayed, the total amount of wins for either side is updated, and the
     * color that gets the first move for the next game is set
     */
    public void gameWin(String color){
       endGame();
       side.showGameWin(color);

       if (color.equals(BLACK)){
           blackWins++;
       }
       else if (color.equals(WHITE)){
           whiteWins++;
       }
       side.updateScoreLabel(blackWins, whiteWins);
       lastWon = color;
    }

    /**
     * Stops the game
     * pre: none
     * post: blackTurn and whiteTurn are set to false, neither side will be able to make a move
     */
    private void endGame(){
        blackTurn = false;
        whiteTurn = false;
    }

    /**
     * Draws the graphics on the panel
     * pre: none
     * post: The board and the pieces are drawn
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;

        //Makes the graphics look smoother
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        board.paint(g2d);
        blackPiece.paint(g2d);
        whitePiece.paint(g2d);
    }

    //Getters and setters
    public void setSidePanel(SidePanel side){
        this.side = side;
    }
    public Board getBoard(){
        return board;
    }
    public SidePanel getSide(){return side;}

}
