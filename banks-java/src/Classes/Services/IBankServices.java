package Classes.Services;

import Classes.Models.Accounts.IAccount;
import Classes.Models.Bank;
import Classes.Models.Customer.Customer;

import java.util.HashMap;

public interface IBankServices {
    Bank createBank(String bankName, double transactionLimit, double creditLimit, double creditAccountCommission, HashMap<Double, Double> depositAccountInterest, double debitAccountInterest);
    Customer createCustomer(Bank bank, String name, String surname);
    IAccount createDebitAccount(Bank bank, Customer customer, int dayBeforeExpiry);
    IAccount createDepositAccount(Bank bank, Customer customer, int dayBeforeExpiry, double amountOfMoney);
    IAccount createCreditAccount(Bank bank, Customer customer, int dayBeforeExpiry);
    void scrollTime(int valueInHours);
    String addMoney(Bank bank, IAccount account, double amount);
    String withdrawMoney(Bank bank, IAccount account, double amount);
    String transferMoney(Bank bankFrom, IAccount accountFrom, Bank bankTo, IAccount accountTo, double amount);
    void changeCreditAccountConfiguration(Bank bank, double newCommission, double newCreditLimit);
    void changeDebitAccountConfiguration(Bank bank, double newInterest);
    void changeDepositAccountConfiguration(Bank bank, HashMap<Double, Double> newConditions);
    IAccount getAccountById(String id);
    Bank getBankByName(String bankName);
    Customer getCustomer(Bank bank, String name, String surname);
    void cancelTransaction(String transactionId);
    void addCustomerAddress(Bank bank, Customer customer, String address);
    void addCustomerPassportNumber(Bank bank, Customer customer, int passportNumber);
    void setNotificationStatus(Bank bank, Customer customer, boolean newStatus);
}
