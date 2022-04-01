package Classes.Services;

import Classes.Models.Accounts.IAccount;
import Classes.Models.Bank;
import Classes.Models.CentralBank;
import Classes.Models.Customer.Customer;
import Classes.Models.DateTime;
import Classes.Tools.*;

import java.util.HashMap;

public class BankServices implements IBankServices{
    private CentralBank _centralBank;
    private DateTime _currentDateTime; // in minutes from the beginning

    public BankServices(){
        _centralBank = _centralBank.getInstance();
        _currentDateTime = new DateTime();
    }

    public Bank createBank(String bankName, double transactionLimit, double creditLimit, double creditAccountCommission, HashMap<Double, Double> depositAccountInterest, double debitAccountInterest){
        try {
            return _centralBank.createBank(bankName, transactionLimit, creditLimit, creditAccountCommission, depositAccountInterest, debitAccountInterest);
        } catch (ExistedBankException e) {
            return null;
        }
    }

    public Customer createCustomer(Bank bank, String name, String surname){
        return bank.createCustomer(name, surname, 0, "");
    }

    public IAccount createDebitAccount(Bank bank, Customer customer, int dayBeforeExpiry){
        return bank.createDebitAccount(customer, dayBeforeExpiry);
    }

    public IAccount createDepositAccount(Bank bank, Customer customer, int dayBeforeExpiry, double amountOfMoney){
        return bank.createDepositAccount(customer, dayBeforeExpiry, amountOfMoney);
    }

    public IAccount createCreditAccount(Bank bank, Customer customer, int dayBeforeExpiry){
        return bank.createCreditAccount(customer, dayBeforeExpiry);
    }

    public void scrollTime(int valueInHours){
        try {
            int daysPassed = valueInHours / 24;
            _currentDateTime.scrollTimeInHours(valueInHours);
            for (int i = 0; i < daysPassed; i++) {
                _centralBank.dailyInterestCall();
                if ((i + 1) % 30 == 0 && i != 0) {
                    _centralBank.monthlyInterestCall();
                }
            }
        }
        catch (NotEnoughMoneyException e){ }
        catch (IncorrectTimeException e){ }
    }

    public String addMoney(Bank bank, IAccount account, double amount){
        return bank.addMoney(null, account, amount);
    }

    public String withdrawMoney(Bank bank, IAccount account, double amount){
        try {
            return bank.withdrawMoney(account, null, amount);
        }
        catch (NotEnoughMoneyException e){ return null; }
        catch (IncorrectTimeException e){ return null; }
        catch (TransactionLimitException e){ return null; }
        catch (DepositWithdrawalException e){ return null; }
    }

    public String transferMoney(Bank bankFrom, IAccount accountFrom, Bank bankTo, IAccount accountTo, double amount){
        try {
            return _centralBank.transferMoney(bankFrom, bankTo, accountFrom, accountTo, amount);
        }
        catch (NotEnoughMoneyException e){ return null; }
        catch (IncorrectTimeException e){ return null; }
        catch (TransactionLimitException e){ return null; }
        catch (DepositWithdrawalException e){ return null; }
    }

    public void changeCreditAccountConfiguration(Bank bank, double newCommission, double newCreditLimit){
        bank.changeCreditAccountsConfiguration(newCommission, newCreditLimit);
    }

    public void changeDebitAccountConfiguration(Bank bank, double newInterest){
        bank.changeDebitAccountsConfiguration(newInterest);
    }

    public void changeDepositAccountConfiguration(Bank bank, HashMap<Double, Double> newConditions){
        bank.changeDepositAccountsConfiguration(newConditions);
    }

    public IAccount getAccountById(String id){
        for (Bank bank : _centralBank.getAllBanks()){
            for (Customer customer : bank.getAllCustomers()){
                for (IAccount account : customer.getAccounts()){
                    if (account.getAccountId().equals(id)){
                        return account;
                    }
                }
            }
        }
        return null;
    }

    public Bank getBankByName(String bankName){
        return _centralBank.getBankByName(bankName);
    }

    public Customer getCustomer(Bank bank, String name, String surname){
        for (Customer customer : bank.getAllCustomers()){
            if (customer.getName().equals(name) && customer.getSurname().equals(surname)){
                return customer;
            }
        }
        return null;
    }

    public void addCustomerAddress(Bank bank, Customer customer, String address){
        bank.setAddressToCustomer(customer, address);
    }

    public void addCustomerPassportNumber(Bank bank, Customer customer, int passportNumber){
        bank.setPassportNumberToCustomer(customer, passportNumber);
    }

    public void cancelTransaction(String transactionId){
        _centralBank.cancelTransaction(transactionId);
    }

    public void setNotificationStatus(Bank bank, Customer customer, boolean newStatus){
        bank.setNotificationStatus(customer, newStatus);
    }
}
