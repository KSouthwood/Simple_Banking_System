package banking;

public class Main {
    static Accounts accounts = new Accounts();
    static MenuSystem menu = new MenuSystem();
    static MenuState state = MenuState.MAIN_MENU;
    static String cardNumber = null;
    static Database db = new Database();

    public static void main(String[] args) {
        if (args.length == 2) {
            db.createDatabase(args[1]);
        }

        MenuState choice;

        do {
            choice = state.equals(MenuState.MAIN_MENU) ? menu.mainMenu() : menu.cardMenu();
            switch (choice) {
                case GENERATE:
                    accounts.generateAccount(db);
                    break;
                case LOG_IN:
                    login();
                    break;
                case LOG_OUT:
                    System.out.println("You have successfully logged out!\n");
                    state = MenuState.MAIN_MENU;
                    cardNumber = null;
                    break;
                case BALANCE:
                    System.out.printf("Balance: %d%n%n", db.getBalance(cardNumber));
                    break;
                case DEPOSIT:
                    deposit();
                    break;
                case TRANSFER:
                    transfer();
                    break;
                case CLOSE_ACCT:
                    db.closeAccount(cardNumber);
                    state = MenuState.MAIN_MENU;
                    cardNumber = null;
                    break;
            }
        } while (!choice.equals(MenuState.QUIT));

        db.closeDatabase();
        System.out.println("Bye!");
    }

    /**
     * <p>Log into an account using the account number and PIN</p>
     */
    private static void login() {
        String number = menu.readLine("Enter your card number:");
        String pin = menu.readLine("Enter your PIN:");
        System.out.println();

        if (db.verifyCardAndPIN(number, pin)) {
            System.out.println("You have successfully logged in!\n");
            state = MenuState.CARD_MENU;
            cardNumber = number;
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
    }

    private static void deposit() {
        System.out.println("Enter income:");
        db.addFunds(cardNumber, menu.getAmount());
        System.out.println();
    }

    /**
     * <p>Transfer funds between two accounts - the currently logged in account
     * and one that the user enters.</p>
     */
    private static void transfer() {
        String transferTo = menu.readLine("Transfer\nEnter card number:");

        // make sure the card number was entered correctly
        int check = accounts.generateCheckDigit(transferTo.substring(0, 15));
        if (!transferTo.substring(15).equals(String.valueOf(check))) {
            System.out.println("Probably you made a mistake in the card number.\n" +
                    "Please try again!\n");
            return;
        }

        // valid card number - check if it exists
        if (!db.verifyCardExists(transferTo)) {
            System.out.println("Such a card does not exist.\n");
            return;
        }

        // card exists - same account number?
        if (transferTo.equals(cardNumber)) {
            System.out.println("You can't transfer money to the same account!\n");
            return;
        }

        // ask how much to transfer and make sure we have enough
        System.out.println("Enter how much money you want to transfer:");
        int transferAmount = menu.getAmount();
        if (transferAmount > db.getBalance(cardNumber)) {
            System.out.println("Not enough money!\n");
            return;
        }

        System.out.println(
                db.transferFunds(cardNumber, transferTo, transferAmount) ?
                        "Success!\n" : "Failed!\n");
    }
}