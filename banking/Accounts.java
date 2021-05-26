package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Accounts {
    private final Map<String, CreditCard> accounts = new HashMap<>();
    private static Random rnd;

    Accounts() {
        rnd = new Random();
    }

    void addAccount(CreditCard card) {
        accounts.put(card.getAccountNumber(), card);
    }

    CreditCard getAccount(String number) {
        return accounts.get(number);
    }

    boolean accountExists(String number) {
        return accounts.containsKey(number);
    }

    void generateAccount() {
        StringBuilder number;

        do {
            number = new StringBuilder("400000");

            for (int digits = 0; digits < 9; digits++) {
                number.append(rnd.nextInt(10));
            }

            number.append(rnd.nextInt(10)); // add check digit (will be calculated in later stages)
        } while (accountExists(number.toString()));

        StringBuilder pin = new StringBuilder();
        for (int digits = 0; digits < 4; digits++) {
            pin.append(rnd.nextInt(10));
        }

        CreditCard card = new CreditCard(number.toString(), pin.toString());
        addAccount(card);

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(number);
        System.out.println("Your card PIN:");
        System.out.println(pin);
        System.out.println();
    }
}
