package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Database {
    Connection connection;

    /**
     * <p>Create a database file with supplied filename.</p>
     * <p>Create the database file to be used for storing the card numbers and
     * PIN's. There is no need to check if the file does/doesn't exist as it
     * will be created if it doesn't, otherwise it opens the file.</p>
     * <p>We also attempt to create the table using
     * <code>CREATE TABLE IF NOT EXISTS</code> - the command only succeeds if
     * there is no table in the database yet.</p>
     *
     * @param filename of database to create
     */
    void createDatabase(String filename) {
        String url = "jdbc:sqlite:" + filename;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try {
            connection = dataSource.getConnection();
            if (connection.isValid(5)) {
                String sql = "CREATE TABLE IF NOT EXISTS card (\n" +
                        "id INTEGER PRIMARY KEY,\n" +
                        "number TEXT,\n" +
                        "pin TEXT,\n" +
                        "balance INTEGER DEFAULT 0\n" +
                        ");";
                Statement stmt = connection.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void closeDatabase() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Add the specified card number and associated PIN into the database.</p>
     *
     * @param number card number to store
     * @param pin associated PIN
     */
    void addCreditCard(String number, String pin) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES (?, ?, 0)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, number);
            stmt.setString(2, pin);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Verifies that the card and PIN entered are correct.</p>
     *
     * @param number card number to verify
     * @param pin    PIN to match to
     * @return true if number exists in table and PIN is correct
     */
    boolean verifyCardAndPIN(String number, String pin) {
        boolean success = false;
        String query = "SELECT * FROM card WHERE number = " + number;
        try (ResultSet rs = connection.createStatement().executeQuery(query)) {
            while (rs.next()) {
                success = number.equals(rs.getString("number")) &&
                        pin.equals(rs.getString("pin"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }

    /**
     * <p>Retrieves the balance of an account. The card number is guaranteed to exist
     * in the table.</p>
     *
     * @param number card number to get balance for
     * @return the current balance
     */
    int getBalance(String number) {
        int balance = -1;
        String query = "SELECT * FROM card WHERE number = " + number;
        try (ResultSet rs = connection.createStatement().executeQuery(query)) {
            while (rs.next()) {
                balance = rs.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    /**
     * <p>Increase balance of an account. Account is guaranteed to exist in table
     * and amount is guaranteed to be positive.</p>
     *
     * @param number card number of balance to increase
     * @param amount how much balance should increase
     */
    void addFunds(String number, int amount) {
        String query =
                "UPDATE card SET balance = balance + " + amount +
                        " WHERE number = " + number;

        try (Statement stmt = connection.createStatement()) {
            if (stmt.executeUpdate(query) == 1) {
                System.out.println("Income was added!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Delete the card from the table. The number is guaranteed to exist in the table.</p>
     *
     * @param number of account to delete
     */
    void closeAccount(String number) {
        String query = "DELETE FROM card WHERE number = " + number;

        try (Statement stmt = connection.createStatement()) {
            if (stmt.executeUpdate(query) == 1) {
                System.out.println("The account has been closed!\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Checks if a card number is already in the table.</p>
     *
     * @param number card number to look for
     * @return true if we found the card
     */
    boolean verifyCardExists(String number) {
        String query = "SELECT * FROM card WHERE number = " + number;
        boolean valid = false;

        try (ResultSet rs = connection.createStatement().executeQuery(query)) {
            valid = rs.next();  // will be true if the ResultSet has at least one row
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valid;
    }

    /**
     * <p>Move funds from one account to another.</p>
     *
     * <p>Turns the auto-commit off before moving funds to ensure both transactions
     * succeed before updating the database with them. If any errors occur, we
     * roll-back the database and report failure.</p>
     *
     * @param transferFrom   the account to transfer from (guaranteed to be valid)
     * @param transferTo     the account to transfer to (guaranteed to be valid)
     * @param transferAmount how much to transfer between accounts
     * @return true if we moved funds without any errors
     */
    boolean transferFunds(String transferFrom, String transferTo, int transferAmount) {
        boolean success = false;
        String subtractFunds = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String depositFunds = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try {
            connection.setAutoCommit(false);

            PreparedStatement fromAccount = connection.prepareStatement(subtractFunds);
            fromAccount.setInt(1, transferAmount);
            fromAccount.setString(2, transferFrom);
            fromAccount.executeUpdate();

            PreparedStatement toAccount = connection.prepareStatement(depositFunds);
            toAccount.setInt(1, transferAmount);
            toAccount.setString(2, transferTo);
            toAccount.executeUpdate();

            connection.commit();
            success = true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.out.println("Transfer failed!\n");
                    connection.rollback();
                    e.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        try {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}
