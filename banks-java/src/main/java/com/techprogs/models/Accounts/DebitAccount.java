package com.techprogs.models.Accounts;

import com.techprogs.models.Customer.Customer;
import com.techprogs.models.Transaction;
import com.techprogs.tools.IncorrectTransactionException;
import com.techprogs.tools.NotEnoughMoneyException;

public class DebitAccount extends Account {
    private double percent;
    private double accumulatedAmount;

    public DebitAccount(Customer owner, double balance, double percent) {
        this.owner = owner;
        this.percent = percent;
        this.balance = balance;

        isSuspicious = owner.getAddress().equals("") || owner.getPassportNumber() == 0;
        if (balance != 0) {
            transactions.add(new Transaction(null, this, balance));
        }
    }

    public double withdrawMoney(Transaction newTransaction) throws NotEnoughMoneyException, IncorrectTransactionException {
        if (newTransaction.getAccountFrom() != this){
            throw new IncorrectTransactionException();
        }
        if (balance - newTransaction.getAmountOfMoney() < 0) {
            throw new NotEnoughMoneyException(id);
        }
        balance -= newTransaction.getAmountOfMoney();
        transactions.add(newTransaction);
        return balance;

    }

    public void everydayUpdate() {
        accumulatedAmount += balance * (percent / (365 * 100));
    }

    public void everyMonthUpdate() {
        balance += accumulatedAmount;
        accumulatedAmount = 0;
    }

    public void setInterest(double newInterest) {
        percent = newInterest;
    }

    @Override
    public double getAccumulatedAmount() {
        return accumulatedAmount;
    }

}
