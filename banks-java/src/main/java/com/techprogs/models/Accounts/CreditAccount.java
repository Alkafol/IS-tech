package com.techprogs.models.Accounts;

import com.techprogs.models.Customer.Customer;
import com.techprogs.models.Transaction;
import com.techprogs.tools.IncorrectTimeException;
import com.techprogs.tools.IncorrectTransactionException;
import com.techprogs.tools.NotEnoughMoneyException;

public class CreditAccount extends Account{
    private double creditLimit;
    private double commission;

    public CreditAccount(Customer owner, double balance, double creditLimit, double commission){
        this.owner = owner;
        this.creditLimit = creditLimit;
        this.commission = commission;
        this.balance = balance;

        isSuspicious = owner.getAddress().equals("") || owner.getPassportNumber() == 0;
        if (balance != 0){
            transactions.add(new Transaction(null, this, balance));
        }
    }

    public double withdrawMoney(Transaction newTransaction) throws NotEnoughMoneyException, IncorrectTransactionException {
        if (newTransaction.getAccountFrom() != this){
            throw new IncorrectTransactionException();
        }
        if (balance - newTransaction.getAmountOfMoney() < creditLimit){
            throw new NotEnoughMoneyException(id);
        }
        balance -= newTransaction.getAmountOfMoney();
        transactions.add(newTransaction);
        return balance;
    }

    public void everydayUpdate() throws NotEnoughMoneyException {
        if (balance < 0){
            balance -= commission;
        }
    }

    @Override
    public void everyMonthUpdate() throws IncorrectTimeException {}

    public void setCommission(double newCommission) {
        commission = newCommission;
    }

    public void setCreditLimit(double newCreditLimit) {
        creditLimit = newCreditLimit;
    }
}
