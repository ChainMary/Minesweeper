import java.util.Scanner;

public class main {


    public static void main(String[] args) {
        int flagNumber = 0;
        // TODO: turn debug mode on or off depending on the setting (diffAndDebug[1])
        // decide difficulty, and whether debug mode is active
        String[] diffAndDebug = chooseDiff();
//        boolean debugMode = false;
//
//        // save debug status for rest of game
//        if (diffAndDebug[1].equals("Yes")) {
//            debugMode = true;
//            // already validated in chooseDiff()
//            // don't need to do the other case
//        }

        // create minefield, make an empty field
        Minefield myMinefield = null;
        switch (diffAndDebug[0]) {
            case "Easy" -> {
                myMinefield = new Minefield(5, 5, 5);
                flagNumber = 5;
                break;
            }
            case "Medium" -> {
                myMinefield = new Minefield(9, 9, 12);
                flagNumber = 12;
                break;
            }
            case "Hard" -> {
                myMinefield = new Minefield(20, 20, 40);
                flagNumber = 40;
                break;
            }
        }
// store rows and columns and flags
        // Show the empty board and ask where they want to start
        System.out.println(myMinefield.toString());

        // ask user where they want to start
        System.out.println("Enter the x and y coordinates to guess:\" [x] [y]\"");
        Scanner sr = new Scanner(System.in);

        // TODO: validate user input???
        int x = sr.nextInt();
        int y = sr.nextInt();


        // use createMines() to add mines to field, avoiding area where the player wants to start
        myMinefield.createMines(x,y,myMinefield.getMines());

        // change status of each cell to reflect mines around it with evaluateField()
        myMinefield.evaluateField(); // (users cannot see this but sets whole board up)

        // reveal starting area to user
        myMinefield.revealStartingArea(x,y);
        System.out.println(myMinefield);
        System.out.println("Your move...");
        boolean win = false;
        // While game isn't over (a mine has been revealed, or last cell uncovered)
        while (!myMinefield.gameOver()) {
            // todo: (add debug if debug code is Yes)

            // ask user where they want to guess next, or whether they want to flag
            System.out.println("Enter the x and y coordinates to guess or flag:\" [x] [y]\"");
            Scanner scnr = new Scanner(System.in);

            x = scnr.nextInt(); // todo: validate these
            y = scnr.nextInt();
            System.out.println("Flag? (Yes/No)");
            String output = scnr.next();
            if (output.equals("Yes")) {
                win = myMinefield.guess(x, y, true);
                flagNumber--;
                if (flagNumber <= 0)
                    flagNumber = 0;
            } else
                win = myMinefield.guess(x, y, false);
            if (diffAndDebug[1].equals("Yes")) {
                myMinefield.debug(); // the debug board
                System.out.println();
                System.out.println(myMinefield); // print the the own board
            } else System.out.println(myMinefield);

            if (win == true){
                myMinefield.debug();
                System.out.println("You lost");
                break;
            }}

        if (win == false)
            System.out.println("You win!");




        // TODO: if user guesses zero then reveal surrounding zeroes with method
        // TODO: Fix gameover
        // TODO: Fix color of print
        // TODO (optional): make it so numbers bigger than 10 print evenly on the board
        // TODO: validate user input!!!
        // TODO: fix debug method for the TA's
    }

    public static String[] chooseDiff() {
        String[] diffAndDebug = new String[2];

        System.out.println("Choose your difficulty (Easy, Medium, or Hard): "); // set diff to Easy Medium or Hard
        Scanner sr = new Scanner(System.in);
        String userWords = sr.next();
        while (!userWords.equals("Easy") && !userWords.equals("Medium") && !userWords.equals("Hard")) {
            System.out.println("Please select a difficulty (\"Easy\" \"Medium\" \"Hard\"):");
            userWords = sr.next();
        }
        diffAndDebug[0] = userWords;

        System.out.println("Debug Mode - all cells revealed (Yes, or No): "); // set debug mode to (Yes or No)
        Scanner sr2 = new Scanner(System.in);
        String userWords2 = sr2.next();
        while (!userWords2.equals("Yes") && !userWords2.equals("No")) {
            System.out.println("Please select a difficulty (\"Yes\" \"No\"):");
            userWords2 = sr2.next();
        }
        diffAndDebug[1] = userWords2;

        return diffAndDebug;
    }

}