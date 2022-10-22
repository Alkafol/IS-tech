package com.techprogs.services;

import com.techprogs.models.Accounts.Account;
import com.techprogs.models.Bank;
import com.techprogs.models.CentralBank;
import com.techprogs.models.Customer.Customer;
import com.techprogs.models.DateTime;
import com.techprogs.models.Transaction;
import com.techprogs.tools.*;

import java.util.HashMap;

public class BankServices implements IBankServices {
    private CentralBank centralBank;
    private final DateTime currentDateTime; // in minutes from the beginning

    public BankServices() {
        centralBank = CentralBank.getInstance();
        currentDateTime = new DateTime();
    }

    public Bank createBank(String bankName, double transactionLimit, double creditLimit, double creditAccountCommission,
                           HashMap<Double, Double> depositAccountInterest, double debitAccountInterest) throws ExistedBankException {
        return centralBank.createBank(bankName, transactionLimit, creditLimit, creditAccountCommission, depositAccountInterest,
                debitAccountInterest);
    }

    public Customer createSimpleCustomer(Bank bank, String name, String surname) {
        return bank.createSimpleCustomer(name, surname);
    }

    public Customer createCustomerWithPassport(Bank bank, String name, String surname, int passport){
        return bank.createCustomerWithPassport(name, surname, passport);
    }

    public Customer createCustomerWithAddress(Bank bank, String name, String surname, String address){
        return bank.createCustomerWithAddress(name, surname, address);
    }

    public Customer createFullCustomer(Bank bank, String name, String surname, int passport, String address){
        return bank.createFullCustomer(name, surname, passport, address);
    }

    public Account createDebitAccount(Bank bank, Customer customer) throws IncorrectCustomerException {
        return bank.createDebitAccount(customer);
    }

    public Account createDepositAccount(Bank bank, Customer customer, int dayBeforeExpiry, double amountOfMoney) throws
            DepositCreationException, IncorrectCustomerException {
        return bank.createDepositAccount(customer, dayBeforeExpiry, amountOfMoney);
    }

    public Account createCreditAccount(Bank bank, Customer customer) throws IncorrectCustomerException {
        return bank.createCreditAccount(customer);
    }

    public void scrollTime(int valueInHours) {
        try {
            int daysPassed = valueInHours / 24;
            currentDateTime.scrollTimeInHours(valueInHours);
            for (int i = 0; i < daysPassed; i++) {
                centralBank.dailyInterestCall();
                if ((i + 1) % 30 == 0) {
                    centralBank.monthlyInterestCall();
                }
            }
        }
        catch (NotEnoughMoneyException e) {
            System.out.println("Account " + e.getAccountId() + " has not enough money");
        } catch (IncorrectTimeException e) {
            System.out.println("Incorrect time");
        }
    }

    public Transaction addMoney(Bank bank, Account account, double amount) throws DepositTransferException,
            IncorrectTransactionException, IncorrectCustomerException {
        return bank.addMoney(null, account, amount);
    }

    public Transaction withdrawMoney(Bank bank, Account account, double amount) throws DepositTransferException,
            NotEnoughMoneyException, TransactionLimitException, IncorrectTransactionException, IncorrectCustomerException {
        return bank.withdrawMoney(account,null, amount);
    }

    public Transaction transferMoney(Bank bankFrom, Account accountFrom, Bank bankTo, Account accountTo, double amount) throws
            DepositTransferException, NotEnoughMoneyException, TransactionLimitException,
            IncorrectTransactionException, IncorrectCustomerException {
        return centralBank.transferMoney(bankFrom, bankTo, accountFrom, accountTo, amount);
    }

    public Transaction transferMoney(Bank bank, Account accountFrom, Account accountTo, double amount) throws
            DepositTransferException, NotEnoughMoneyException, IncorrectTimeException, TransactionLimitException,
            IncorrectTransactionException, IncorrectCustomerException {
        return bank.transferMoney(accountFrom, accountTo, amount);
    }

    public void changeCreditAccountConfiguration(Bank bank, double newCommission, double newCreditLimit) {
        bank.changeCreditAccountsConfiguration(newCommission, newCreditLimit);
    }

    public void changeDebitAccountConfiguration(Bank bank, double newInterest) {
        bank.changeDebitAccountsConfiguration(newInterest);
    }

    public void changeDepositAccountConfiguration(Bank bank, HashMap<Double, Double> newConditions) {
        bank.changeDepositAccountsConfiguration(newConditions);
    }

    public Account getAccountById(String id) {
        for (Bank bank : centralBank.getAllBanks()) {
            for (Customer customer : bank.getCustomers()) {
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountId().equals(id)) {
                        return account;
                    }
                }
            }
        }
        return null;
    }

    public Bank getBankByName(String bankName) {
        return centralBank.getBankByName(bankName);
    }

    public Customer getCustomer(String id) {
        for (Bank bank : centralBank.getAllBanks()) {
            for (Customer customer : bank.getCustomers()) {
                if (customer.getId().equals(id)) {
                    return customer;
                }
            }
        }
        return null;
    }

    public void addCustomerAddress(Bank bank, Customer customer, String address) throws IncorrectCustomerException {
        bank.setAddressToCustomer(customer, address);
    }

    public void addCustomerPassportNumber(Bank bank, Customer customer, int passportNumber) throws IncorrectCustomerException {
        bank.setPassportNumberToCustomer(customer, passportNumber);
    }

    public void cancelTransaction(Bank bankFrom, Bank bankTo, Customer customerFrom, Customer customerTo, Transaction transaction) throws
            CancellingTransactionException, IncorrectTransactionException, IncorrectCustomerException {
        if (bankFrom == bankTo){
            bankFrom.cancelTransaction(customerFrom, customerTo, transaction);
        }
        else {
            centralBank.cancelTransaction(bankFrom, bankTo, customerFrom, customerTo, transaction);
        }
    }

    public void cancelTransaction(Bank bank, Customer customerFrom, Customer customerTo, Transaction transaction) throws
            CancellingTransactionException, IncorrectTransactionException, IncorrectCustomerException {
        bank.cancelTransaction(customerFrom, customerTo, transaction);
    }

    public void cancelTransaction(Bank bank, Customer customer, Transaction transaction) throws CancellingTransactionException,
            IncorrectTransactionException, IncorrectCustomerException {
        bank.cancelTransaction(customer, transaction);
    }

    public void setNotificationStatus(Bank bank, Customer customer, boolean newStatus) throws IncorrectCustomerException {
        bank.setNotificationStatus(customer, newStatus);
    }

    public CentralBank getCentralBank() {
        return centralBank;
    }

    public DateTime getCurrentDateTime() {
        return currentDateTime;
    }
}
