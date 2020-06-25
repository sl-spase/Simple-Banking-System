package banking;

import java.util.Random;

class Account {
    private String cardNumber;
    private String pin;
    Random random = new Random();
    DataBase dataBase = new DataBase();


    public Account() {
        this.cardNumber = generateCardNumber();
        this.pin = generatePin();
    }


    public void createAccount() {
        String msg = String.format("Your card has been created\n" +
                "Your card number:\n" +
                "%s\n" +
                "Your card PIN:\n%s\n", getCardNumber(), getPin());

        System.out.println(msg);
        dataBase.insert(cardNumber, pin);
    }


    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("400000");

        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }

        String[] ch = sb.toString().split("");

        int[] digits = new int[ch.length];

        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(ch[i]);
        }

        if (!checkCard(digits)) {
            return generateCardNumber();
        }

        sb.setLength(0);
        for (int i : digits) {
            sb.append(i);
        }
        return sb.toString();
    }


    public static boolean checkCard(int[] digits) {
        int sum = 0;
        int length = digits.length;


        for (int i = 0; i < length; i++) {

            int digit = digits[length - i - 1];

            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum == 60;
    }


    private String generatePin() {
        String cardPinNumber = randomPinNumber();
        while (!isValidPinNumber()) {
            cardPinNumber = randomPinNumber();
        }

        return cardPinNumber;
    }

    private String randomPinNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }


    private boolean isValidPinNumber() {
        return true;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public String toString() {
        return "Account{" +
                "cardNumber='" + cardNumber + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

}
