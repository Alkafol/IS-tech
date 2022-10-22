package com.techprogs.models.Accounts;

import com.techprogs.models.Customer.Customer;
import com.techprogs.models.Transaction;
import com.techprogs.tools.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected double balance;
    protected boolean isSuspicious;
    protected String id;
    protected Customer owner;
    protected List<Transaction> transactions = new ArrayList<>();

    public abstract double withdrawMoney(Transaction newTransaction) throws NotEnoughMoneyException, DepositTransferException, IncorrectTransactionException;
    public abstract void everydayUpdate() throws NotEnoughMoneyException, IncorrectTimeException;
    public abstract void everyMonthUpdate() throws IncorrectTimeException;

    public void cancelTransaction(Transaction transaction) throws IncorrectTransactionException {
        if (!transactions.contains(transaction)){
            throw new IncorrectTransactionException();
        }

        if (transaction.getAccountFrom() == this){
            balance += transaction.getAmountOfMoney();
        }
        else if (transaction.getAccountTo() == this){
            balance -= transaction.getAmountOfMoney();
        }
        transactions.remove(transaction);
    }

    public double addMoney(Transaction newTransaction) throws DepositTransferException, IncorrectTransactionException {
        if (newTransaction.getAccountTo() != this){
            throw new IncorrectTransactionException();
        }
        balance += newTransaction.getAmountOfMoney();
        transactions.add(newTransaction);
        return balance;
    }

    public double getBalance(){
        return balance;
    }

    public boolean getSuspicious(){
        return isSuspicious;
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

    public String getAccountId(){
        return id;
    }

    public void setSuspicious(boolean isSuspicious){
        this.isSuspicious = isSuspicious;
    }

    public double getAccumulatedAmount(){ return 0;}

    public Customer getOwner() {
        return owner;
    }
}

