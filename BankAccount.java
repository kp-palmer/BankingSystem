import java.util.*;
import java.text.DecimalFormat;
import java.io.*;

/**
 * Abstract class BankAccount - write a description of the class here
 *
 * @author (Kristen)
 */
abstract class BankAccount
{   
    private String userLink;
    private String type;
    private String id;
    private double myBalance = 0.00;

    // Constructor for making new accounts with user input
    public BankAccount(String userLink, String type, int id) {
        this.userLink = userLink;
        this.type = type;
        this.id = type + id;
    }

    // Constructor to make account with non-zero balance
    public BankAccount(String userLink, String type, int id, double balance) {
        this.userLink = userLink;
        this.type = type;
        this.id = type + id;
        this.myBalance = balance;
    }

    // Constructor to create bank accounts from file
    public BankAccount(String[] accountData) {
        userLink = accountData[0];
        type = accountData[1];
        id = accountData[2];
        myBalance = Double.parseDouble(accountData[3]);
    }

    /* --- GETTERS --- */
    public String getUserLink() {
        return this.userLink;
    }

    public String getID() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String checkBalance() {
        return formatCash(this.myBalance);
    } 

    // Displays menu for each account
    public void displayMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("(1) Deposit money\t(2) Withdraw money\t (3) View balance\t(4) Exit " + this.id);
        boolean valid = false;
        int input = 0;

        while (!valid) {
            try {
                input = Integer.parseInt(sc.next().trim()); 
                valid = true;
            } catch (NumberFormatException e){
                System.out.println("Whoops, that's not an option");
            }
        }

        switch (input) {
            case 1:
            System.out.println("How much money would you like to deposit? (Don't type the $)");
            try {
                Double money = Double.parseDouble(sc.next().trim());
                this.deposit(money);
            } catch (NumberFormatException e) {
                System.out.println("Sorry, this isn't a valid entry");
            }
            displayMenu();
            break;
            case 2:
            System.out.println("How much money would you like to withdraw? (Don't type the $)");
            try {
                Double money = Double.parseDouble(sc.next().trim());
                this.withdraw(money);
            } catch (NumberFormatException e) {
                System.out.println("Sorry, this isn't a valid entry");
            }
            displayMenu();
            break;
            case 3:
            System.out.println("Balance: $" + this.checkBalance());
            displayMenu();
            break;
            case 4:
            System.out.println("Exiting account");
            break;
            default:
            System.out.println("Sorry, this isn't an option");
            displayMenu();
            break;
        }
    }

    // Rounds money to two decimal places
    public String formatCash(double money) {
        DecimalFormat df2 = new DecimalFormat(".##");
        double dm = money * 100;
        String correctMoney;

        if (dm % 1 != 0) {
            correctMoney = df2.format(money);
        } else {
            correctMoney = money + "0";
        }
        return correctMoney;
    }

    // Adds money to current balance
    public void deposit(double money) {
        if (money > 0) {
            this.myBalance += Math.round(money * 100.0) / 100.0;
            System.out.println("$" + formatCash(money) + " has been added to your account");
            //BankAccountMain.overWrite(this, BankAccountMain.accFile, BankAccountMain.abr, BankAccountMain.abw);
        } else {
            System.out.println("No can do\n");
        }
    }

    // Subtracts money from current balance as long as money < balance
    public void withdraw(double money){
        if (money < myBalance && money > 0) {
            this.myBalance -= Math.round(money * 100.0) / 100.0;
            System.out.println("Transaction is successful, $" + formatCash(money) + " has been removed from your account.");
            //BankAccountMain.overWrite(this, BankAccountMain.accFile, BankAccountMain.abr, BankAccountMain.abw);
        } else {
            System.out.println("No siree, can't do that\n");
        }
    }

    // Formats string to be added to text file
    public String toTextFile() {
        return userLink + "," + type + "," + id + "," + formatCash(myBalance);
    }
}