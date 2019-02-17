import java.util.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

/**
 * @author (Kristen Palmer)
 * @version (1.0)
 */
public class Password
{
    /**
     * Prompts user to create a password that meets the requirements specified in the validPassword() method
     * Method checks to see whether or not the password meets a number of requirements
     * If it doesn't meet a requirement, the user will be told what they must do to fix their error
     * @returns pw: valid password
     */
    public static String makePassword() {
        System.out.println("Please enter a password with at least 10 characters, one number, one uppercase and one lowercase letter, and one special character.");
        Scanner sc = new Scanner(System.in);
        String password;
        
        while (true) {
            password = sc.nextLine();
            if (!(password.length() >= 10)) {
                System.out.println("Your password must be 10 or more characters.");
            } else if (!password.matches(".*\\d.*")) {
                System.out.println("Your password must contain at least one number.");
            } else if (!(password.matches(".*[A-Z].*") && password.matches(".*[a-z].*"))) {
                System.out.println("Your password must contain at least one uppercase and one lowercase letter.");
            } else if (!(password.matches(".*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*"))) {
                System.out.println("Your password must contain at least one special character.");
            } else {
                break;
            }
        }
        return password;
    }

    // returns string that contains the hashed password + salt
    public static String hash(String password, String salt) {
        return getSHA(password + salt);
    }
    
    public static String getSHA(String input) { 
        try { 
            MessageDigest md = MessageDigest.getInstance("SHA-256"); 
            byte[] messageDigest = md.digest(input.getBytes()); 
            BigInteger no = new BigInteger(1, messageDigest); 
            String hashtext = no.toString(16); 
    
            while (hashtext.length() < 64) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        } 
        catch (NoSuchAlgorithmException e) { 
            System.out.println("Exception thrown"
                            + " for incorrect algorithm: " + e); 
            return null; 
        } 
    } 
}