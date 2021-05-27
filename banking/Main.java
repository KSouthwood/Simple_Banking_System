package banking;

public class Main {
    static Accounts accounts = new Accounts();
    static MenuSystem menu = new MenuSystem();
    static MenuState state = MenuState.MAIN_MENU;
    static CreditCard card = null;

    public static void main(String[] args) {
        Database db = new Database();
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
                    login(db);
                    break;
                case LOG_OUT:
                    System.out.println("You have successfully logged out!\n");
                    state = MenuState.MAIN_MENU;
                    card = null;
                    break;
                case BALANCE:
                    int balance = db.getBalance(card.getAccountNumber());
                    System.out.printf("Balance: %d%n%n", balance);
                    break;
            }
        } while (!choice.equals(MenuState.QUIT));

        db.closeDatabase();
        System.out.println("Bye!");
    }

    private static void login(Database db) {
        System.out.println("Enter your card number:");
        String number = menu.readLine();
        System.out.println("Enter your PIN:");
        String pin = menu.readLine();
        System.out.println();

        if (db.verifyCardAndPIN(number, pin)) {
            System.out.println("You have successfully logged in!\n");
            state = MenuState.CARD_MENU;
            card = new CreditCard(number, pin);
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
    }

}