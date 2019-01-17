/*
 * Frank Chen
 * SplashScreenPanel.java
 * Initializes and adds functionality to the Board object
 * ICS4U1
 * November 28, 2018
 */
package com.company;

import java.awt.*;

public class Board {

    //Constants
    private final String BLACK = "black";
    private final String WHITE = "white";

    //Declare GamePanel object
    private GamePanel game;

    //Variables relating to the size
    int size = 15;
    int gridSize = 450/size;
    int boardEnd = 30+((size-1)*gridSize);

    //2d array that keeps track of the state of the board, i.e. knows where every piece is
    //empty = 0, black = 1, white = 2;
    int[][] state = new int[size][size];
    //2d array that keeps track of the order in which the pieces are played
    int[][] history = new int[size][size];
    //Counter tallying up the amount of pieces played
    int piecesPlaced = 0;
    //Variable that determines if there is a win
    int winCount;
    boolean showHistory;

    //Instantiates the GamePanel class
    public Board(GamePanel game){
        this.game = game;
    }

    /**
     * Draws the board
     * pre: none
     * post: The board is drawn
     */
    public void paint(Graphics2D g){
        //Draws the background
        g.setColor(new Color(247,200,118));
        g.fillRect(0,0,480,480);
        //Draws the grid lines based on the size
        g.setColor(new Color(40,40,40));
        for (int i = 0; i < size; i++){
            g.drawLine(30+(i*gridSize),30,30+(i*gridSize),boardEnd);
            g.drawLine(30,30+(i*gridSize),boardEnd,30+(i*gridSize));
        }
        //Draws the marker dots when the board size is 15
        if (size == 15) {
            g.fillOval(237, 237, 6, 6);
            g.fillOval(117, 117, 6, 6);
            g.fillOval(357, 117, 6, 6);
            g.fillOval(117, 357, 6, 6);
            g.fillOval(357, 357, 6, 6);
        }
    }

    /**
     * Places a piece on the board
     * pre: color is "black" or "white", row and column are > 0 and < size - 1
     * post: A piece is placed on the board
     */
    public void place(String color, int row, int column){

        //If the space is empty, update piecesPlayed and the history
        if (state[row][column]==0) {
            piecesPlaced++;
            history[row][column] = piecesPlaced;
        }

        //If the space is empty and the piece being placed is black, update state, check for win, and change the turn
        if (state[row][column] == 0 && color.equals(BLACK)) {
            state[row][column] = 1;
            checkWin(color);
            game.changeTurn();
        }
        //If the space is empty and the piece being placed is white, update state, check for win, and change the turn
        else if (state[row][column] == 0 && color.equals(WHITE)){
            state[row][column] = 2;
            checkWin(color);
            game.changeTurn();
        }

    }

    //If a specific piece has been played at the point, return true
    /**
     * If a specific piece has been played at the point, return true
     * pre: color is "black" or "white", row and column are > 0 and < size - 1
     * post: True is returned if a specific piece has been played at a point, return false otherwise
     */
    public boolean isPlaced(String color,int row, int column){
        if (state[row][column] == 1 && color.equals(BLACK)){
            return true;
        }
        else if (state[row][column] == 2 && color.equals(WHITE)){
            return true;
        }
        return false;
    }

    //Removes the piece last played using the history
    /**
     * Removes the last piece played
     * pre: none
     * post: The last piece played is removed
     */
    public void undoPlace(){
        //Scans every point from left to right, top to bottom
        for (int r = 0, b = 0; r < size && b == 0; r++){
            for (int c = 0; c < size && b == 0; c++){

                //Find the most recent piece played and updates the piecesPlaced removes it from the history
                //and and state, changes the turn, and then breaks from the loop
                if (history[r][c] == piecesPlaced && piecesPlaced > 0){
                    piecesPlaced--;
                    history[r][c] = 0;
                    state[r][c] = 0;
                    game.changeTurn(true);
                    game.repaint();
                    b=1;
                }

            }
        }
    }

    /**
     * Resets the board to how it was during the start
     * pre: none
     * post: The board has been reset
     */
    public void clear(){
        //Clears/resets the state and history, by scanning each point from left to right, top to bottom
        for (int r = 0; r < size; r++){
            for (int c = 0; c < size; c++){
                state[r][c] = 0;
                history[r][c] = 0;
            }
        }
        //Clears the piecesPlaced
        piecesPlaced = 0;
    }

    /**
     * If the board is completely empty, return true
     * pre: none
     * post: True is returned if the board is completely empty, false otherwise
     */
    public boolean isEmpty(){
        boolean empty = true;

        for (int r = 0; r < size; r++){
            for (int c = 0; c < size; c++){
                if (state[r][c] != 0){
                    empty = false;
                }
            }
        }
        return empty;
    }

    //Checks to see if there is a winner
    /**
     * Checks to see if there has been a win
     * pre: color is "black" or "white"
     * post: Every grid on the board is check to see if there is a row of 5
     */
    private void checkWin(String color){
        winCount = 0;
        int value = 0;

        //Black is 1 and white is 2
        if (color.equals(BLACK)){
            value = 1;
        }
        else if (color.equals(WHITE)){
            value = 2;
        }

        //Checks for a line of 5 in every direction, if found, wincount is increased by one
        checkHorizontal(value);
        checkVertical(value);
        checkForwardDiagonal(value);
        checkBackwardDiagonal(value);

        //Makes sure there is only one win
        if (winCount>0){
            game.gameWin(color);
        }

    }

    /**
     * Checks for a horizontal row of 5
     * pre: value is 1 or 2
     * post: The wincount is increased if a horizontal row of 5 is found
     */
    private void checkHorizontal(int value){

        //Scans the board from left to right, top to bottom
        //Not every point is scanned as r and c are anchor points
        for (int r = 0; r < size; r++){
            for (int c = 0; c < size-4; c++){

                //Checks to see if the 4 pieces right of the anchor point are the same color
                if (state[r][c] == value && state[r][c+1] == value && state[r][c+2] == value
                        && state[r][c+3] == value && state[r][c+4] == value){
                    winCount++;
                }

            }

        }
    }

    /**
     * Checks for a vertical row of 5
     * pre: value is 1 or 2
     * post: The wincount is increased if a vertical row of 5 is found
     */
    private void checkVertical(int value){

        //Scans the board from left to right, top to bottom
        for (int r = 0; r < size-4; r++){
            for (int c = 0; c < size; c++){

                //Checks to see if the 4 pieces below the anchor point are the same color
                if (state[r][c] == value && state[r+1][c] == value && state[r+2][c] == value
                        && state[r+3][c] == value && state[r+4][c] == value){
                    winCount++;
                }

            }

        }
    }

    /**
     * Checks for a forward diagonal row of five
     * pre: value is 1 or 2
     * post: The wincount is increased if a forward diagonal row of 5 is found
     */
    private void checkForwardDiagonal(int value){
        //Scans the board from left to right, top to bottom
        for (int r = 4; r < size; r++){
            for (int c = 0; c < size-4; c++){

                //Checks to see if the 4 pieces northeast of the anchor point are the same color
                if (state[r][c] == value && state[r-1][c+1] == value && state[r-2][c+2] == value
                        && state[r-3][c+3] == value && state[r-4][c+4] == value){
                    winCount++;
                }
            }
        }
    }

    /**
     * Checks for a backward diagonal row of five
     * pre: value is 1 or 2
     * post: The wincount is increased if a backward diagonal row of 5 is found
     */
    private void checkBackwardDiagonal(int value){

        //Scans the board from left to right, top to bottom
        for (int r = 0; r < size-4; r++){
            for (int c = 0; c < size-4; c++){

                //Checks to see if the 4 pieces southeast of the anchor point are the same color
                if (state[r][c] == value && state[r+1][c+1] == value && state[r+2][c+2] == value
                        && state[r+3][c+3] == value && state[r+4][c+4] == value){
                    winCount++;
                }
            }
        }
    }
    /**
     * Updates all variables relating to the size
     * pre: size > 0
     * post: All the variables relating to the size are updated
     */
    public void setSize(int size){
        this.size = size;
        gridSize = 450/size;
        boardEnd = 30+((size-1)*gridSize);
        state = new int[size][size];
        history = new int[size][size];
    }
    //SETTER AND GETTER METHODS
    public void setShowHistory(boolean showHistory){
        this.showHistory = showHistory;
    }
    public boolean getShowHistory(){
        return showHistory;
    }
    public int getSize(){
        return size;
    }
    public int[][] getState(){
        return state;
    }
    public int getPiecesPlaced(){
        return piecesPlaced;
    }
    public int[][] getHistory(){
        return history;
    }

}
