/*
 * Frank Chen
 * GameAI.java
 * Initializes and adds functionality for the game's ai
 * ICS4U1
 * November 28, 2018
 */
package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameAI {

    //Declare variables and objects
    private Board board;
    private String color;
    private Random random;
    //Array containing the points of where the piece could be placed
    private List<int[]> openSpaces;
    //board state
    private int[][] s;
    private int size;
    //value of color in the board state array, black = 1 and white = 2
    private int b = 1, w = 2;

    //Initialize variables and instantiate classes
    public GameAI(Board board, String color){
        openSpaces = new ArrayList<int[]>();
        this.board = board;
        this.color = color;
        random = new Random();
        s = board.getState();
        size = board.getSize();
    }

    /**
     * The ai decides where to place the piece
     * pre: none
     * post: Where to place the piece is decided
     */
    public void makeMove(){
        //updates the state of the board
        update();

        //Places the first piece near the center
        if (nonePlaced()){
            int center = (board.getSize()/2);

            int r = randomNumber(-1,1)+center;
            int c = randomNumber(-1,1)+center;
            while(s[r][c] != 0){
                r = randomNumber(-1,1)+center;
                c = randomNumber(-1,1)+center;
            }
            board.place(color,r,c);
        }
        //If the ai has a row of 4, a piece is placed extend the row
        else if (checkAllClosed(4,w) || checkAllBroken(4,w) || checkFourEdge(w)){
            placeOpen();
        }
        //If the player has a row of 4, a piece is placed to block the row
        else if (checkAllClosed(4,b) || checkAllBroken(4,b) || checkFourEdge(b)){
            placeOpen();
        }
        //If the ai has a row of 3, a piece is placed extend the row
        else if (checkAllOpen(3,w) || checkAllBroken(3,w)){
            placeOpen();
        }
        //If the player has a row of 3, a piece is placed to block the row
        else if (checkAllOpen(3,b) || checkAllBroken(3,b)){
            placeOpen();
        }
        //If the ai has a combo (two 3's together or one 3 and one 4 together), a piece is placed to join the lines
        else if (checkCombo(w)){
            placeOpen();
        }
        //If the player has a combo, a piece is placed to block it
        else if (checkCombo(b)){
            placeOpen();
        }
        //If the ai has a row of 2, a piece is placed extend the row
        else if (checkAllOpen(2,w)){
            placeOpen();
        }
        //If the ai has a row of 2, a piece is placed extend the row
        else if (checkAllBroken(2,w)){
            placeOpen();
        }
        //If the player has a row of 2, a piece is placed to block the row
        else if (checkAllOpen(2,b)){
            placeOpen();
        }
        //If the player has a row of 2, a piece is placed to block the row
        else if (checkAllBroken(2,b)){
            placeOpen();
        }
        //If the ai has a closed row of 3, a piece is placed to extend the row
        //else if (checkAllClosed(3,w)){
        //    placeOpen();
        //}
        //If the ai has a row of 1, a piece is placed extend the row
        else if (checkAllOpen(1,w)){
            placeOpen();
        }
        //If none of the conditions are met, a piece is placed randomly
        else {
            int r = random.nextInt(board.getSize());
            int c = random.nextInt(board.getSize());
            while (s[r][c] != 0) {
                r = random.nextInt(board.getSize());
                c = random.nextInt(board.getSize());
            }
            board.place(color, r, c);
        }
    }

    /**
     * The ai places a piece at the center of the board
     * pre: none
     * post: A piece is placed at the center of the board
     */
    public void firstMove(){
        int center = (board.getSize()/2);
        board.place(color, center, center);
    }

    /**
     * Generates a random number within a range and returns it
     * pre: The end number is larger than the start number
     * post: A random number is generated and returned
     */
    private int randomNumber(int start, int end){
        int dif = end - start;
        int num = random.nextInt(dif+1)+start;
        return num;
    }

    /**
     * The ai places a piece randomly from a list of approved spaces
     * pre: none
     * post: A piece is placed on the board
     */
    public void placeOpen(){
        int index = randomNumber(0,openSpaces.size()-1);
        int space[] = openSpaces.get(index);
        board.place(color,space[0],space[1]);
        openSpaces.clear();
    }

    /**
     * Returns whether or not a piece has been played by the ai
     * pre: none
     * post: True is returned if the ai has placed no pieces and false is returned if the ai place placed a piece
     */
    public boolean nonePlaced(){
        boolean placed = true;

        for (int r = 0; r < size; r++){
            for (int c = 0; c < size; c++){
                if (s[r][c] == w){
                    placed = false;
                }
            }
        }
        return placed;
    }

    /**
     * Updates the value of the board state variable (s) and the board size variable (size)
     * pre: none
     * post: The board size and board state are updated
     */
    public void update(){
        size = board.getSize();
        s = board.getState();
    }

    /**
     * Returns whether or not there is a line of l spaces of cv color that is open on both ends
     * pre: l > 0 and cv == 1 || cv == 2
     * post: True is returned if there is an appropriate line, false is return if there is no appropriate line
     */
    private boolean checkAllOpen(int l, int cv){
        checkHoriOpen(l,cv);
        checkVertOpen(l,cv);
        checkForwardDiagOpen(l,cv);
        checkBackwardDiagOpen(l,cv);
        if (openSpaces.size() > 0){
            return true;
        }
        return false;

    }

    /**
     * Returns whether or not there is a line of 4 spaces of cv color that is at the edge of the board
     * pre:cv == 1 || cv == 2
     * post: True is returned if there is an appropriate line, false is return if there is no appropriate line
     */
    private boolean checkFourEdge(int cv){
        checkHoriEdgeFour(cv);
        checkVertEdgeFour(cv);
        checkForwardDiagEdgeFour(cv);
        checkBackwardDiagEdgeFour(cv);

        if (openSpaces.size() > 0){
            return true;
        }
        return false;

    }

    /**
     * Returns whether or not there is a line of l spaces of cv color that is open on one end
     * pre: l > 0 and cv == 1 || cv == 2
     * post: True is returned if there is an appropriate line, false is return if there is no appropriate line
     */
    private boolean checkAllClosed(int l, int cv){
        checkHoriClosed(l,cv);
        checkVertClosed(l,cv);
        checkForwardDiagClosed(l,cv);
        checkBackwardDiagClosed(l,cv);

        if (openSpaces.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * Returns whether or not there are 2 connecting lines of 3/4
     * pre: cv == 1 || cv == 2
     * post: True is returned if there is an appropriate line, false is return if there is no appropriate line
     */
    private boolean checkCombo(int cv){
        //Checks for open 2 lines
        checkHoriComponents(cv);
        checkVertComponents(cv);
        checkForwardDiagComponents(cv);
        checkBackwardDiagComponents(cv);

        //Checks for closed 3 lines
        checkHoriClosed(3,cv);
        checkVertClosed(3,cv);
        checkForwardDiagClosed(3,cv);
        checkBackwardDiagClosed(3,cv);

        //Checks for closed broken 3 lines
        checkHoriBroken(3,cv,false);
        checkVertBroken(3,cv,false);
        checkForwardDiagBroken(3,cv,false);
        checkBackwardDiagBroken(3,cv,false);

        List<int[]>comboSpaces = new ArrayList<int[]>();

        //Checks to see if any of the lines stem from the same point, if so, add it to the array
        for (int i = 0; i < openSpaces.size(); i++){
            for (int j = 0; j < openSpaces.size(); j++){
                if (i !=j && openSpaces.get(i)[0] == openSpaces.get(j)[0] && openSpaces.get(i)[1] == openSpaces.get(j)[1]){
                    comboSpaces.add(new int[]{openSpaces.get(i)[0],openSpaces.get(i)[1]});
                }
            }
        }

        //Sets openSpaces equal to comboSpaces, clear comboSpaces after
        openSpaces.clear();
        openSpaces = new ArrayList<int[]>(comboSpaces);
        comboSpaces.clear();

        if (openSpaces.size() > 0){
            return true;
        }
        return false;

    }

    /**
     * Returns whether or not there is a line of l spaces of cv color with a gap
     * pre: l > 0 and cv == 1 || cv == 2
     * post: True is returned if there is an appropriate line, false is return if there is no appropriate line
     */
    private boolean checkAllBroken(int l, int cv){
        checkHoriBroken(l,cv,true);
        checkVertBroken(l,cv,true);
        checkForwardDiagBroken(l,cv,true);
        checkBackwardDiagBroken(l,cv,true);

        if (openSpaces.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * Checks to see if there is a horizontal line of l spaces of cv color that is open on both sides,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are is added to an array when there is an appropriate line
     */
    private void checkHoriOpen(int l, int cv){

        for (int r = 0; r < size; r++){
            for (int c = 0; c < size-1-l; c++) {

                boolean check = true;

                //checks middle
                for (int i = 0; i < l; i++) {
                    if (s[r][c + 1 + i] != cv) {
                        check = false;
                    }
                }
                if (check) {
                    if (l == 1 && (s[r][c] == 0 && s[r][c + 1 + l] == 0)) {
                        openSpaces.add(new int[]{r, c});
                        openSpaces.add(new int[]{r, c + l + 1});
                    }
                    //checks to see if one end has 2 empty spaces and if the other end has 1 space
                    else {
                        if (c > 0 && (s[r][c - 1] == 0 && s[r][c] == 0 && s[r][c + 1 + l] == 0)) {
                            openSpaces.add(new int[]{r, c});
                        }
                        if (c < size - 2 - l && (s[r][c] == 0 && s[r][c + 1 + l] == 0 && s[r][c + 2 + l] == 0)) {
                            openSpaces.add(new int[]{r, c + 1 + l});
                        }
                    }
                }
            }
        }
    }
    /**
     * Checks to see if there is a vertical line of l spaces of cv color that is open on both sides,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are is added to an array when there is an appropriate line
     */
    private void checkVertOpen(int l, int cv){
        for (int r = 0; r < size-1-l; r++){
            for (int c = 0; c < size; c++){

                boolean check = true;

                //checks middle
                for (int i = 0; i < l; i++){
                    if (s[r+1+i][c]!=cv){
                        check = false;
                    }
                }

                if (check) {
                    if (l == 1 && (s[r][c] == 0 && s[r + 1 + l][c] == 0)) {
                        openSpaces.add(new int[]{r, c});
                        openSpaces.add(new int[]{r + l + 1, c});
                    }
                    //checks to see if one end has 2 empty spaces and if the other end has 1 space
                    else {
                        if (r > 0 && (s[r - 1][c] == 0 && s[r][c] == 0 && s[r + 1 + l][c] == 0)) {
                            openSpaces.add(new int[]{r, c});
                        }
                        if (r < size - 2 - l && (s[r][c] == 0 && s[r + 1 + l][c] == 0 && s[r + 2 + l][c] == 0)) {
                            openSpaces.add(new int[]{r + 1 + l, c});
                        }
                    }
                }
            }
        }
    }
    /**
     * Checks to see if there is a forward diagonal line of l spaces of cv color that is open on both sides,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkForwardDiagOpen(int l, int cv){
        for (int r = l+1; r < size; r++){
            for (int c = 0; c < size-1-l; c++) {

                boolean check = true;

                //checks middle
                for (int i = 0; i < l; i++) {
                    if (s[r - 1 - i][c + 1 + i] != cv) {
                        check = false;
                    }
                }
                if (check) {
                    if (l == 1 && (s[r][c] == 0 && s[r - 1 - l][c + 1 + l] == 0)) {
                            openSpaces.add(new int[]{r, c});
                            openSpaces.add(new int[]{r - 1 - l, c + 1 + l});
                    }
                    else {
                        if (c > 0 && r < size - 1 && (s[r + 1][c - 1] == 0 && s[r][c] == 0 && s[r - 1 - l][c + 1 + l] == 0)) {
                            openSpaces.add(new int[]{r, c});
                        }
                        if (c < size - 2 - l && r < l + 1 && (s[r][c] == 0 && s[r - 1 - l][c + 1 + l] == 0 && s[r - 2 - l][c + 2 + l] == 0)) {
                            openSpaces.add(new int[]{r - 1 - l, c + 1 + l});
                        }
                    }

                }
            }
        }
    }
    /**
     * Checks to see if there is a backward diagonal line of l spaces of cv color that is open on both sides,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkBackwardDiagOpen(int l, int cv){
        for (int r = 0; r < size-(l+1); r++){
            for (int c = 0; c < size-(l+1); c++){

                boolean check = true;

                //checks middle
                for (int i = 0; i < l; i++){
                    if (s[r+1+i][c+1+i]!=cv){
                        check = false;
                    }
                }

                if (check) {
                    if (l == 1 && (s[r][c] == 0 && s[r + 1 + l][c + 1 + l] == 0)) {
                        openSpaces.add(new int[]{r,c});
                        openSpaces.add(new int[]{r+1+l,c+1+l});
                    }
                    else {
                        if (c > 0 && r > 0 && (s[r - 1][c - 1] == 0 && s[r][c] == 0 && s[r + 1 + l][c + 1 + l] == 0)) {
                            openSpaces.add(new int[]{r, c});
                        }
                        if (c < size - 2 - l && r < size - 2 - l && (s[r][c] == 0 && s[r + 1 + l][c + 1 + l] == 0 && s[r + 2 + l][c + 2 + l] == 0)) {
                            openSpaces.add(new int[]{r + 1 + l, c + 1 + l});
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks to see if there is a horizontal line of l spaces of cv color with a gap,
     * adds spaces that complete the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkHoriBroken(int l, int cv, boolean isOpen){

        //Sets opposite color value
        int ocv = 0;
        if (cv == w)
            ocv = b;
        else if (cv == b)
            ocv = w;

        for (int r = 0; r < size; r++){
            for (int c = 0; c < size-2-l; c++){

                boolean check = true;
                int broken = 0;

                //Checks the center
                for (int i = 0; i < l+1; i++){
                    if (s[r][c+1+i]==ocv){
                        check = false;
                    }
                    else if (s[r][c+1+i]==0){
                        broken++;
                        openSpaces.add(new int[]{r,c+1+i});
                    }
                }

                //check if the ends are empty
                if (isOpen && l != 4 && (s[r][c] != 0 || s[r][c+2+l] != 0)){
                    check = false;
                }

                if (!(check && broken == 1)){
                    for (int i = 0; i < broken; i++){
                        openSpaces.remove(openSpaces.size()-1);
                    }
                }
            }
        }
    }
    /**
     * Checks to see if there is a vertical line of l spaces of cv color with a gap,
     * adds spaces that complete the line to an array
     * pre: none
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkVertBroken(int l,int cv, boolean isOpen){

        //Sets opposite color value
        int ocv = 0;
        if (cv == w)
            ocv = b;
        else if (cv == b)
            ocv = w;

        for (int r = 0; r < size-l-2; r++){
            for (int c = 0; c < size; c++){

                boolean check = true;
                int broken = 0;

                //Checks the enter 4
                for (int i = 0; i < l+1; i++){
                    if (s[r+1+i][c]==ocv){
                        check = false;
                    }
                    else if (s[r+1+i][c]==0){
                        broken++;
                        openSpaces.add(new int[]{r+1+i,c});
                    }
                }
                //check if the ends are empty
                if (isOpen && l != 4 && (s[r][c] != 0 || s[r+2+l][c] != 0)){
                    check = false;
                }

                if (!(check && broken == 1)){
                    for (int i = 0; i < broken; i++){
                        openSpaces.remove(openSpaces.size()-1);
                    }
                }
            }
        }
    }
    /**
     * Checks to see if there is a forward diagonal line of l spaces of cv color with a gap,
     * adds spaces that complete the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkForwardDiagBroken(int l, int cv,boolean isOpen){

        //Sets opposite color value
        int ocv = 0;
        if (cv == w)
            ocv = b;
        else if (cv == b)
            ocv = w;

        for (int r = l+2; r < size; r++){
            for (int c = 0; c < size-l-2; c++){

                boolean check = true;
                int broken = 0;

                //Checks the enter 4
                for (int i = 0; i < l+1; i++){
                    if (s[r-1-i][c+1+i]==ocv){
                        check = false;
                    }
                    else if (s[r-1-i][c+1+i]==0){
                        broken++;
                        openSpaces.add(new int[]{r-1-i,c+1+i});
                    }
                }
                //check if the ends are empty
                if (isOpen && l != 4 && (s[r][c] != 0 || s[r-2-l][c+2+l] != 0)){
                    check = false;
                }

                if (!(check && broken == 1)){
                    for (int i = 0; i < broken; i++){
                        openSpaces.remove(openSpaces.size()-1);
                    }
                }
            }
        }
    }
    /**
     * Checks to see if there is a backward diagonal line of l spaces of cv color with a gap,
     * adds spaces that complete the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkBackwardDiagBroken(int l, int cv,boolean isOpen){

        //Sets opposite color value
        int ocv = 0;
        if (cv == w)
            ocv = b;
        else if (cv == b)
            ocv = w;

        for (int r = 0; r < size-2-l; r++){
            for (int c = 0; c < size-2-l; c++){

                boolean check = true;
                int broken = 0;

                //Checks the enter 4
                for (int i = 0; i < l+1; i++){
                    if (s[r+1+i][c+1+i]==ocv){
                        check = false;
                    }
                    else if (s[r+1+i][c+1+i]==0){
                        broken++;
                        openSpaces.add(new int[]{r+1+i,c+1+i});
                    }
                }
                //check if the ends are empty
                if (isOpen && l != 4 && (s[r][c] != 0 || s[r+2+l][c+2+l] != 0)){
                    check = false;
                }

                if (!(check && broken == 1)){
                    for (int i = 0; i < broken; i++){
                        openSpaces.remove(openSpaces.size()-1);
                    }
                }
            }
        }
    }

    /**
     * Checks to see if there is a horizontal line of l spaces of cv color that is on the edge of the board,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkHoriEdgeFour(int cv){
        for (int r = 0; r < size; r++){

            int check1 = 0, broken1 = 0;
            int check2 = 0, broken2 = 0;

            for (int i = 0; i < 5; i++){
                if (s[r][i] == cv){
                    check1++;
                }
                else if (s[r][i] == 0){
                    broken1++;
                    openSpaces.add(new int[]{r,i});
                }
            }
            filterOpenSpaces(check1,broken1);

            for (int i = 0; i < 5; i++){
                if (s[r][size-1-i] == cv){
                    check2++;
                }
                else if (s[r][size-1-i] == 0){
                    broken2++;
                    openSpaces.add(new int[]{r,size-1-i});
                }
            }
            filterOpenSpaces(check2,broken2);
        }
    }
    /**
     * Checks to see if there is a vertical line of l spaces of cv color that is on the edge of the board,
     * adds spaces that block/extend the line to an array
     * pre: cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkVertEdgeFour(int cv){
        for (int c = 0; c < size; c++){

            int check1 = 0, broken1 = 0;
            int check2 = 0, broken2 = 0;

            for (int i = 0; i < 5; i++){
                if (s[i][c] == cv){
                    check1++;
                }
                else if (s[i][c] == 0){
                    broken1++;
                    openSpaces.add(new int[]{i,c});
                }
            }
            filterOpenSpaces(check1,broken1);

            for (int i = 0; i < 5; i++){
                if (s[size-1-i][c] == cv){
                    check2++;
                }
                else if (s[size-1-i][c] == 0){
                    broken2++;
                    openSpaces.add(new int[]{size-1-i,c});
                }
            }
            filterOpenSpaces(check2,broken2);
        }
    }
    /**
     * Checks to see if there is a forward diagonal line of l spaces of cv color that is on the edge of the board,
     * adds spaces that block/extend the line to an array
     * pre: cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkForwardDiagEdgeFour(int cv){

        for (int c = 0; c < size-4; c++){

            int check1 = 0, broken1 = 0;
            int check2 = 0, broken2 = 0;

            for (int i = 0; i < 5; i++){
                if (s[4-i][c+i] == cv){
                    check1++;
                }
                else if (s[4-i][c+i] == 0){
                    broken1++;
                    openSpaces.add(new int[]{4-i,c+i});
                }
            }
            filterOpenSpaces(check1,broken1);

            for (int i = 0; i < 5; i++){
                if (s[size-1-i][c+i] == cv){
                    check2++;
                }
                else if (s[size-1-i][c+i] == 0){
                    broken2++;
                    openSpaces.add(new int[]{size-1-i,c+i});
                }
            }
            filterOpenSpaces(check2,broken2);
        }
        for (int r = 4; r < size; r++){

            int check1 = 0, broken1 = 0;
            int check2 = 0, broken2 = 0;

            for (int i = 0; i < 5; i++){
                if (s[r-i][i] == cv){
                    check1++;
                }
                else if (s[r-i][i] == 0){
                    broken1++;
                    openSpaces.add(new int[]{r-i,i});
                }
            }
            filterOpenSpaces(check1,broken1);

            for (int i = 0; i < 5; i++){
                if (s[r-i][size-5+i] == cv){
                    check2++;
                }
                else if (s[r-i][size-5+i] == 0){
                    broken2++;
                    openSpaces.add(new int[]{r-i,size-5+i});
                }
            }
            filterOpenSpaces(check2,broken2);
        }
    }
    /**
     * Checks to see if there is a backward diagonal line of l spaces of cv color that is on the edge of the board,
     * adds spaces that block/extend the line to an array
     * pre: cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkBackwardDiagEdgeFour(int cv){
        for (int c = 0; c < size-4; c++){

            int check1 = 0, broken1 = 0;
            int check2 = 0, broken2 = 0;

            for (int i = 0; i < 5; i++){
                if (s[i][c+i] == cv){
                    check1++;
                }
                else if (s[i][c+i] == 0){
                    broken1++;
                    openSpaces.add(new int[]{i,c+i});
                }
            }
            filterOpenSpaces(check1,broken1);

            for (int i = 0; i < 5; i++){
                if (s[size-5+i][c+i] == cv){
                    check2++;
                }
                else if (s[size-5+i][c+i] == 0){
                    broken2++;
                    openSpaces.add(new int[]{size-5+i,c+i});
                }
            }
            filterOpenSpaces(check2,broken2);
        }
        for (int r = 0; r < size-4; r++){

            int check1 = 0, broken1 = 0;
            int check2 = 0, broken2 = 0;

            for (int i = 0; i < 5; i++){
                if (s[r+i][i] == cv){
                    check1++;
                }
                else if (s[r+i][i] == 0){
                    broken1++;
                    openSpaces.add(new int[]{r+i,i});
                }
            }
            filterOpenSpaces(check1,broken1);

            for (int i = 0; i < 5; i++){
                if (s[r+i][size-5+i] == cv){
                    check2++;
                }
                else if (s[r+i][size-5+i] == 0){
                    broken2++;
                    openSpaces.add(new int[]{r+i,size-5+i});
                }
            }
            filterOpenSpaces(check2,broken2);
        }
    }
    /**
     * Checks to see if there check is 4 and broken is 1, if not, points are removed from the array
     * pre: none
     * post: Points are removed from the array if check != 4 and broken != 1
     */
    private void filterOpenSpaces(int check, int broken){
        if (!(check == 4 && broken == 1)){
            for (int i = 0; i < broken; i++){
                openSpaces.remove(openSpaces.size()-1);
            }
        }
    }

    /**
     * Checks to see if there is a horizontal line of 2 spaces of cv color that is placed in a specific manner,
     * adds spaces that block/extend/complete the line to an array
     * pre: cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkHoriComponents(int cv){
        for (int r = 0; r < size; r++){
            for (int c = 0; c < size - 4; c++){
                //--OO-
                if (s[r][c] == 0 && s[r][c+1] == 0 && s[r][c+2] == cv && s[r][c+3] == cv && s[r][c+4] == 0){
                    openSpaces.add(new int[]{r,c+1});
                }
                //-OO--
                if (s[r][c] == 0 && s[r][c+1] == cv && s[r][c+2] == cv && s[r][c+3] == 0 && s[r][c+4] == 0){
                    openSpaces.add(new int[]{r,c+4});
                }
                //-O-O-
                if (s[r][c] == 0 && s[r][c+1] == cv && s[r][c+2] == 0 && s[r][c+3] == cv && s[r][c+4] == 0){
                    openSpaces.add(new int[]{r,c+2});
                }
            }
        }
    }
    /**
     * Checks to see if there is a vertical line of 2 spaces of cv color that is placed in a specific manner,
     * adds spaces that block/extend/complete the line to an array
     * pre: cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkVertComponents(int cv){
        for (int r = 0; r < size-4; r++){
            for (int c = 0; c < size; c++){
                //--OO-
                if (s[r][c] == 0 && s[r+1][c] == 0 && s[r+2][c] == cv && s[r+3][c] == cv && s[r+4][c] == 0){
                    openSpaces.add(new int[]{r+1,c});
                }
                //-OO--
                if (s[r][c] == 0 && s[r+1][c] == cv && s[r+2][c] == cv && s[r+3][c] == 0 && s[r+4][c] == 0){
                    openSpaces.add(new int[]{r+3,c});
                }
                //-O-O-
                if (s[r][c] == 0 && s[r+1][c] == cv && s[r+2][c] == 0 && s[r+3][c] == cv && s[r+4][c] == 0){
                    openSpaces.add(new int[]{r+2,c});
                }
            }
        }
    }
    /**
     * Checks to see if there is a forward diagonal line of 2 spaces of cv color that is placed in a specific manner,
     * adds spaces that block/extend/complete the line to an array
     * pre: cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkForwardDiagComponents(int cv){
        for (int r = 4; r < size; r++){
            for (int c = 0; c < size-4; c++){
                //--OO-
                if (s[r][c] == 0 && s[r-1][c+1] == 0 && s[r-2][c+2] == cv && s[r-3][c+3] == cv && s[r-4][c+4] == 0){
                    openSpaces.add(new int[]{r-1,c+1});
                }
                //-OO--
                if (s[r][c] == 0 && s[r-1][c+1] == cv && s[r-2][c+2] == cv && s[r-3][c+3] == 0 && s[r-4][c+4] == 0){
                    openSpaces.add(new int[]{r-3,c+3});
                }
                //-O-O-
                if (s[r][c] == 0 && s[r-1][c+1] == cv && s[r-2][c+2] == 0 && s[r-3][c+3] == cv && s[r-4][c+4] == 0){
                    openSpaces.add(new int[]{r-2,c+2});
                }
            }
        }
    }
    /**
     * Checks to see if there is a backward diagonal of 2 spaces of cv color that is placed in a specific manner,
     * adds spaces that block/extend/complete the line to an array
     * pre: cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkBackwardDiagComponents(int cv){
        for (int r = 0; r < size-4; r++){
            for (int c = 0; c < size-4; c++){
                //--OO-
                if (s[r][c] == 0 && s[r+1][c+1] == 0 && s[r+2][c+2] == cv && s[r+3][c+3] == cv && s[r+4][c+4] == 0){
                    openSpaces.add(new int[]{r+1,c+1});
                }
                //-OO--
                if (s[r][c] == 0 && s[r+1][c+1] == cv && s[r+2][c+2] == cv && s[r+3][c+3] == 0 && s[r+4][c+4] == 0){
                    openSpaces.add(new int[]{r+3,c+3});
                }
                //-O-O-
                if (s[r][c] == 0 && s[r+1][c+1] == cv && s[r+2][c+2] == 0 && s[r+3][c+3] == cv && s[r+4][c+4] == 0){
                    openSpaces.add(new int[]{r+2,c+2});
                }
            }
        }
    }
    /**
     * Checks to see if there is a horizontal line of l spaces of cv color that is open on one end,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkHoriClosed(int l, int cv){
        for (int r = 0; r < size; r++){
            for (int c = 0; c < size-1-l; c++){
                boolean check = true;

                //checks middle
                for (int i = 0; i < l; i++){
                    if (s[r][c+1+i]!=cv){
                        check = false;
                    }
                }
                //When the length is 4 and one end is potentially blocked
                if (check && (s[r][c] == 0 || s[r][c+1+l] == 0)){
                    if (s[r][c] == 0)
                        openSpaces.add(new int[]{r, c});
                    if(s[r][c+1+l] == 0)
                        openSpaces.add(new int[]{r, c + l + 1});
                }

            }
        }
    }
    /**
     * Checks to see if there is a vertical line of l spaces of cv color that is open on one end,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkVertClosed(int l, int cv){
        for (int r = 0; r < size-1-l; r++){
            for (int c = 0; c < size; c++){
                boolean check = true;

                //checks middle
                for (int i = 0; i < l; i++){
                    if (s[r+1+i][c]!=cv){
                        check = false;
                    }
                }
                //When the length is 4 and one end is potentially blocked
                if (check && (s[r][c] == 0 || s[r+1+l][c] == 0)){
                    if (s[r][c] == 0)
                        openSpaces.add(new int[]{r, c});
                    if(s[r+1+l][c] == 0)
                        openSpaces.add(new int[]{r+1+l, c});
                }

            }
        }
    }
    /**
     * Checks to see if there is a forward diagonal line of l spaces of cv color that is open on one end,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkForwardDiagClosed(int l, int cv){
        for (int r = l+1; r < size; r++){
            for (int c = 0; c < size-1-l; c++){
                boolean check = true;

                //checks middle
                for (int i = 0; i < l; i++){
                    if (s[r-1-i][c+1+i]!=cv){
                        check = false;
                    }
                }

                if (check && (s[r][c] == 0 || s[r-1-l][c+1+l] == 0)){
                    if (s[r][c] == 0)
                        openSpaces.add(new int[]{r, c});
                    if(s[r-1-l][c+1+l] == 0)
                        openSpaces.add(new int[]{r-l-1, c+l+1});
                }
            }
        }
    }
    /**
     * Checks to see if there is a backward diagonal line of l spaces of cv color that is open on one end,
     * adds spaces that block/extend the line to an array
     * pre: l > 0 and cv == 1 || cv == 2
     * post: Points are added to an array when there is an appropriate line
     */
    private void checkBackwardDiagClosed(int l, int cv){
        for (int r = 0; r < size-1-l; r++){
            for (int c = 0; c < size-1-l; c++){
                boolean check = true;

                //check middle
                for (int i = 0; i < l; i++){
                    if (s[r+1+i][c+1+i]!=cv){
                        check = false;
                    }
                }

                if (check && (s[r][c] == 0 || s[r+1+l][c+1+l] == 0)){
                    if (s[r][c] == 0)
                        openSpaces.add(new int[]{r,c});
                    if (s[r+1+l][c+1+l] == 0)
                        openSpaces.add(new int[]{r+1+l,c+1+l});
                }
            }
        }
    }

}
