package Classes.Models.Accounts;

import Classes.Models.Customer.Customer;
import Classes.Models.Transaction;
import Classes.Tools.NotEnoughMoneyException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DebitAccount implements IAccount{
    private double _balance;
    private boolean _suspicious;
    private double _creditLimit;
    private String _id = UUID.randomUUID().toString();
    private Customer _owner;
    private int _daysBeforeExpiry;
    private double _commission;
    private double _percent;
    private double _accumulatedAmount;
    private List<Transaction> _allTransactions = new ArrayList<>();

    public DebitAccount(Customer owner, int daysBeforeExpiry, double creditLimit, double commission, double percent){
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

    public void everydayUpdate(){
        if (_daysBeforeExpiry > 0){
            _accumulatedAmount += _balance * (_percent / (365 * 100));
        }
    }

    public void everyMonthUpdate(){
        if (_daysBeforeExpiry > 0){
            _balance += _accumulatedAmount;
            _accumulatedAmount = 0;
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

    public boolean getSuspicious(){ return _suspicious; }

    public double getBalance() { return _balance; }

    public String getAccountId(){ return _id; }

    public List<Transaction> getTransactions(){ return _allTransactions; }

    public void setSuspicious(boolean ifSuspicious){ _suspicious = ifSuspicious; }

    public void setCommission(double newCommission) { return; }

    public void setCreditLimit(double newCreditLimit) { return; }

    public void setInterest(double newInterest) { _percent = newInterest; }

}
