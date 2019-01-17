/*
 * Frank Chen
 * SplashScreenPanel.java
 * Initializes and adds functionality to the Piece object
 * ICS4U1
 * November 28, 2018
 */
package com.company;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Piece {

    //Constant
    private final String COLOR;

    //Initialize the default variables relating to the size
    //The dimensions of the board, e.g. 15x15
    int size = 15;
    //The size of each square in the grid
    int gridSize = 450/size;
    //Location of the last line of the board
    int boardEnd = 30+((size-1)*gridSize);
    //i.e half of the gridSize
    int offset = gridSize/2;
    //The diameter of the piece, 86.67% of the gridsize
    private int diameter = gridSize*13/15;
    private int radius = diameter/2;

    //Declare variables and objects
    private GamePanel game;
    private Board board;
    private Color pieceColor, otherPieceColor;
    private int[][][] grid = new int[2][size][size];
    private int mouseX, mouseY;
    private boolean mouseClick;

    //Initialize variables and objects
    public Piece(GamePanel game,Board board,String color){
        this.game = game;
        this.board = board;
        COLOR = color;
        initializeColor();
        initializeGrid();
    }

    //Allows the object to draw on the gamePanel
    //Draws the mouse hover preview piece and the placed pieces
    /**
     * Draws the mouse hover preview piece, placed pieces, and history
     * pre: none
     * post: The preview piece, placed pieces, and history are drawn
     */
    public void paint(Graphics2D g){
        //draws the previewed pieces
        prePlacement(g);
        drawPlaced(g);
        drawHistory(g);
    }
    //
    /**
     * Draws the placed pieces
     * pre: none
     * post: The placed pieces are drawn
     */
    public void drawPlaced(Graphics2D g){
        //Scans every point on the board from left to right, top to bottom
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                //If a piece is placed at a point, draw it
                if (board.isPlaced(COLOR, r,c)){
                    g.setColor(pieceColor);
                    g.fillOval(grid[0][r][c]-radius,grid[1][r][c]-radius,diameter,diameter);
                }
            }
        }
    }

    /**
     * Displays the order that the pieces are placed in
     * pre: none
     * post: The order of pieces played is displayed
     */
    public void drawHistory(Graphics2D g){
        //Scans every point on the board from left to right, top to bottom
        if (board.getShowHistory()){
            for (int r = 0; r < size; r++){
                for (int c = 0; c < size; c++){

                    //If a piece is placed at a point, draw the history number on it
                    if (board.isPlaced(COLOR, r,c)){
                        g.setFont(new Font("Arial", Font.PLAIN, radius));
                        g.setColor(otherPieceColor);
                        g.drawString(""+ board.getHistory()[r][c], grid[0][r][c]-(radius/2), grid[1][r][c]+(radius/2));
                    }
                }
            }
        }
    }
    /**
     * Keeps track of whether the mouse is on the board or not, and whether the mouse is interacting with the grid
     * pre: none
     * post: If the mouse is hovering over an intersection on the grid, a translucent preview piece is drawn at
     * that point. If the mouse is hovering over a point and clicks, a piece is then placed that at point
     */
    private void prePlacement (Graphics2D g){
        boolean withinBoard;
        int hoverX = 0, hoverY = 0;
        int alpha = 0;

        //Checks to see if the mouse is within the bounds of the board
        if (mouseX >= offset && mouseX <= boardEnd+offset && mouseY >= offset && mouseY <= boardEnd+offset) {
            withinBoard = true;
        }
        else {
            withinBoard = false;
            mouseClick = false;
            clearHover();
        }

        if (withinBoard){
            //top to bottom, left to right scan of every point in the grid
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {

                    //Gets the x and y value of the point
                    int gridX = grid[0][r][c];
                    int gridY = grid[1][r][c];

                    //If the mouse is clicked, a piece is placed in the point within the bounds of that intersection
                    if (mouseClick && mouseX >= gridX-offset && mouseX <= gridX+offset && mouseY >= gridY-offset
                            && mouseY <= gridY+offset){
                        mouseClick = false;
                        board.place(COLOR,r,c);
                    }
                    //If the mouse is hovered within the bounds of a point, a translucent piece is shown temporarily
                    //while the mouse is still at that location
                    else if (mouseX >= gridX-offset && mouseX <= gridX+offset && mouseY >= gridY-offset && mouseY <= gridY+offset){
                        hoverX = gridX-radius;
                        hoverY = gridY-radius;
                        alpha = 100;
                    }

                }
            }

        }

        //Draws the preview piece
        g.setColor(new Color(pieceColor.getRed(),pieceColor.getGreen(),pieceColor.getBlue(),alpha));
        g.fillOval(hoverX,hoverY,diameter,diameter);

        game.repaint();
    }

    /**
     * Sets the color of this object
     * pre: none
     * post: The color of this object is set to either "white" or "black"
     */
    private void initializeColor(){
        if (COLOR.equals("black")){
            pieceColor = Color.BLACK;
            otherPieceColor = Color.WHITE;
        }
        else if (COLOR.equals("white")){
            pieceColor = Color.WHITE;
            otherPieceColor = Color.BLACK;
        }
    }

    /**
     * Initializes the x and y value for each point in the grid using a 3d array
     * [a][b][c], a is the x and y value (0 and 1 respectively), b is the row number, and c is the column number
     * pre: none
     * post: The x and y value in the 3d array is initialized
     */
    private void initializeGrid(){

        //Scans every point left to right, top to bottom
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                //column = c, r = row
                grid[0][r][c] = 30 + (c*gridSize);
                grid[1][r][c] = 30 + (r*gridSize);
            }
        }

    }

    /**
     * Removes the phantom piece
     * pre: none
     * post: The phantom piece is removed by changing the mouse location
     */
    public void clearHover(){
        mouseX = 0;
        mouseY = 0;
    }
    //SETTER METHODS
    /**
     * Updates all variables relating to the size and reinitializes the grid
     * pre: size > 0
     * post: All the variables relating to the size are updated and the grid is reinitialized
     */
    public void setSize(int size){
        this.size = size;
        gridSize = 450/size;
        boardEnd = 30+((size-1)*gridSize);
        offset = gridSize/2;
        diameter = gridSize*13/15;
        radius = diameter/2;
        grid = new int[2][size][size];
        initializeGrid();
    }
    //Updates the mouse location
    public void setMouseLocation(int mouseX, int mouseY){
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
    public void setMouseClick(MouseEvent e){
        mouseClick = true;
    }


}
