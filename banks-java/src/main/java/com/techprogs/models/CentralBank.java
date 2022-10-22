package com.techprogs.models;

import com.techprogs.models.Accounts.Account;
import com.techprogs.models.Customer.Customer;
import com.techprogs.tools.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CentralBank {
    private List<Bank> banks = new ArrayList<>();
    private static CentralBank centralBank;

    private CentralBank() {
    }

    public Bank createBank(String bankName, double transactionLimit, double creditLimit, double creditAccountCommission,
                           HashMap<Double, Double> depositAccountInterest, double debitAccountInterest) throws ExistedBankException {
        if (getBankByName(bankName) != null) {
            throw new ExistedBankException();
        } else {
            Bank newBank = new Bank(bankName, transactionLimit, creditLimit, creditAccountCommission,
                    depositAccountInterest, debitAccountInterest);
            banks.add(newBank);
            return newBank;
        }
    }

    public Transaction transferMoney(Bank bankFrom, Bank bankTo, Account accountFrom, Account accountTo, double amount) throws
            NotEnoughMoneyException, TransactionLimitException, DepositTransferException,
            IncorrectTransactionException, IncorrectCustomerException {
        Transaction newTransaction = new Transaction(accountFrom, accountTo, amount);
        bankFrom.withdrawMoney(accountFrom, accountTo, amount);
        bankTo.addMoney(accountFrom, accountTo, amount);
        return newTransaction;
    }

    public void dailyInterestCall() throws NotEnoughMoneyException, IncorrectTimeException {
        for (Bank bank : banks) {
            bank.dailyInterestCall();
        }
    }

    public void monthlyInterestCall() throws IncorrectTimeException {
        for (Bank bank : banks) {
            bank.monthlyInterestCall();
        }
    }

    public static CentralBank getInstance() {
        if (centralBank == null) {
            centralBank = new CentralBank();
        }
        return centralBank;
    }

    public Bank getBankByName(String name) {
        for (Bank bank : banks) {
            if (bank.getName().equals(name)) {
                return bank;
            }
        }
        return null;
    }

    public void cancelTransaction(Bank bankFrom, Bank bankTo, Customer customerFrom, Customer customerTo, Transaction transaction) throws
            CancellingTransactionException, IncorrectTransactionException, IncorrectCustomerException {
        bankFrom.cancelTransaction(customerFrom, transaction);
        bankTo.cancelTransaction(customerTo, transaction);
    }

    public List<Bank> getAllBanks() {
        return banks;
    }
}
