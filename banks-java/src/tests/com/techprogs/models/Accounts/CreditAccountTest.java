package com.techprogs.models.Accounts;

import com.techprogs.models.Customer.Customer;
import com.techprogs.models.Transaction;
import com.techprogs.tools.IncorrectTransactionException;
import com.techprogs.tools.NotEnoughMoneyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreditAccountTest {
    CreditAccount underTest;

    @BeforeEach
    void setUp() {
        Customer customer = mock(Customer.class);
        when(customer.getAddress()).thenReturn("");
        when(customer.getPassportNumber()).thenReturn(0);
        underTest = new CreditAccount(customer, -1000, -10000, 10);
    }

    @Test
    void withdrawMoney() throws Exception{
        // given
        Transaction transaction = new Transaction(underTest, null, 1000);
        underTest.transactions.add(transaction);

        // when
        underTest.withdrawMoney(transaction);

        // then
        assertEquals(-2000, underTest.getBalance());
    }

    @Test
    void withdrawMoneyWithIncorrectTransaction(){
        // given
        Transaction transaction = new Transaction(null, underTest, 1000);
        underTest.transactions.add(transaction);

        // when
        // then
        assertThrows(IncorrectTransactionException.class, () -> underTest.withdrawMoney(transaction));
    }

    @Test
    void withdrawMoneyLessThanCreditLimit(){
        // given
        Transaction transaction = new Transaction(underTest, null, 10000);
        underTest.transactions.add(transaction);

        // when
        // then
        assertThrows(NotEnoughMoneyException.class, () -> underTest.withdrawMoney(transaction));
    }

    @Test
    void everydayUpdateWithPositiveBalance() throws Exception {
        // given
        Customer customer = mock(Customer.class);
        when(customer.getAddress()).thenReturn("");
        when(customer.getPassportNumber()).thenReturn(0);
        underTest = new CreditAccount(customer, 1000, -10000, 10);

        // when
        underTest.everydayUpdate();

        // then
        assertEquals(1000, underTest.getBalance());
    }

    @Test
    void everyDayUpdateWithNegativeBalance() throws Exception {
        // given
        // when
        underTest.everydayUpdate();

        // then
        assertEquals(-1010, underTest.getBalance());
    }

    @Test
    void cancelTransactionFrom() throws Exception{
        // given
        Transaction transaction = new Transaction(underTest, null, 1000);
        underTest.transactions.add(transaction);

        // when
        underTest.cancelTransaction(transaction);

        // then
        assertEquals(0, underTest.getBalance());
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
        assertEquals(-2000, underTest.getBalance());
        assertFalse(underTest.transactions.contains(transaction));
    }

    @Test
    void addMoney() throws Exception{
        // given
        Transaction transaction = new Transaction(null, underTest, 1000);
        underTest.transactions.add(transaction);

        // when
        underTest.addMoney(transaction);

        // then
        assertEquals(0, underTest.getBalance());
    }

    @Test
    void addMoneyWithIncorrectTransaction() throws Exception{
        // given
        Transaction transaction = new Transaction(underTest, null, 1000);
        underTest.transactions.add(transaction);

        // when
        // then
        assertThrows(IncorrectTransactionException.class, () -> underTest.addMoney(transaction));
    }

    @Test
    void cancelTransactionWithAbsentTransaction() throws Exception{
        // given
        Transaction transaction = new Transaction(null, underTest, 1000);

        // when
        // then
        assertThrows(IncorrectTransactionException.class, () -> underTest.cancelTransaction(transaction));
    }
}