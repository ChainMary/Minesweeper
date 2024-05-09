import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Minefield {
    /**
     Global Section
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /*
     * Class Variable Section
     *
     */
    private Cell[][] minefield;
    private int mines;

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */

    /**
     * Minefield
     *
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        mines = flags;
        minefield = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield[i][j] = new Cell(false, "-");
            }
        }
    }

    /**
     * evaluateField
     *
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     *
     */
    public void evaluateField() {
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield.length; j++) {
                int total = 0; // total bombs in a 3x3 radius

                // check all areas surrounding the spot, if within bounds
                if (!minefield[i][j].getStatus().equals("M")) { // if the spot itself is not a bomb,
                    // upper left - only check if going less than 0 - [x-1][y-1]
                    if (i-1 >= 0 && j-1 >= 0) {
                        if (minefield[i-1][j-1].getStatus().equals("M")) {
                            total++;
                        }
                    }
                    // upper mid, if x-1
                    if (i-1 >= 0) {
                        if (minefield[i-1][j].getStatus().equals("M")) {
                            total++;
                        }
                    }
                    // upper right, if x-1 and y + 1
                    if (i-1 >= 0 && j+1 <= minefield.length-1) {
                        if (minefield[i-1][j+1].getStatus().equals("M")) {
                            total++;
                        }
                    }
                    // left, y - 1
                    if (j-1 >= 0) {
                        if (minefield[i][j-1].getStatus().equals("M")) {
                            total++;
                        }
                    }
                    // right, y + 1
                    if (j+1 <= minefield.length -1) {
                        if (minefield[i][j+1].getStatus().equals("M")) {
                            total++;
                        }
                    }
                    // bottom left, x+1 y-1
                    if (i+1 <= minefield.length - 1 && j-1 >= 0) {
                        if (minefield[i+1][j-1].getStatus().equals("M")) {
                            total++;
                        }
                    }
                    // bottom mid, x+1
                    if (i+1 <= minefield.length - 1 ) {
                        if (minefield[i+1][j].getStatus().equals("M")) {
                            total++;
                        }
                    }
                    // bottom right x+1 y+1
                    if (i+1 <= minefield.length - 1 && j+1 <= minefield.length - 1) {
                        if (minefield[i+1][j+1].getStatus().equals("M")) {
                            total++;
                        }
                    }
                    // if - 1, check to see if going below zero
                    // if + 1, check to see if going past minefield.length-1

                    minefield[i][j].setStatus(String.valueOf(total));
                }
            }
        }
    }

    /**
     * createMines
     *
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        for (int num = mines; num > 0; num--) {
            Random rand = new Random();
            int r1 = rand.nextInt(0,minefield.length);
            int r2 = rand.nextInt(0,minefield.length);

            // If there is no mine at the spot, and the spot is not the same as the starting point (x, y)
            if ((!minefield[r1][r2].getStatus().equals("M")) && r1 != x && r2 != y) {
                minefield[r1][r2].setStatus("M");
            } else { num++; }
        }
    }

    /**
     * guess
     *
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     *
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        if ((x<0 && y < 0 )|| (x > minefield.length && y > minefield.length)){
            System.out.println("Out of bounds, guess again: ");
        }
        else{
            minefield[x][y].setRevealed(true);
            if (flag){
                minefield[x][y].setStatus("F");
                }
            if (minefield[x][y].getStatus().equals("M"))
                    return true;

            else {
                if (minefield[x][y].getStatus().equals("0"))
                    revealZeroes(x,y);
                else if (minefield[x][y].getStatus().equals("M"))
                    return true;
            }
        }
        return false;
    }


    /**
     * gameOver
     *
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        int time = 0; // unrevealed cells counter
        for (int i = 0; i < minefield.length; i ++) {
            for (int j = 0; j < minefield[0].length; j++) { // iterate through minefield to count unrevealed cells

                if (!minefield[i][j].getRevealed()) {
                    time++;
                }
            }
        }

        for (int i = 0; i < minefield.length; i ++) {
            for (int j = 0; j < minefield[0].length; j++) { // iterate through minefield to decide if game is over

                // end case 1 : when the player reveals the last cell
                if (time == 1 && !minefield[i][j].getStatus().equals("M")) {
                    System.out.println("YOU WIN!!!");
                    return true; // the game is over
                }

                // end case 2: if the player REVEALS cell that is a MINE
                if (minefield[i][j].getStatus().equals("M") && minefield[i][j].getRevealed()) { // is a mine, and is revealed
                    System.out.println("YOU LOSE!!!");
                    return true; // the game is over
                }

            }
        }
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the pseudocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen<Cell> myZeroStack = new Stack1Gen<>();
        myZeroStack.push(minefield[x][y]); // add to the stack

        while (!myZeroStack.isEmpty()) { // while stack isn't empty, loop thru
            Cell myCell = myZeroStack.pop(); // get from stack,
            myCell.setRevealed(true); // make that "0" visible to user by setting revealed to true

            // check its surroundings via recursion and repeat if a zero is found

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int currentX = x + dx;
                    int currentY = y + dy;
                    if (currentX >= 0 && currentX < minefield.length && currentY >= 0 && currentY < minefield[0].length) {
                        Cell nextCell = minefield[currentX][currentY];
                        if (nextCell.getStatus().equals("0") && !nextCell.getRevealed())
                            myZeroStack.push(nextCell);}

                }
            }
        }
    }



//        while (!myZeroStack.isEmpty()) { // while stack isn't empty, loop thru
//            Cell myCell = myZeroStack.pop(); // get from stack,
//            myCell.setRevealed(true); // make that "0" visible to user by setting revealed to true
//
//            // check its surroundings via recursion and repeat if a zero is found
//
//            // upper left
//            if (x - 1 >= 0 && y - 1 >= 0) {
//                if (minefield[x-1][y-1].getStatus().equals("0")) {
//                    myZeroStack.push(minefield[x-1][y-1]);// use push instead
//                }
//            }
//            // upper mid
//            if (x - 1 >= 0) {
//                if (minefield[x-1][y].getStatus().equals("0")) {
//                    myZeroStack.push(minefield[x-1][y]);
//                }
//            }
//            // upper right
//            if (x - 1 >= 0 && y + 1 <= minefield.length-1) {
//                if (minefield[x-1][y+1].getStatus().equals("0")) {
//                    myZeroStack.push(minefield[x-1][y+1]);
//                }
//            }
//            // left
//            if (y-1 >= 0) {
//                if (minefield[x][y-1].getStatus().equals("0")) {
//                    myZeroStack.push(minefield[x][y-1]);
//                }
//            }
//            // right
//            if (y + 1 <= minefield.length - 1) {
//                if (minefield[x][y+1].getStatus().equals("0")) {
//                    myZeroStack.push(minefield[x][y+1]);
//                }
//            }
//            // bottom left
//            if (x + 1 <= minefield.length - 1 && y - 1 >= 0) {
//                if (minefield[x+1][y-1].getStatus().equals("0")) {
//                    myZeroStack.push(minefield[x+1][y-1]);
//                }
//            }
//            // bottom mid
//            if (x + 1 <= minefield.length - 1) {
//                if (minefield[x+1][y].getStatus().equals("0")) {
//                    myZeroStack.push(minefield[x+1][y]);
//                }
//            }
//            // bottom right
//            if (x + 1 <= minefield.length - 1 && y + 1 <= minefield.length - 1) {
//                if (minefield[x+1][y+1].getStatus().equals("0")) {
//                    myZeroStack.push(minefield[x+1][y+1]);
//                }
//            }
//
//        }
//
//    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
//    Q1Gen<Cell> myQ= new Q1Gen<>();
    public void revealStartingArea(int x, int y) {
        Q1Gen<Cell> myQ = new Q1Gen<>();
        myQ.add(minefield[x][y]); // Add the initial cell into the queue

        while (myQ.length() > 0) {
            Cell removed = myQ.remove(); // Remove a cell from the queue
            removed.setRevealed(true); // Mark the cell as revealed

            if (removed.getStatus().equals("M")) // If the removed cell is a mine, stop further revealing
                break;

            // Check neighboring cells and add them to the queue if they meet the criteria
            if (y - 1 >= 0 && !minefield[x][y - 1].getRevealed()) // Left neighbor
                myQ.add(minefield[x][y - 1]);
            if (y + 1 < minefield[0].length && !minefield[x][y + 1].getRevealed()) // Right neighbor
                myQ.add(minefield[x][y + 1]);
            if (x - 1 >= 0 && !minefield[x - 1][y].getRevealed()) // Upper neighbor
                myQ.add(minefield[x - 1][y]);
            if (x + 1 < minefield.length && !minefield[x + 1][y].getRevealed()) // Lower neighbor
                myQ.add(minefield[x + 1][y]);
        }
    }



    /**
     * For both printing methods utilize the ANSI colour codes provided!
     *
     *
     *
     *
     *
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected.
     */
    public void debug() { // TODO: print the full board revealed before the user makes a guess, when in debug mode
        System.out.print("  ");
        for (int i = 0; i < minefield.length; i++) { // E.g. (x 0 1 2 3 4)
            System.out.printf("%3d", i);
        }
        System.out.println();

        for (int i = 0; i < minefield.length; i++) {
            System.out.printf("%2d", i);
            System.out.print("  ");

            for (int j = 0; j < minefield[0].length; j++) {
                String status = minefield[i][j].getStatus();
                switch (status) {
                    case "F":
                        System.out.print(ANSI_GREEN + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "M":
                        System.out.print(ANSI_RED + status + "  "+ ANSI_GREY_BACKGROUND);
                        break;
                    case "0":
                        System.out.print(ANSI_YELLOW_BRIGHT + status + "  "+ ANSI_GREY_BACKGROUND);
                        break;
                    case "1":
                        System.out.print(ANSI_YELLOW + status+ "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "2":
                        System.out.print(ANSI_BLUE_BRIGHT + status + "  "+ ANSI_GREY_BACKGROUND);
                        break;
                    case "3":
                        System.out.print(ANSI_BLUE + status+ "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "4":
                        System.out.print(ANSI_RED_BRIGHT + status + "  "+ ANSI_GREY_BACKGROUND);
                        break;
                    case "5":
                        System.out.print(ANSI_PURPLE + status+ "  "+ ANSI_GREY_BACKGROUND);
                        break;
                    case "6":
                        System.out.print(ANSI_CYAN + status + "  "+ ANSI_GREY_BACKGROUND);
                        break;
                    case "7":
                        System.out.print(ANSI_RED + status + "  "+ ANSI_GREY_BACKGROUND);
                        break;
                    case "8":
                        System.out.print(ANSI_GREEN + status + "  "+ ANSI_GREY_BACKGROUND);
                        break;
                    case "9":
                        System.out.print(ANSI_YELLOW + status+ "  " + ANSI_GREY_BACKGROUND);
                        break;
                    default:
                        System.out.print(ANSI_GREY_BACKGROUND + status+ "  " + ANSI_GREY_BACKGROUND);


                }
                // show square cuz this is debug mode
            }
            System.out.println();
        }
    }

    public int getMines() {
        return mines;
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() { // TODO: make it colorful, make it look nice when going above 10
        StringBuilder out = new StringBuilder();
        out.append("  ");
        for (int i = 0; i < minefield.length; i++)  // E.g. (x 0 1 2 3 4)
            out.append(String.format("%3d", i));
        out.append("\n");

        for (int i = 0; i < minefield.length; i++) {
            out.append(String.format("%2d", i));
            out.append("  ");

            for (int j = 0; j < minefield[0].length; j++) {
                if (minefield[i][j].getRevealed()){
                String status = minefield[i][j].getStatus();
                    switch (status) {
                        case "F":
                            out.append(ANSI_GREEN).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "M":
                            out.append(ANSI_RED).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "0":
                            out.append(ANSI_YELLOW_BRIGHT).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "1":
                            out.append(ANSI_YELLOW).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "2":
                            out.append(ANSI_BLUE_BRIGHT).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "3":
                            out.append(ANSI_BLUE).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "4":
                            out.append(ANSI_RED_BRIGHT).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "5":
                            out.append(ANSI_PURPLE).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "6":
                            out.append(ANSI_CYAN).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "7":
                            out.append(ANSI_RED).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "8":
                            out.append(ANSI_GREEN).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        case "9":
                            out.append(ANSI_YELLOW).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                            break;
                        default:
                            out.append(ANSI_GREY_BACKGROUND).append(status).append("  ").append(ANSI_GREY_BACKGROUND);
                    }
                } else { // otherwise, show a null value (Note: the true "status"/value is already decided, just not shown to user yet)
                    out.append(ANSI_GREY_BACKGROUND).append("-").append("  ").append(ANSI_GREY_BACKGROUND);
                }
            }
            out.append("\n");

            System.out.println();}

        return out.toString();

}}
