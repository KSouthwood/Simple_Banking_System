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

            number.append(generateCheckDigit(number)); // add check digit (will be calculated in later stages)
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

    /**
     * Generate a check digit using Luhn's Algorithm.
     *
     * @param number the card number to be checksummed
     * @return check digit
     */
    private int generateCheckDigit(StringBuilder number) {
        int total = 0;
        for (int digit = 0; digit < number.length(); digit++) {
            int num = number.charAt(digit) - '0';
            if (digit % 2 == 0) {   // odd digit (index for odd digits will be even)
                num *= 2;   // double it
                if (num > 9) {
                    num -= 9;   // subtract 9 if we're at 10 or over
                }
            }
            total += num;
        }
        return (10 - (total % 10)) % 10;    // calculate check digit
    }
}
