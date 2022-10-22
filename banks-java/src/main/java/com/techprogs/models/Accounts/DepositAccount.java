package com.techprogs.models.Accounts;

import com.techprogs.models.Customer.Customer;
import com.techprogs.models.Transaction;
import com.techprogs.tools.DepositTransferException;
import com.techprogs.tools.IncorrectTransactionException;
import com.techprogs.tools.NotEnoughMoneyException;

public class DepositAccount extends Account{
    private double percent;
    private double accumulatedAmount;
    private double daysBeforeExpiry;

    public DepositAccount(Customer owner, double balance, double percent, int daysBeforeExpiry){
        this.owner = owner;
        this.daysBeforeExpiry = daysBeforeExpiry;
        this.percent = percent;
        this.balance = balance;
        isSuspicious = owner.getAddress().equals("") || owner.getPassportNumber() == 0;
    }

    @Override
    public double addMoney(Transaction newTransaction) throws DepositTransferException {
        throw new DepositTransferException();
    }

    public double withdrawMoney(Transaction newTransaction) throws DepositTransferException, NotEnoughMoneyException, IncorrectTransactionException {
        if (newTransaction.getAccountFrom() != this){
            throw new IncorrectTransactionException();
        }
        if (daysBeforeExpiry > 0){
            throw new DepositTransferException();
        }
        if (balance - newTransaction.getAmountOfMoney() < 0){
            throw new NotEnoughMoneyException(id);
        }


        balance -= newTransaction.getAmountOfMoney();
        transactions.add(newTransaction);
        return balance;
    }

    public void everydayUpdate(){
        if (daysBeforeExpiry > 0){
            accumulatedAmount += balance * (percent / (365 * 100));
            daysBeforeExpiry--;
        }
    }

    public void everyMonthUpdate(){
        if (daysBeforeExpiry > 0){
            balance += accumulatedAmount;
            accumulatedAmount = 0;
        }
    }

    public void setInterest(double newInterest) {
        percent = newInterest;
    }

    @Override
    public double getAccumulatedAmount(){ return  accumulatedAmount; }
}
