package banking;

import java.util.*;


public class Main {
    public static void main(String[] args) {
        String databaseName = args[1];
        DataBase dataBase = new DataBase(databaseName);
        DataBase.createNewDatabase();
        DataBase.createNewTable();

        BankingSystem bankingSystem = new BankingSystem();
        bankingSystem.systemStart();
    }
}


class BankingSystem {
    Scanner scanner = new Scanner(System.in);
    private int userPressedButton;
    Login login = new Login();

    public BankingSystem() {

    }

    private void showMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        userPressedButton = scanner.nextInt();
        System.out.println();
    }

    public void systemStart() {
        while (true) {
            if (login.isExit()) {
                return;
            } else {
                showMenu();
            }
            switch (userPressedButton) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    loginInAccount();
                    break;

                case 0:
                    System.out.println("Bye!");
                    return;
            }
        }
    }

    private void createAccount() {
        Account account = new Account();
        account.createAccount();
    }

    private void loginInAccount() {
        Login login = new Login();
        login.logIn();
    }

}



