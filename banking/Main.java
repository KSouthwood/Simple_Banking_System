package banking;

public class Main {
    static Accounts accts = new Accounts();
    static MenuSystem menu = new MenuSystem();

    public static void main(String[] args) {
        String choice;

        do {
            choice = menu.mainMenu();
            switch (choice) {
                case "CREATE":
                    accts.generateAccount();
                    break;
                case "LOGIN":
                    login();
                    break;
                default:
                    break;
            }

        } while (!choice.equals("QUIT"));

        System.out.println("Bye!");
    }

    private static void login() {
        System.out.println("Enter your card number:");
        String number = menu.readLine();
        System.out.println("Enter your PIN:");
        String pin = menu.readLine();
        System.out.println();

        if (accts.accountExists(number) && accts.getAccount(number).getPIN().equals(pin)) {
            System.out.println("You have successfully logged in!\n");
            loggedIn(accts.getAccount(number));
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
    }

    private static void loggedIn(CreditCard card) {
        String choice;

        do {
            choice = menu.cardMenu();
            switch (choice) {
                case "BALANCE":
                    System.out.printf("Balance: %.0f%n%n", card.getBalance());
                    break;
                case "LOG-OUT":
                    System.out.println("You have successfully logged out!\n");
                    break;
                case "EXIT":
                default:
                    break;
            }
        } while (choice.equals("BALANCE"));
    }
}