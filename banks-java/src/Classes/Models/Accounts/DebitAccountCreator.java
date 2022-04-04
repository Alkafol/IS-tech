package Classes.Models.Accounts;

import Classes.Models.Customer.Customer;

public class DebitAccountCreator extends AccountCreator {
    @Override
    public IAccount createNewAccount(Customer owner, int daysBeforeExpiry, double creditLimit, double commission, double percent){
        return new DebitAccount(owner, daysBeforeExpiry, creditLimit, commission, percent);
    }
}