package Classes.Models;

import Classes.Models.Accounts.IAccount;
import Classes.Models.Customer.Customer;
import Classes.Tools.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CentralBank {
    private List<Bank> _allBanks = new ArrayList<>();
    private static CentralBank _centralBank;

    private CentralBank(){ }

    public Bank createBank(String bankName, double transactionLimit, double creditLimit, double creditAccountCommission, HashMap<Double, Double> depositAccountInterest, double debitAccountInterest) throws ExistedBankException {
        if (getBankByName(bankName) != null){
            throw new ExistedBankException();

        }
        else {
            Bank newBank = new Bank(bankName, transactionLimit, creditLimit, creditAccountCommission, depositAccountInterest, debitAccountInterest);
            _allBanks.add(newBank);
            return newBank;
        }
    }

    public String transferMoney(Bank bankFrom, Bank bankTo, IAccount accountFrom, IAccount accountTo, double amount) throws NotEnoughMoneyException, IncorrectTimeException, TransactionLimitException, DepositWithdrawalException {
        Transaction newTransaction = new Transaction(accountFrom, accountTo, amount);
        bankFrom.transferringWithdrawal(newTransaction);
        bankTo.transferringAddition(newTransaction);
        return newTransaction.getTransactionId();
    }

    public void dailyInterestCall() throws NotEnoughMoneyException, IncorrectTimeException {
        for (Bank bank : _allBanks){
            bank.dailyInterestCall();
        }
    }

    public void monthlyInterestCall() throws IncorrectTimeException {
        for (Bank bank : _allBanks){
            bank.monthlyInterestCall();
        }
    }

    public static CentralBank getInstance(){
        if (_centralBank == null){
            _centralBank = new CentralBank();
        }
        return _centralBank;
    }

    public Bank getBankByName(String name){
        for (Bank bank : _allBanks){
            if (bank.getName().equals(name)){
                return bank;
            }
        }
        return null;
    }

    public void cancelTransaction(String transactionId){

        for (Bank bank : _allBanks){
            for (Customer customer : bank.getAllCustomers()){
                for (IAccount account : customer.getAccounts()){
                    for (int i = 0; i < account.getTransactions().size(); ++i){
                        if (account.getTransactions().get(i).getTransactionId() == transactionId) {
                            account.cancelTransaction(account.getTransactions().get(i));
                        }
                    }
                }
            }

        }
    }

    public List<Bank> getAllBanks(){ return _allBanks; }
}
