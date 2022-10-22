package com.techprogs.models.Customer;

import com.techprogs.models.Accounts.Account;
import com.techprogs.models.NotifySender.INotifySender;
import com.techprogs.models.Transaction;
import com.techprogs.tools.IncorrectTransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private String name;
    private String surname;
    private int passportNumber = 0;
    private String address = "";
    private final String id = UUID.randomUUID().toString();
    private List<Account> accounts = new ArrayList<>();
    private INotifySender notifySender;
    private boolean isNotification;

    public void setNotifyConfig(INotifySender notifySender) {
        this.notifySender = notifySender;
    }

    public void setAddress(String address) {
        if (passportNumber != 0) {
            for (Account account : accounts) {
                account.setSuspicious(false);
            }
        }
        this.address = address;
    }

    public void setPassportNumber(int passportNumber) {
        if (!address.equals("")) {
            for (Account account : accounts) {
                account.setSuspicious(false);
            }
        }
        this.passportNumber = passportNumber;
    }

    public String getId() {
        return id;
    }

    public boolean isNotification() {
        return isNotification;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void cancelTransaction(Transaction transaction, Account account) throws IncorrectTransactionException {
        account.cancelTransaction(transaction);
    }

    public void setNotificationStatus(boolean newStatus) {
        isNotification = newStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public boolean getNotificationStatus() {
        return isNotification;
    }

    public INotifySender getNotifySender() {
        return notifySender;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public int getPassportNumber() {
        return passportNumber;
    }
}

