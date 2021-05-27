package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    Connection connection;

    /**
     * Creates a database file with supplied filename if one doesn't exist. Also
     * will create the initial table if necessary as well.
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void addCreditCard(String number, String pin) {
        String statement =
                String.format("INSERT INTO card (number, pin, balance) VALUES (%s, %s, 0)",
                        number, pin);

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(statement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

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
}
