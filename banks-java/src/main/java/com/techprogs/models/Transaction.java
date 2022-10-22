package com.techprogs.models;

import com.techprogs.models.Accounts.Account;

import java.util.UUID;

public class Transaction {
    private final Account accountFrom;
    private final Account accountTo;
    private final double amount;
    private final String transactionId = UUID.randomUUID().toString();

    public Transaction(Account accountFrom, Account accountTo, double amount){
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public double getAmountOfMoney(){
        return amount;
    }

    public String getTransactionId(){
        return transactionId;
    }

    public Account getAccountTo() { return accountTo; }

    public Account getAccountFrom() { return accountFrom; }
}
