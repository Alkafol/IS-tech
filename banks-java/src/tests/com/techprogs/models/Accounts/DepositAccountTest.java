package com.techprogs.models.Accounts;

import com.techprogs.models.Customer.Customer;
import com.techprogs.models.Transaction;
import com.techprogs.tools.DepositTransferException;
import com.techprogs.tools.IncorrectTransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DepositAccountTest {
    DepositAccount underTest;

    @BeforeEach
    void setUp() {
        Customer customer = mock(Customer.class);
        when(customer.getAddress()).thenReturn("");
        when(customer.getPassportNumber()).thenReturn(0);
        underTest = new DepositAccount(customer, 10000, 10, 35);
    }

    @Test
    void addMoney() throws Exception {
        // given
        Transaction transaction = new Transaction(null, underTest, 1000);

        // when
        // then
        assertThrows(DepositTransferException.class, () -> underTest.addMoney(transaction));
    }

    @Test
    void withdrawMoneyBeforeExpiry() {
        // given
        Transaction transaction = new Transaction(underTest, null, 1000);

        // when
        // then
        assertThrows(DepositTransferException.class, () -> underTest.withdrawMoney(transaction));
    }

    @Test
    void withdrawMoneyAfterExpiry() throws Exception{
        // given
        Customer customer = mock(Customer.class);
        when(customer.getAddress()).thenReturn("");
        when(customer.getPassportNumber()).thenReturn(0);
        underTest = new DepositAccount(customer, 10000, 10, 0);
        Transaction transaction = new Transaction(underTest, null, 1000);

        // when
        underTest.withdrawMoney(transaction);

        // then
        assertEquals(9000, underTest.getBalance());
    }

    @Test
    void withdrawMoneyAfterExpiryWithIncorrectTransaction() throws Exception{
        // given
        Transaction transaction = new Transaction(null, underTest, 1000);

        // when
        // then
        assertThrows(IncorrectTransactionException.class, () -> underTest.withdrawMoney(transaction));
    }

    @Test
    void everydayUpdate() throws Exception {
        // given
        // when
        underTest.everydayUpdate();

        // then
        assertEquals(2.74, Math.round(underTest.getAccumulatedAmount() * 100.0) / 100.0);
    }

    @Test
    void everyMonthUpdate() throws Exception {
        // given
        // when
        underTest.everydayUpdate();
        underTest.everyMonthUpdate();

        // then
        assertEquals(10002.74, Math.round(underTest.getBalance() * 100.0) / 100.0);
    }

    @Test
    void cancelTransactionFrom() throws Exception{
        // given
        Transaction transaction = new Transaction(underTest, null, 100);
        underTest.transactions.add(transaction);

        // when
        underTest.cancelTransaction(transaction);

        // then
        assertEquals(10100, underTest.getBalance());
        assertFalse(underTest.transactions.contains(transaction));
    }

    @Test
    void cancelTransactionWithAbsentTransaction(){
        // given
        Transaction transaction = new Transaction(underTest, null, 100);

        // when
        // then
        assertThrows(IncorrectTransactionException.class, () -> underTest.cancelTransaction(transaction));
    }
}