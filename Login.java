package banking;

import java.util.Scanner;

class Login {

    Scanner scanner = new Scanner(System.in);
    private String loginCardNumber;
    private String loginCardPin;
    private static boolean isExit = false;

    public Login() {

    }

    public void logIn() {
        System.out.println("Enter your card number:");
        loginCardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        loginCardPin = scanner.nextLine();
        System.out.println();

        if (DataBase.checkUserNameAndPassword(loginCardNumber, loginCardPin)) {
            showLogInMenu();
        }
    }

    private void showLogInMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
        String option = scanner.nextLine();
        System.out.println();
        DataBase dataBase = new DataBase();
        switch (option) {
            case "1":
                int balance = dataBase.getBalanceStatus(loginCardNumber);
                System.out.println("Balance: " + balance + "\n");
                showLogInMenu();
                break;
            case "2":
                System.out.println("Enter income:");
                int income = Integer.parseInt(scanner.nextLine());
                dataBase.addIncome(income, loginCardNumber);
                showLogInMenu();
                break;
            case "3":
                System.out.println("Transfer\nEnter card number:");
                String transferTo = scanner.nextLine();
                dataBase.doTransfer(loginCardNumber, transferTo);
                showLogInMenu();
                break;
            case "4":
                dataBase.closeAccount(loginCardNumber);
                break;
            case "5":
                System.out.println("You have successfully logged out!\n");
                isExit = false;
                break;
            case "0":
                isExit = true;
                break;
        }
    }

    public boolean isExit() {
        return isExit;
    }

    public static boolean checkLuhnAlgorithm(String cardNumber) {
        int result = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                int doubleDigit = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
                result += doubleDigit;
                continue;
            }
            result += digit;
        }
        return result % 10 == 0;
    }
}
