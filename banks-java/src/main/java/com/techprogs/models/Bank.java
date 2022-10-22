package com.techprogs.models;

import java.util.*;
import com.techprogs.models.Accounts.*;
import com.techprogs.models.Customer.Customer;
import com.techprogs.models.Customer.CustomerBuilder;
import com.techprogs.models.Customer.ICustomerBuilder;
import com.techprogs.tools.*;

public class Bank {
    private List<Customer> customers = new ArrayList<>();
    private double transactionLimit;
    private double creditLimit;
    private double creditAccountCommission;
    private Map<Double, Double> depositAccountInterest;
    private double debitAccountInterest;
    private String name;

    public Bank(String bankName, double transactionLimit, double creditLimit, double creditAccountCommission, HashMap<Double, Double> depositAccountInterest, double debitAccountInterest) {
        name = bankName;
        this.transactionLimit = transactionLimit;
        this.creditLimit = creditLimit;
        this.creditAccountCommission = creditAccountCommission;
        this.depositAccountInterest = depositAccountInterest;
        this.debitAccountInterest = debitAccountInterest;
    }

    public Customer createSimpleCustomer(String name, String surname) {
        ICustomerBuilder customerBuilder = new CustomerBuilder();
        Customer customer = customerBuilder.addName(name).addSurname(surname).getCustomer();
        customers.add(customer);
        return customer;
    }

    public Customer createCustomerWithPassport(String name, String surname, int passport){
        ICustomerBuilder customerBuilder = new CustomerBuilder();
        Customer customer = customerBuilder.addName(name).addSurname(surname).addPassportNumber(passport).getCustomer();
        customers.add(customer);
        return customer;
    }

    public Customer createCustomerWithAddress(String name, String surname, String address){
        ICustomerBuilder customerBuilder = new CustomerBuilder();
        Customer customer = customerBuilder.addName(name).addSurname(surname).addAddress(address).getCustomer();
        customers.add(customer);
        return customer;
    }

    public Customer createFullCustomer(String name, String surname, int passport, String address){
        ICustomerBuilder customerBuilder = new CustomerBuilder();
        Customer customer = customerBuilder.addName(name).addSurname(surname).addPassportNumber(passport)
                .addAddress(address).getCustomer();
        customers.add(customer);
        return customer;
    }

    public Transaction transferMoney(Account accountFrom, Account accountTo, double amount) throws NotEnoughMoneyException,
            TransactionLimitException, DepositTransferException, IncorrectTransactionException, IncorrectCustomerException {
        if (!hasCustomer(accountFrom.getOwner()) || !hasCustomer(accountTo.getOwner())){
            throw new IncorrectCustomerException();
        }
        Transaction newTransaction = new Transaction(accountFrom, accountTo, amount);
        if (accountFrom.getSuspicious() && amount > transactionLimit) {
            throw new TransactionLimitException();
        } else {
            accountFrom.withdrawMoney(newTransaction);
            accountTo.addMoney(newTransaction);
        }
        return newTransaction;
    }

    public Transaction addMoney(Account accountFrom, Account accountTo, double amount) throws DepositTransferException,
            IncorrectTransactionException, IncorrectCustomerException {
        if (!hasCustomer(accountTo.getOwner())){
            throw new IncorrectCustomerException();
        }
        Transaction newTransaction = new Transaction(accountFrom, accountTo, amount);
        accountTo.addMoney(newTransaction);
        return newTransaction;
    }

    public Transaction withdrawMoney(Account accountFrom, Account accountTo, double amount) throws TransactionLimitException,
            DepositTransferException, NotEnoughMoneyException, IncorrectTransactionException, IncorrectCustomerException {
        if (!hasCustomer(accountFrom.getOwner())){
            throw new IncorrectCustomerException();
        }
        Transaction newTransaction = new Transaction(accountFrom, accountTo, amount);
        if (accountFrom.getSuspicious() && amount > transactionLimit) {
            throw new TransactionLimitException();
        } else {
            accountFrom.withdrawMoney(newTransaction);
        }
        return newTransaction;
    }

    public Account createCreditAccount(Customer customer) throws IncorrectCustomerException {
        if (!hasCustomer(customer)){
            throw  new IncorrectCustomerException();
        }
        AccountCreator accountCreator = new AccountCreator();
        Account newCreditAccount = accountCreator.createAccount(AccountType.CREDIT, customer, 0, Integer.MAX_VALUE, 0, creditLimit, creditAccountCommission);
        customer.addAccount(newCreditAccount);
        return newCreditAccount;
    }

    public Account createDebitAccount(Customer customer) throws IncorrectCustomerException {
        if (!hasCustomer(customer)){
            throw  new IncorrectCustomerException();
        }
        AccountCreator accountCreator = new AccountCreator();
        Account newDebitAccount = accountCreator.createAccount(AccountType.DEBIT, customer, 0, Integer.MAX_VALUE, debitAccountInterest, 0, 0);
        customer.addAccount(newDebitAccount);
        return newDebitAccount;
    }

    // HashMap should look like:
    // (less than) 50.000 - 3 (%)
    // (less than) 100.000 - 4 (%).

    public Account createDepositAccount(Customer customer, int dayBeforeExpiry, double balance) throws
            DepositCreationException, IncorrectCustomerException {
        if (!hasCustomer(customer)){
            throw  new IncorrectCustomerException();
        }
        List<Double> edgeValues = new ArrayList<>(depositAccountInterest.keySet().stream().toList());
        edgeValues.sort(Double::compare);
        double interest = -1;
        if (balance < edgeValues.get(0)) {
            interest = depositAccountInterest.get(edgeValues.get(0));
        } else {
            for (int i = 0; i < edgeValues.size() - 1; ++i) {
                if (balance >= edgeValues.get(i) && balance < edgeValues.get(i + 1)) {
                    interest = depositAccountInterest.get(edgeValues.get(i + 1));
                    break;
                }
            }

            if (interest == -1) {
                throw new DepositCreationException();
            }
        }

        AccountCreator accountCreator = new AccountCreator();
        Account account = accountCreator.createAccount(AccountType.DEPOSIT, customer, balance, dayBeforeExpiry,
                interest, 0, 0);
        customer.addAccount(account);
        return account;
    }

    public void setAddressToCustomer(Customer customer, String address) throws IncorrectCustomerException {
        if (!hasCustomer(customer)){
            throw  new IncorrectCustomerException();
        }
        customer.setAddress(address);
    }

    public void setPassportNumberToCustomer(Customer customer, int passportNumber) throws IncorrectCustomerException {
        if (!hasCustomer(customer)){
            throw  new IncorrectCustomerException();
        }
        customer.setPassportNumber(passportNumber);
    }

    public void changeCreditAccountsConfiguration(double newCommission, double newCreditLimit) {
        creditAccountCommission = newCommission;
        creditLimit = newCreditLimit;
        for (Customer customer : customers) {
            boolean isSendNotification = false;
            for (Account account : customer.getAccounts()) {
                if (account instanceof CreditAccount) {
                    isSendNotification = true;
                    ((CreditAccount) account).setCreditLimit(newCreditLimit);
                    ((CreditAccount) account).setCommission(newCommission);
                }
            }
            if (isSendNotification && customer.getNotificationStatus()) {
                customer.getNotifySender().sendMessage("Credit accounts configuration changed");
            }
        }
    }

    public void changeDebitAccountsConfiguration(double newInterest) {
        debitAccountInterest = newInterest;
        for (Customer customer : customers) {
            boolean isSendNotification = false;
            for (Account account : customer.getAccounts()) {
                if (account instanceof DebitAccount) {
                    isSendNotification = true;
                    ((DebitAccount) account).setInterest(newInterest);
                }
            }
            if (isSendNotification && customer.getNotificationStatus()) {
                customer.getNotifySender().sendMessage("Debit accounts configuration changed");
            }
        }
    }

    public void changeDepositAccountsConfiguration(HashMap<Double, Double> newConditions) {
        depositAccountInterest = newConditions;
        for (Customer customer : customers) {
            boolean isSendNotification = false;
            for (Account account : customer.getAccounts()) {
                if (account instanceof DepositAccount) {
                    isSendNotification = true;
                    break;
                }
            }
            if (customer.getNotificationStatus() && isSendNotification) {
                customer.getNotifySender().sendMessage("Deposit accounts configuration for NEW accounts changed");
            }
        }
    }

    public void dailyInterestCall() throws NotEnoughMoneyException, IncorrectTimeException {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                account.everydayUpdate();
            }
        }
    }

    public void monthlyInterestCall() throws IncorrectTimeException {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                account.everyMonthUpdate();
            }
        }
    }

    public void cancelTransaction(Customer customerFrom, Customer customerTo, Transaction transaction) throws
            CancellingTransactionException, IncorrectTransactionException, IncorrectCustomerException {
        if (!hasCustomer(customerFrom) || !hasCustomer(customerTo)){
            throw  new IncorrectCustomerException();
        }
        if (transaction.getAccountTo() == null || transaction.getAccountFrom() == null){
            throw new CancellingTransactionException();
        }
        else if (customerFrom == customerTo || customerFrom == null){
            cancelTransaction(customerTo, transaction);
            return;
        }
        else if (customerTo == null){
            cancelTransaction(customerFrom, transaction);
            return;
        }
        customerFrom.cancelTransaction(transaction, transaction.getAccountFrom());
        customerTo.cancelTransaction(transaction, transaction.getAccountTo());
    }

    public void cancelTransaction(Customer customer, Transaction transaction) throws CancellingTransactionException,
            IncorrectTransactionException, IncorrectCustomerException {
        if (!hasCustomer(customer)){
            throw  new IncorrectCustomerException();
        }
        if (transaction.getAccountTo() == null || transaction.getAccountFrom() == null){
            throw new CancellingTransactionException();
        }
        customer.cancelTransaction(transaction, transaction.getAccountFrom());
        customer.cancelTransaction(transaction, transaction.getAccountTo());
    }

    public void setNotificationStatus(Customer customer, boolean newStatus) throws IncorrectCustomerException {
        if (!hasCustomer(customer)){
            throw  new IncorrectCustomerException();
        }
        customer.setNotificationStatus(newStatus);
    }

    public void SetTransactionLimit(double transactionLimit) {
        transactionLimit = transactionLimit < 0 ? 0 : transactionLimit;
        this.transactionLimit = transactionLimit;
    }

    public boolean hasCustomer(Customer customer){
        for (Customer currentCustomer : customers){
            if (currentCustomer.equals(customer)){
                return true;
            }
        }

        return false;
    }


    public String getName() {
        return name;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public double getTransactionLimit() {
        return transactionLimit;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public double getCreditAccountCommission() {
        return creditAccountCommission;
    }

    public Map<Double, Double> getDepositAccountInterest() {
        return depositAccountInterest;
    }

    public double getDebitAccountInterest() {
        return debitAccountInterest;
    }
}
