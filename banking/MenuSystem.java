package banking;

import java.util.Scanner;

public class MenuSystem {
    final static Scanner scanner = new Scanner(System.in);

    /**
     * <p>Present user with the main menu choices.</p>
     * @return MenuState enum representing the users choice
     */
    MenuState mainMenu() {
        MenuState choice = null;

        printMainMenu();
        switch (getChoice(0, 2)) {
            case 1:
                choice = MenuState.GENERATE;
                break;
            case 2:
                choice = MenuState.LOG_IN;
                break;
            case 0:
                choice = MenuState.QUIT;
                break;
            default:
                break;
        }

        return choice;
    }

    private void printMainMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    /**
     * <p>Present user with the card account menu choices.</p>
     * @return MenuState enum representing the users choice
     */
    MenuState cardMenu() {
        MenuState choice = null;

        printCardMenu();
        switch (getChoice(0, 5)) {
            case 1:
                choice = MenuState.BALANCE;
                break;
            case 2:
                choice = MenuState.DEPOSIT;
                break;
            case 3:
                choice = MenuState.TRANSFER;
                break;
            case 4:
                choice = MenuState.CLOSE_ACCT;
                break;
            case 5:
                choice = MenuState.LOG_OUT;
                break;
            case 0:
                choice = MenuState.QUIT;
                break;
            default:
                break;
        }

        return choice;
    }

    void printCardMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    /**
     * <p>Gets input from the user and validates it</p>
     * <p>Reads input from the user and ensures it is a valid response. Should
     * the input be invalid, print an error message and wait for more input.</p>
     * @param min the smallest integer we will accept
     * @param max the largest integer we will accept
     * @return the valid input
     */
    private int getChoice(int min, int max) {
        boolean inRange = false;
        int choice = Integer.MIN_VALUE;

        while (!inRange) {
            String input = scanner.nextLine();

            if (input.matches("\\d")) {
                choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    inRange = true;
                }
            }

            if (!inRange) {
                System.out.printf("Please enter a single digit between %d and %d.%n",
                        min, max);
            }
        }

        System.out.println();
        return choice;
    }

    String readLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    /**
     * <p>Get an amount from the user. Only positive integers will be accepted.
     * (n >= 0)</p>
     * @return the positive amount entered by the user
     */
    int getAmount() {
        int amount = Integer.MIN_VALUE;

        do {
            String input = scanner.nextLine();
            if (!input.matches("^\\d+")) {
                System.out.println("Please enter a positive integer.");
                continue;
            }
            amount = Integer.parseInt(input);
        } while (amount < 0);

        return amount;
    }
}
