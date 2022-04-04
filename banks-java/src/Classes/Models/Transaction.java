package Classes.Models;

import Classes.Models.Accounts.IAccount;

import java.util.UUID;

public class Transaction {
    private final IAccount _accountFrom;
    private final IAccount _accountTo;
    private final double _amount;
    private final String _transactionId = UUID.randomUUID().toString();

    public Transaction(IAccount accountFrom, IAccount accountTo, double amount){
        _accountFrom = accountFrom;
        _accountTo = accountTo;
        _amount = amount;
    }

    public double getAmountOfMoney(){
        return _amount;
    }

    public String getTransactionId(){
        return _transactionId;
    }

    public IAccount getAccountTo() { return _accountTo; }

    public IAccount getAccountFrom() { return _accountFrom; }
}
