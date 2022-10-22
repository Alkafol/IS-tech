package com.techprogs.services;

import com.techprogs.models.Accounts.Account;
import com.techprogs.models.Bank;
import com.techprogs.models.CentralBank;
import com.techprogs.models.Customer.Customer;
import com.techprogs.models.DateTime;
import com.techprogs.models.Transaction;
import com.techprogs.tools.*;

import java.util.HashMap;

public interface IBankServices {
    Bank createBank(String bankName, double transactionLimit, double creditLimit, double creditAccountCommission,
                    HashMap<Double, Double> depositAccountInterest, double debitAccountInterest) throws ExistedBankException;

    Customer createSimpleCustomer(Bank bank, String name, String surname);

    Customer createCustomerWithPassport(Bank bank, String name, String surname, int passport);

    Customer createCustomerWithAddress(Bank bank, String name, String surname, String address);

    Customer createFullCustomer(Bank bank, String name, String surname, int passport, String address);

    Account createDebitAccount(Bank bank, Customer customer) throws IncorrectCustomerException;

    Account createDepositAccount(Bank bank, Customer customer, int dayBeforeExpiry, double amountOfMoney) throws
            DepositCreationException, IncorrectCustomerException;

    Account createCreditAccount(Bank bank, Customer customer) throws IncorrectCustomerException;

    void scrollTime(int valueInHours);

    Transaction addMoney(Bank bank, Account account, double amount) throws DepositTransferException,
            IncorrectTransactionException, IncorrectCustomerException;

    Transaction withdrawMoney(Bank bank, Account account, double amount) throws DepositTransferException,
            NotEnoughMoneyException, TransactionLimitException, IncorrectTransactionException, IncorrectCustomerException;

    Transaction transferMoney(Bank bankFrom, Account accountFrom, Bank bankTo, Account accountTo, double amount) throws
            DepositTransferException, NotEnoughMoneyException, IncorrectTimeException, TransactionLimitException,
            IncorrectTransactionException, IncorrectCustomerException;

    Transaction transferMoney(Bank bank, Account accountFrom, Account accountTo, double amount) throws
            DepositTransferException, NotEnoughMoneyException, IncorrectTimeException, TransactionLimitException,
            IncorrectTransactionException, IncorrectCustomerException;

    void changeCreditAccountConfiguration(Bank bank, double newCommission, double newCreditLimit);

    void changeDebitAccountConfiguration(Bank bank, double newInterest);

    void changeDepositAccountConfiguration(Bank bank, HashMap<Double, Double> newConditions);

    Account getAccountById(String id);

    Bank getBankByName(String bankName);

    Customer getCustomer(String id);

    void cancelTransaction(Bank bankFrom, Bank bankTo, Customer customerFrom, Customer customerTo, Transaction transaction) throws
            CancellingTransactionException, IncorrectTransactionException, IncorrectCustomerException;

    void cancelTransaction(Bank bank, Customer customerFrom, Customer customerTo, Transaction transaction) throws
            CancellingTransactionException, IncorrectTransactionException, IncorrectCustomerException;

    void cancelTransaction(Bank bank, Customer customer, Transaction transaction) throws CancellingTransactionException,
            IncorrectTransactionException, IncorrectCustomerException;

    void addCustomerAddress(Bank bank, Customer customer, String address) throws IncorrectCustomerException;

    void addCustomerPassportNumber(Bank bank, Customer customer, int passportNumber) throws IncorrectCustomerException;

    void setNotificationStatus(Bank bank, Customer customer, boolean newStatus) throws IncorrectCustomerException;

    CentralBank getCentralBank();

    DateTime getCurrentDateTime();
}
