import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

/**
 * Write a description of class BankAccountMain here.
 *
 * @author (Kristen)
 * @version (1.0)
 */
public class BankAccountMain
{
    private static ArrayList<User> userAccounts = new ArrayList<User>();
    private static Scanner sc = new Scanner(System.in);
    static File pwFile, accFile;
    static BufferedWriter ubw, abw;
    static BufferedReader ubr, abr;

    public static void main(String [] args) {
        System.out.println("Welcome to the bank.");
        choices();
        rewrite(pwFile, accFile);
        System.out.println("Thank you for using our bank.");
    }

    // Panel of choices user can make
    public static void choices() {
        boolean valid = false;
        boolean goodUser = false;
        String input = "";
        fileStatus();
        getDataFromFile(pwFile, accFile);
        System.out.println("(A) Create a new user account\t(B) Access an existing user account\t(X) Exit the bank");

        while (!valid) {
            try {
                input = sc.next().trim().toUpperCase();
                switch (input) {
                    case "A": 
                        System.out.println("Please create a username");
                        String un = sc.next().toLowerCase();
    
                        while (true) {
                            if (userInData(un) != -1) {
                                System.out.println("This username is already taken, please input a different one.");
                                un = sc.next().toLowerCase();
                                continue;
                            } else {
                                break;
                            }
                        }
                        String pw = Password.makePassword();
                        makeAccount(un,pw);
                        valid = true;
                        break;
                    case "B":
                        signIn(input);
                        valid = true; 
                        break;
                    case "X":
                        valid = true;
                        break;
                    default:
                        System.out.println("Please enter a valid option\n");
                }
            } catch (Exception e){
                System.out.println("Whoops, that's not an option\n");
                e.printStackTrace();
            }
        }
    }

    /* --- USER HANDLING ---*/

    /**
     * @param un: username of user
     * @param password: unhashed password of user
     * 
     * Generates random salt for user and hashes password accordingly
     * 
     * Creates user with random user identifier link (userLink), 
     * lowercase username, hashed password, and user salt; adds user 
     * information to file and to userAccounts ArrayList
     */
    public static void makeAccount(String un, String password) {
        String salt = java.util.UUID.randomUUID().toString();
        String userID = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        password = Password.hash(password, salt);
        User u = new User(userID, un.toLowerCase(), password, salt);
        userAccounts.add(u);
        putInfoOnFile(u, pwFile, ubw);
        System.out.print("Hello, " + u.getUsername() + ".\n");
        u.bankMenu();
    }

    /**
     * Checks to see if the given username is in the list of users
     * @returns i: index of username in accounts, or -1 if
     * username isn't found
     */
    public static int userInData(String username) {
        for (int i = 0; i < userAccounts.size(); i++) {
            if (userAccounts.get(i).getUsername().equals(username)) {
                return i;
            }
        } 
        return -1;
    }

    /**
     * Prompts user to input username, and checks to see if it's in the
     * database. If it is, it prompts user to input a password. If the 
     * hashed (password + user salt) matches the hashed password, the user
     * can access the account.
     */
    public static void signIn(String input) {
        if (!userAccounts.isEmpty()) {
            System.out.println("\nPlease enter your username.");
            input = sc.next().toLowerCase();

            int counter = 1;
            int userIndex = userInData(input);
            if (userIndex > -1) {
                User u = userAccounts.get(userIndex);
                System.out.println("Please enter your password");
                input = sc.next();
                while (!(u.getHashedPassword().equals(Password.hash(input, u.getUserSalt()))) ){
                    if (counter < 3) {
                        System.out.println("Your password is incorrect, try again.");
                        counter++;
                        input = sc.next();
                    } else {
                        rewrite(pwFile, accFile);
                        System.out.println("Too many failed attempts, you are now being logged out of the program.");
                        System.exit(0);
                    }
                }
                System.out.print("Hello, " + u.getUsername() + ".\n");
                u.bankMenu();
            } else {
                System.out.println("I'm sorry, there are no users in the system with that username.");
                signIn(input);
            }
        } else {
            System.out.println("There are currently no users in the system\n");
            choices();
        }
    }

    /* --- FILE HANDLING ---*/

    /**
     * Creates files of given names if they don't exist, otherwise just
     * sets files to read only
     */
    public static void fileStatus() {
        pwFile = new File("Password.txt");
        accFile = new File("Accounts.txt");
        try {
            boolean isPw = pwFile.createNewFile();
            boolean isAcc = accFile.createNewFile();
            pwFile.setReadOnly();
            accFile.setReadOnly();
        } catch (Exception e) {
            System.out.println("Error with file creation?");
        }
    }

    /**
     * 
     */
    public static void getDataFromFile(File userFile, File accountFile) {
        try {
            ubr = new BufferedReader(new FileReader(userFile));
            abr = new BufferedReader(new FileReader(accountFile));
            String userStr = ubr.readLine();
            String accountStr = abr.readLine();

            while (userStr != null) {
                String[] userData = userStr.split("\\s*,\\s*");            
                User u = new User(userData);
                userAccounts.add(u);
                userStr = ubr.readLine();
            }
            while (accountStr != null) {
                String[] accountData = accountStr.split("\\s*,\\s*");
                BankAccount b = null;
                if (accountData[1].equals("savings")) {
                    b = new SavingsAccount(accountData);
                } else if (accountData[1].equals("checking")) {
                    b = new CheckingAccount(accountData);
                }

                accountStr = abr.readLine();
                for (int i = 0; i < userAccounts.size(); i++) {
                    if (userAccounts.get(i).getUserLink().equals(b.getUserLink())) {
                        userAccounts.get(i).addAccFromFile(b);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file\n");
        }
    }

    /**
     * @param o: object that will be written to file, will be either a user or a bank account
     * @param file: file to write information to
     * @param bw: Corresponding BufferedReader for specified file
     * 
     * Writes the information of the object to the text file
     */
    public static void putInfoOnFile(Object obj, File file, BufferedWriter bw) {
        try {
            file.setWritable(true);
            bw = new BufferedWriter(new FileWriter(file, true));
            if (obj instanceof User) {
                bw.write(((User)obj).toTextFile() + "\n");
            } else if (obj instanceof BankAccount) { 
                bw.write(((BankAccount)obj).toTextFile() + "\n");
            }
            file.setReadOnly();
            bw.close();
        } catch (IOException e) {
            System.out.print("Error adding info to file.\n\n");
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public static void rewrite(File pFile, File aFile) {
        try {
            pFile.setWritable(true);
            aFile.setWritable(true);
            ubw = new BufferedWriter(new FileWriter(pFile));
            abw = new BufferedWriter(new FileWriter(aFile));
            for (User u: userAccounts) {
                ubw.write(u.toTextFile() + "\n");
                for (BankAccount b: u.getAccounts()) {
                    abw.write(b.toTextFile() + "\n");
                }
            }
            pFile.setReadOnly();
            aFile.setReadOnly();
            abw.close();
            ubw.close();
        } catch (IOException e) {
            System.out.println("arghhh");
            e.printStackTrace();
        }
    }
}