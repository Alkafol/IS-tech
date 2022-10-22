package com.techprogs.models.Accounts;

import com.techprogs.models.Customer.Customer;

public class AccountCreator {
    public Account createAccount(AccountType accountType, Customer owner, double balance, int daysBeforeExpiry,
                                 double percent, double creditLimit, double commission){
        Account account = null;
        switch (accountType){
            case DEBIT:
                account = new DebitAccount(owner, balance, percent);
                break;
            case CREDIT:
                account = new CreditAccount(owner, balance, creditLimit, commission);
                break;
            case DEPOSIT:
                account = new DepositAccount(owner, balance, percent, daysBeforeExpiry);
                break;
        }

        return account;
    }

}
