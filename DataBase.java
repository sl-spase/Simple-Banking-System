package banking;

import java.sql.*;
import java.util.Scanner;

class DataBase {

    private static String url;

    public DataBase() {
    }

    public DataBase(String arg) {
        url = "jdbc:sqlite:" + arg;
    }

    public static void createNewDatabase() {

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean checkUserNameAndPassword(String cardNumber, String password) {
        String sql = "SELECT * FROM card where number = ? and pin = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,cardNumber);
            pstmt.setString(2,password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("You have successfully logged in!\n");
            } else {
                System.out.println("Wrong card number or PIN!\n");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;

    }


    public static boolean isUserCardExist(String cardNumber) {
        String sql = "SELECT * FROM card where number = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,cardNumber);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Such a card does not exist.\n");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;

    }


    public static void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getBalanceStatus(String cardNumber) {

        String sql = "SELECT balance from CARD WHERE NUMBER = ?" ;
        int result = 0;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,cardNumber);
            ResultSet rs = pstmt.executeQuery();

            result = rs.getInt("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }


    public void doTransfer(String transferFrom, String transferTo) {
        Scanner scanner = new Scanner(System.in);

        if (!Login.checkLuhnAlgorithm(transferTo)) {
            System.out.println("Probably you made mistake in the card number. Please try again!\n");
            return;
        }

        if (transferFrom.equals(transferTo)) {
            System.out.println("You can't transfer money to the same account!\n");
            return;
        }

        if (!isUserCardExist(transferTo)) {
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int transferAmount = Integer.parseInt(scanner.nextLine());
        if (transferAmount > getBalanceStatus(transferFrom)) {
            System.out.println("Not enough money!\n");
            return;
        }

        String sql1 = "UPDATE card\n" +
                "SET balance = balance + " + transferAmount +
                " WHERE\n" +
                " number = " + transferTo;

        String sql2 = "UPDATE card\n" +
                "SET balance = balance - " + transferAmount +
                " WHERE\n" +
                " number = " + transferFrom

                ;
        try (Connection conn = DriverManager.getConnection(url)){
            Statement stmt  = conn.createStatement();
            stmt.execute(sql1);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Success!\n");

   }


    public void addIncome(int income, String cardNumber) {
        String sql = "UPDATE card set balance = balance + ?" +
                "WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,income);
            pstmt.setString(2,cardNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Income was added!\n");
    }


    public void closeAccount(String cardNumber) {
        String sql = "DELETE FROM card" +
                " WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,cardNumber);
            pstmt.executeUpdate();
            System.out.println("The account has been closed!\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }




    public void insert(String cartNumber, String cardPin) {
        String sql = "INSERT INTO card(number, pin) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cartNumber);
            pstmt.setString(2, cardPin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}