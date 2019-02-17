import java.util.*;

/**
 * Write a description of class CheckingAccount here.
 *
 * @author (Kristen)
 */
public class SavingsAccount extends BankAccount
{
    public SavingsAccount(String userLink, int id)
    {
        super(userLink, "savings", id);
    }
    
    public SavingsAccount(String userLink, int id, double balance)
    {
        super(userLink, "savings", id, balance);
    }
    
    public SavingsAccount(String[] data) {
        super(data);
    }
}
