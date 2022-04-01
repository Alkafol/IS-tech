package Classes.Models;

import Classes.Models.Accounts.*;
import Classes.Models.Customer.Customer;
import Classes.Models.Customer.CustomerBuilder;
import Classes.Models.Customer.IBuilder;
import Classes.Models.NotifySender.NotifySender;
import Classes.Tools.DepositWithdrawalException;
import Classes.Tools.IncorrectTimeException;
import Classes.Tools.NotEnoughMoneyException;
import Classes.Tools.TransactionLimitException;

import java.util.*;

public class Bank {
    private List<Customer> _allCustomers = new ArrayList<>();
    private double _transactionLimit;
    private double _creditLimit;
    private double _creditAccountCommission;
    private Map<Double, Double>_depositAccountInterest;
    private double _debitAccountInterest;
    private String _name;

    public Bank(String bankName, double transactionLimit, double creditLimit, double creditAccountCommission, HashMap<Double, Double> depositAccountInterest,  double debitAccountInterest){
        _name = bankName;
        _transactionLimit = transactionLimit;
        _creditLimit = creditLimit;
        _creditAccountCommission = creditAccountCommission;
        _depositAccountInterest = depositAccountInterest;
        _debitAccountInterest = debitAccountInterest;
    }

    public Customer createCustomer(String name, String surname, int passportNumber, String address){
        IBuilder customerBuilder = new CustomerBuilder();
        customerBuilder.addName(name);
        customerBuilder.addSurname(surname);
        customerBuilder.addPassportNumber(passportNumber);
        customerBuilder.addAddress(address);
        _allCustomers.add(customerBuilder.getCustomer());
        return customerBuilder.getCustomer();
    }

    public String withdrawMoney(IAccount accountFrom, IAccount accountTo, double amount) throws NotEnoughMoneyException, IncorrectTimeException, TransactionLimitException, DepositWithdrawalException {
        Transaction newTransaction = new Transaction(accountFrom, accountTo, amount);
        if (accountFrom.getSuspicious() && amount > _transactionLimit){
            throw new TransactionLimitException();

        }
        else{
            accountFrom.withdrawMoney(newTransaction);
        }
        return newTransaction.getTransactionId();
    }

    public void transferringWithdrawal(Transaction transaction) throws NotEnoughMoneyException, IncorrectTimeException, TransactionLimitException, DepositWithdrawalException {
        if (transaction.getAccountFrom().getSuspicious() && transaction.getAmountOfMoney() > _transactionLimit){
            throw new TransactionLimitException();
        }
        else{
            transaction.getAccountFrom().withdrawMoney(transaction);
        }
    }

    public void transferringAddition(Transaction transaction) {
        transaction.getAccountTo().addMoney(transaction);
    }

    public String addMoney(IAccount accountFrom, IAccount accountTo, double amount){
        Transaction newTransaction = new Transaction(accountFrom, accountTo, amount);
        accountTo.addMoney(newTransaction);
        return newTransaction.getTransactionId();
    }

    public IAccount createCreditAccount(Customer customer, int dayBeforeExpiry){
        CreditAccountCreator creditAccountCreator = new CreditAccountCreator();
        IAccount newCreditAccount = creditAccountCreator.createNewAccount(customer, dayBeforeExpiry, _creditLimit, _creditAccountCommission, 0);
        customer.createAccount(newCreditAccount);
        return newCreditAccount;
    }

    public IAccount createDebitAccount(Customer customer, int dayBeforeExpiry){
        DebitAccountCreator debitAccountCreator = new DebitAccountCreator();
        IAccount newDebitAccount = debitAccountCreator.createNewAccount(customer, dayBeforeExpiry, 0, 0, _debitAccountInterest);
        customer.createAccount(newDebitAccount);
        return newDebitAccount;
    }

    public IAccount createDepositAccount(Customer customer, int dayBeforeExpiry, double amountOfMoney){
        List<Double> conditions = _depositAccountInterest.keySet().stream().toList();
        for (int i = 0; i < conditions.size(); i += 2){
            if (amountOfMoney > conditions.get(i)){
                double percent = _depositAccountInterest.get(conditions.get(i + 1));
                DepositAccountCreator depositAccountCreator = new DepositAccountCreator();
                IAccount newDepositAccount = depositAccountCreator.createNewAccount(customer, dayBeforeExpiry, 0, 0, percent);
                newDepositAccount.addMoney(new Transaction(null, newDepositAccount, amountOfMoney));
                customer.createAccount(newDepositAccount);
                return newDepositAccount;
            }
        }
        return null;
    }

    public void setAddressToCustomer(Customer customer, String address){
        customer.setAddress(address);
    }

    public void setPassportNumberToCustomer(Customer customer, int passportNumber){
        customer.setPassportNumber(passportNumber);
    }

    public void changeCreditAccountsConfiguration(double newCommission, double newCreditLimit){
        _creditAccountCommission = newCommission;
        _creditLimit = newCreditLimit;
        for (Customer customer : _allCustomers) {
            for (IAccount account : customer.getAccounts()){
                if (account.getClass().getSimpleName().equals("CreditAccount")){
                    account.setCreditLimit(newCreditLimit);
                    account.setCommission(newCommission);
                }
            }
        }

        for (Customer customer : _allCustomers){
            if (customer.getNotificationStatus() == true){
                customer.getNotifySender().sendMessage("Credit account configuration changed");
            }
        }
    }

    public void changeDebitAccountsConfiguration(double newInterest){
        _debitAccountInterest = newInterest;
        for (Customer customer : _allCustomers){
            for (IAccount account : customer.getAccounts()){
                if (account.getClass().getSimpleName().equals("DebitAccount")){
                    account.setInterest(newInterest);
                }
            }
        }

        for (Customer customer : _allCustomers){
            if (customer.getNotificationStatus() == true){
                customer.getNotifySender().sendMessage("Debit account configuration changed");
            }
        }
    }

    public void changeDepositAccountsConfiguration(HashMap<Double, Double> newConditions){
        _depositAccountInterest = newConditions;
        List<Double> conditions = _depositAccountInterest.keySet().stream().toList();
        for (Customer customer : _allCustomers){
            for (IAccount account : customer.getAccounts()){
                if (account.getClass().getSimpleName().equals("DepositAccount")){
                    for (int i = 0; i < conditions.size(); i += 2){
                        if (account.getBalance() > conditions.get(i)){
                            double percent = _depositAccountInterest.get(conditions.get(i + 1));
                            account.setInterest(percent);
                        }
                    }
                }
            }
        }

        for (Customer customer : _allCustomers){
            if (customer.getNotificationStatus() == true){
                customer.getNotifySender().sendMessage("Deposit account configuration changed");
            }
        }
    }

    public void dailyInterestCall() throws NotEnoughMoneyException, IncorrectTimeException {
        for (Customer customer : _allCustomers){
            for (IAccount account : customer.getAccounts()){
                account.everydayUpdate();
            }
        }
    }

    public void monthlyInterestCall() throws IncorrectTimeException {
        for (Customer customer : _allCustomers){
            for (IAccount account : customer.getAccounts()){
                account.everyMonthUpdate();
            }
        }
    }

    public void setNotificationStatus(Customer customer, boolean newStatus){
        for (Customer currentCustomer : _allCustomers){
            if (currentCustomer == customer){
                customer.setNotificationStatus(newStatus);
                customer.setNotifyConfig(new NotifySender());
            }
        }
    }

    public String getName(){ return _name; }
    public List<Customer> getAllCustomers() { return _allCustomers; }
}
