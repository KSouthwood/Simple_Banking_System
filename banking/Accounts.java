package banking;

import java.util.Random;

public class Accounts {
    private static Random rnd;

    Accounts() {
        rnd = new Random();
    }

    /**
     * <p>Generate a 16 digit credit card number. First six digits are always
     * the same, the next nine are random, and the last is the check digit
     * calculated using Luhn's algorithm.</p>
     *
     * @param db the database that all account numbers are stored in
     */
    void generateAccount(Database db) {
        StringBuilder number;

        do {
            number = new StringBuilder("400000");

            for (int digits = 0; digits < 9; digits++) {
                number.append(rnd.nextInt(10));
            }

            number.append(generateCheckDigit(number.toString())); // add check digit
        } while (db.verifyCardExists(number.toString()));

        StringBuilder pin = new StringBuilder();
        for (int digits = 0; digits < 4; digits++) {
            pin.append(rnd.nextInt(10));
        }

        db.addCreditCard(number.toString(), pin.toString());

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(number);
        System.out.println("Your card PIN:");
        System.out.println(pin);
        System.out.println();
    }

    /**
     * <p>Generate a check digit using Luhn's Algorithm.</p>
     *
     * @param number the card number to be checksummed
     * @return check digit
     */
    int generateCheckDigit(String number) {
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
