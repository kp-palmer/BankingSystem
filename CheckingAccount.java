import java.util.*;
import java.text.DecimalFormat;

/**
 * Write a description of class CheckingAccount here.
 *
 * @author (Kristen)
 */
public class CheckingAccount extends BankAccount
{   
    public CheckingAccount(String userLink, int id)
    {
        super(userLink, "checking", id);
    }
    
    public CheckingAccount(String userLink, int id, double balance)
    {
        super(userLink, "checking", id, balance);
    }
    
    public CheckingAccount(String[] data) {
        super(data);
    }
}