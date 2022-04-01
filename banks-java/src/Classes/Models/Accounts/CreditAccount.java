package Classes.Models.Accounts;

import Classes.Models.Customer.Customer;
import Classes.Models.Transaction;
import Classes.Tools.NotEnoughMoneyException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreditAccount implements IAccount{

    private double _balance;
    private boolean _suspicious;
    private double _creditLimit;
    private String _id = UUID.randomUUID().toString();
    private Customer _owner;
    private int _daysBeforeExpiry;
    private double _commission;
    private double _percent;
    private List<Transaction> _allTransactions = new ArrayList<>();

    public CreditAccount(Customer owner, int daysBeforeExpiry, double creditLimit, double commission, double percent){
        if (owner.getAddress() == "" && owner.getPassportNumber() == 0){
            _suspicious = true;
        }
        else{
            _suspicious = false;
        }
        _owner = owner;
        _daysBeforeExpiry = daysBeforeExpiry;
        _creditLimit = creditLimit;
        _commission = commission;
        _percent = percent;
    }

    public double addMoney(Transaction newTransaction){
        _balance += newTransaction.getAmountOfMoney();
        _allTransactions.add(newTransaction);
        return _balance;
    }

    public double withdrawMoney(Transaction newTransaction) throws NotEnoughMoneyException {
        if (_balance - newTransaction.getAmountOfMoney() < _creditLimit){
            throw new NotEnoughMoneyException();
        }
        _balance -= newTransaction.getAmountOfMoney();
        _allTransactions.add(newTransaction);
        return _balance;
    }

    public void everydayUpdate() throws NotEnoughMoneyException {
        if (_balance < 0){
            if (_balance - _commission < _creditLimit){
                throw new NotEnoughMoneyException();
            }
            else {
                _balance -= _commission;
            }
        }
    }

    public void cancelTransaction(Transaction transaction){
        if (transaction.getAccountFrom() == this){
            _balance += transaction.getAmountOfMoney();
            _allTransactions.remove(transaction);
        }
        else if (transaction.getAccountTo() == this){
            _balance -= transaction.getAmountOfMoney();
            _allTransactions.remove(transaction);
        }
    }

    public void everyMonthUpdate(){
        return;
    }

    public boolean getSuspicious(){
        return _suspicious;
    }

    public double getBalance() { return _balance; }

    public List<Transaction> getTransactions(){ return _allTransactions; }

    public String getAccountId(){ return _id; }

    public void setSuspicious(boolean ifSuspicious){
        _suspicious = ifSuspicious;
    }

    public void setCommission(double newCommission) { _commission = newCommission; }

    public void setCreditLimit(double newCreditLimit) { _creditLimit = newCreditLimit; }

    public void setInterest(double newInterest) { return; }
}
