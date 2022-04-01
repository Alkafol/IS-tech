package Classes.Models.Accounts;

import Classes.Models.Transaction;
import Classes.Tools.DepositWithdrawalException;
import Classes.Tools.IncorrectTimeException;
import Classes.Tools.NotEnoughMoneyException;

import java.util.List;

public interface IAccount {
    double addMoney(Transaction newTransaction);
    double withdrawMoney(Transaction newTransaction) throws NotEnoughMoneyException, IncorrectTimeException, DepositWithdrawalException;
    void everydayUpdate() throws NotEnoughMoneyException, IncorrectTimeException;
    void everyMonthUpdate() throws IncorrectTimeException;
    void cancelTransaction(Transaction transaction);
    double getBalance();
    boolean getSuspicious();
    List<Transaction> getTransactions();
    String getAccountId();
    void setSuspicious(boolean ifSuspicious);
    void setCreditLimit(double newCreditLimit);
    void setCommission(double newCommission);
    void setInterest(double newInterest);
}
