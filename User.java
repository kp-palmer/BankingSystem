import java.util.*;
/**
 * Write a description of class User here.
 *
 * @author (Kristen)
 */
public class User
{
    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
    private String username = "";
    private String hashedPassword = "";
    private String userSalt = "";
    private String userLink;
    private int checkingCount;
    private int savingsCount;

    // Constructor to create user from within program (from user input)
    public User(String userLink, String un, String hpw, String us) {
        this.userLink = userLink;
        this.username = un;
        this.hashedPassword = hpw;
        this.userSalt = us;
    }  

    // Constructor to create user from file
    public User(String[] userInfo) {
        this.userLink = userInfo[0];
        this.username = userInfo[1];
        this.hashedPassword = userInfo[2];
        this.userSalt = userInfo[3];
    }

    /* -- GETTERS -- */
    public String getUserLink() {
        return this.userLink;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUserSalt() {
        return this.userSalt;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public ArrayList<BankAccount> getAccounts() {
        return this.accounts;
    }

    // Formats text for text file
    public String toTextFile() {
        return userLink + "," + username + "," + hashedPassword + "," + userSalt;
    }
    
    // Prints options users can take within their account
    public void bankMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("(A) Create a new account\t (B) Access an existing account\t (C) Change your password (X) Exit");
        boolean valid = false;
        String input = "";

        if (!valid) {
            try {
                input = sc.next().trim().toUpperCase();
                if (input.equals("A") || input.equals("B") || input.equals("C") || input.equals("X")) {
                    valid = true;
                } else {
                    System.out.println("Please enter a valid option\n");
                }
            } catch (Exception e){
                System.out.println("Whoops, that's not an option\n");
            }
        }

        switch (input) {
            case "A":
                addAccount();
                bankMenu();
                break;
            case "B":
                accessAccounts();
                bankMenu();
                break;
            case "C":
                changePassword();    
                bankMenu();
                break;
            case "X":
                break;
            default:
                bankMenu();
                break;
        }
    }

    // Makes user create a new password, changes the salt once new password is set
    public void changePassword() {
        System.out.println("What would you like to change your password to?");
        String password = Password.makePassword();
        if (hashedPassword.equals(Password.hash(password, userSalt))) {
            System.out.println("Your new password cannot be the same as your old password, try again.");
            changePassword();
        } else {
            this.userSalt = java.util.UUID.randomUUID().toString();
            this.hashedPassword = Password.hash(password, userSalt);
        }
    }
    
    /**
     * Prompts user to choose between checking and savings account,
     * writes new bank account to file and allows user to access/modify
     * its information
     */
    public void addAccount() {
        Scanner sc = new Scanner(System.in);
        boolean valid = false;
        String in = null;
        
        while (!valid) { 
            System.out.println("What type of account would you like to create?\n(1) Checking account\t(2) Savings account");
            in = sc.next();
            try {
                int input = Integer.parseInt(in); 
                switch(input){
                    case 1:
                        valid = true;
                        checkingCount++;
                        BankAccount checkingAcc = new CheckingAccount(userLink, checkingCount);
                        accounts.add(checkingAcc);
                        BankAccountMain.putInfoOnFile(checkingAcc, BankAccountMain.accFile, BankAccountMain.abw);
                        System.out.println("Welcome to your new checking account. What would you like to do?");
                        checkingAcc.displayMenu();
                        break;
                    case 2:
                        valid = true;
                        savingsCount++;
                        BankAccount savingsAcc = new SavingsAccount(userLink, savingsCount);
                        accounts.add(savingsAcc);
                        BankAccountMain.putInfoOnFile(savingsAcc, BankAccountMain.accFile, BankAccountMain.abw);
                        System.out.println("Welcome to your new savings account. What would you like to do?");
                        savingsAcc.displayMenu();
                        break;
                    default:
                        System.out.println("Please enter 1 or 2\n");
                        //addAccount();
                }
            } catch (NumberFormatException e) {
                System.out.println("This is not a valid entry\n"); 
                //addAccount();
            }
        }
    }

    public void addAccFromFile(BankAccount b) {
        accounts.add(b);
    }

    /**
     * Prints list of user's bank accounts, allows them to
     * access account information upon entering account ID
     */
    public void accessAccounts() {
        Scanner sc = new Scanner(System.in);
        boolean validID = false;
        
        if (!accounts.isEmpty()) {
            System.out.println();
            for (BankAccount b: accounts) {
                System.out.println("ID: " + b.getID() + "\tBalance: $" + b.checkBalance());
            }
            System.out.println("\nType the ID of the account you would like to acccess");

            String input = sc.next().trim().toUpperCase();
            if (!validID) {
                for (int i = 0; i < accounts.size(); i++){
                    if (input.equals(accounts.get(i).getID().toUpperCase())) {
                        accounts.get(i).displayMenu();
                        validID = true;
                    }
                }
                if(!validID) {
                    System.out.println("I'm sorry, that is not a valid ID");
                }
            }
        } else {
            System.out.println("You have not created any accounts yet\n");
        }
    }
}