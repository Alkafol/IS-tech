package com.techprogs.models.Accounts;

import com.techprogs.models.Customer.Customer;
import com.techprogs.models.Transaction;
import com.techprogs.tools.IncorrectTransactionException;
import com.techprogs.tools.NotEnoughMoneyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DebitAccountTest {
    DebitAccount underTest;

    @BeforeEach
    void setUp() {
        Customer customer = mock(Customer.class);
        when(customer.getAddress()).thenReturn("");
        when(customer.getPassportNumber()).thenReturn(0);
        underTest = new DebitAccount(customer, 10000, 10);
    }


    @Test
    void withdrawMoney() throws Exception{
        // given
        Transaction transaction = new Transaction(underTest, null, 1000);
        underTest.transactions.add(transaction);

        // when
        underTest.withdrawMoney(transaction);

        // then
        assertEquals(9000, underTest.getBalance());
    }

    @Test
    void withdrawTooMuchMoney(){
        // given
        Transaction transaction = new Transaction(underTest, null, 11000);
        underTest.transactions.add(transaction);

        // when
        // then
        assertThrows(NotEnoughMoneyException.class, () -> underTest.withdrawMoney(transaction));
    }

    @Test
    void withdrawMoneyWithIncorrectTransaction(){
        // given
        Transaction transaction = new Transaction(null, underTest, 11000);
        underTest.transactions.add(transaction);

        // when
        // then
        assertThrows(IncorrectTransactionException.class, () -> underTest.withdrawMoney(transaction));
    }

    @Test
    void addMoney() throws Exception{
        // given
        Transaction transaction = new Transaction(null, underTest, 10000);

        // when
        underTest.addMoney(transaction);

        // then
        assertEquals(20000, underTest.getBalance());
    }

    @Test
    void addMoneyWithIncorrectTransaction(){
        // given
        Transaction transaction = new Transaction(underTest, null, 10000);

        // when
        // then
        assertThrows(IncorrectTransactionException.class, () -> underTest.addMoney(transaction));
    }

    @Test
    void everydayUpdate() {
        // given
        // when
        underTest.everydayUpdate();

        // then
        assertEquals(2.74, Math.round(underTest.getAccumulatedAmount() * 100.0) / 100.0 );
    }

    @Test
    void everyMonthUpdate() {
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
        Transaction transaction = new Transaction(underTest, null, 1000);
        underTest.transactions.add(transaction);

        // when
        underTest.cancelTransaction(transaction);

        // then
        assertEquals(11000, underTest.getBalance());
        assertFalse(underTest.transactions.contains(transaction));
    }

    @Test
    void cancelTransactionTo() throws Exception{
        // given
        Transaction transaction = new Transaction(null, underTest, 1000);
        underTest.transactions.add(transaction);

        // when
        underTest.cancelTransaction(transaction);

        // then
        assertEquals(9000, underTest.getBalance());
        assertFalse(underTest.transactions.contains(transaction));
    }

    @Test
    void cancelTransactionWithAbsentTransaction(){
        // given
        Transaction transaction = new Transaction(null, underTest, 1000);

        // when
        // then
        assertThrows(IncorrectTransactionException.class, () -> underTest.cancelTransaction(transaction));
    }
}