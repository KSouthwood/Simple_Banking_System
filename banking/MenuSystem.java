package banking;

import java.util.Scanner;

public class MenuSystem {
    final static Scanner scanner = new Scanner(System.in);

    String mainMenu() {
        String choice = null;

        printMainMenu();
        switch (getChoice(0, 2)) {
            case 1:
                choice = "CREATE";
                break;
            case 2:
                choice = "LOGIN";
                break;
            case 0:
                choice = "QUIT";
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

    String cardMenu() {
        String choice = null;

        printCardMenu();
        switch (getChoice(0, 2)) {
            case 1:
                choice = "BALANCE";
                break;
            case 2:
                choice = "LOG-OUT";
                break;
            case 0:
                choice = "EXIT";
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
