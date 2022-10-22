package com.techprogs.tools;

public class NotEnoughMoneyException extends Exception {
    private final String accountId;

    public NotEnoughMoneyException(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}