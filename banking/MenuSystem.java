package banking;

import java.util.Scanner;

public class MenuSystem {
    final static Scanner scanner = new Scanner(System.in);

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

    MenuState cardMenu() {
        MenuState choice = null;

        printCardMenu();
        switch (getChoice(0, 2)) {
            case 1:
                choice = MenuState.BALANCE;
                break;
            case 2:
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
        System.out.println("2. Log out");
        System.out.println("0. Exit");
    }

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

    String readLine() {
        return scanner.nextLine();
    }
}
