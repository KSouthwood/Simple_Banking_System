package banking;

public class CreditCard {
    private final String accountNumber;
    private final String PIN;
    private final double balance;

    CreditCard(String number, String pin) {
        accountNumber = number;
        PIN = pin;
        balance = 0.00;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPIN() {
        return PIN;
    }

    public double getBalance() {
        return balance;
    }
}
