package com.techprogs.models;

import com.techprogs.models.Accounts.Account;
import com.techprogs.models.Accounts.DebitAccount;
import com.techprogs.models.Customer.Customer;
import com.techprogs.tools.ExistedBankException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CentralBankTest {
    CentralBank underTest;

    @BeforeEach
    void setUp() {
        underTest = CentralBank.getInstance();
    }

    @AfterEach
    void tearDown() throws Exception {
        Field instance = CentralBank.class.getDeclaredField("centralBank");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void createBank() throws Exception {
        // given
        HashMap<Double, Double> depositInterest = new HashMap<>() {{
            put(25000.0, 4.5);
            put(100000.0, 6.0);
        }};

        // when
        underTest.createBank("Sberbank", 10000, -10000, 15, depositInterest, 4.5);

        // then
        assertEquals(1, underTest.getAllBanks().size());

    }

    @Test
    void createBankWithExistedName() throws Exception{
        // given
        HashMap<Double, Double> depositInterest = new HashMap<>() {{
            put(25000.0, 4.5);
            put(100000.0, 6.0);
        }};
        underTest.createBank("Sberbank", 10000, -10000, 15, depositInterest, 4.5);

        // when
        // then
        assertThrows(ExistedBankException.class, () -> underTest.createBank("Sberbank", 10000, -10000, 15, depositInterest, 4.5));
    }

    @Test
    void transferMoney() throws Exception {
        // given
        Bank bankFrom = mock(Bank.class);
        Bank bankTo = mock(Bank.class);
        Account debitAccountFrom = mock(DebitAccount.class);
        Account debitAccountTo = mock(DebitAccount.class);

        // when
        underTest.transferMoney(bankFrom, bankTo, debitAccountFrom, debitAccountTo, 1000);

        // then
        verify(bankFrom).withdrawMoney(debitAccountFrom, debitAccountTo, 1000);
        verify(bankTo).addMoney(debitAccountFrom, debitAccountTo, 1000);
    }

    @Test
    void dailyInterestCall() throws Exception {
        // given
        Bank sberbank = mock(Bank.class);
        Bank tinkoff = mock(Bank.class);
        Field banksInstance = CentralBank.class.getDeclaredField("banks");
        banksInstance.setAccessible(true);
        banksInstance.set(underTest, List.of(sberbank, tinkoff));

        // when
        underTest.dailyInterestCall();

        // then
        verify(sberbank).dailyInterestCall();
        verify(tinkoff).dailyInterestCall();
    }

    @Test
    void monthlyInterestCall() throws Exception {
        // given
        Bank sberbank = mock(Bank.class);
        Bank tinkoff = mock(Bank.class);
        Field banksInstance = CentralBank.class.getDeclaredField("banks");
        banksInstance.setAccessible(true);
        banksInstance.set(underTest, List.of(sberbank, tinkoff));

        // when
        underTest.monthlyInterestCall();

        // then
        verify(sberbank).monthlyInterestCall();
        verify(tinkoff).monthlyInterestCall();
    }

    @Test
    void getBankByName() throws Exception{
        // given
        Bank sberbank = mock(Bank.class);
        Bank tinkoff = mock(Bank.class);
        when(sberbank.getName()).thenReturn("Sberbank");
        when(tinkoff.getName()).thenReturn("Tinkoff");
        Field banksInstance = CentralBank.class.getDeclaredField("banks");
        banksInstance.setAccessible(true);
        banksInstance.set(underTest, List.of(tinkoff, sberbank));


        // when
        Bank bank = underTest.getBankByName("Sberbank");

        // then
        assertEquals(bank, sberbank);
    }

    @Test
    void cancelTransaction() throws Exception {
        // given
        Bank bankFrom = mock(Bank.class);
        Bank bankTo = mock(Bank.class);
        Customer customerFrom  = mock(Customer.class);
        Customer customerTo = mock(Customer.class);
        Transaction transaction = mock(Transaction.class);

        // when
        underTest.cancelTransaction(bankFrom, bankTo, customerFrom, customerTo, transaction);

        // then
        verify(bankFrom).cancelTransaction(customerFrom, transaction);
        verify(bankTo).cancelTransaction(customerTo, transaction);
    }
}