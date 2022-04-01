package Classes.Models.Accounts;

import Classes.Models.Customer.Customer;

public abstract class AccountCreator {

    public abstract IAccount createNewAccount(Customer owner, int daysBeforeExpiry, double creditLimit, double commission, double percent);
}
