package com.techprogs.services;

import com.techprogs.models.Accounts.Account;
import com.techprogs.models.Accounts.CreditAccount;
import com.techprogs.models.Bank;
import com.techprogs.models.CentralBank;
import com.techprogs.models.Customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BankServicesTest {
    BankServices underTest;
    public static final int HOURS_IN_DAY = 24;

    @BeforeEach
    void setUp() {
        underTest = new BankServices();
    }

    @Test
    void scrollTime() throws Exception{
        // given
        CentralBank centralBank = mock(CentralBank.class);
        Field instance = BankServices.class.getDeclaredField("centralBank");
        instance.setAccessible(true);
        instance.set(underTest, centralBank);

        // when
        underTest.scrollTime(HOURS_IN_DAY * 40);

        // then
        verify(centralBank, times(40)).dailyInterestCall();
        verify(centralBank).monthlyInterestCall();
    }

    @Test
    void getAccountById() throws Exception{
        // given
        CentralBank centralBank = mock(CentralBank.class);
        Field instance = BankServices.class.getDeclaredField("centralBank");
        instance.setAccessible(true);
        instance.set(underTest, centralBank);

        Bank bank = mock(Bank.class);
        Customer customerAnton = mock(Customer.class);
        Customer customerAndrey = mock(Customer.class);
        Account creditAccount = mock(CreditAccount.class);

        when(centralBank.getAllBanks()).thenReturn(List.of(bank));
        when(bank.getCustomers()).thenReturn(List.of(customerAndrey, customerAnton));
        when(customerAnton.getAccounts()).thenReturn(List.of(creditAccount));
        when(creditAccount.getAccountId()).thenReturn("111111");

        // when
        Account account =underTest.getAccountById("111111");

        // then
        assertEquals(creditAccount, account);
    }

    @Test
    void getCustomer() throws Exception{
        // given
        CentralBank centralBank = mock(CentralBank.class);
        Field instance = BankServices.class.getDeclaredField("centralBank");
        instance.setAccessible(true);
        instance.set(underTest, centralBank);

        Bank bank = mock(Bank.class);
        Customer customerAnton = mock(Customer.class);

        when(centralBank.getAllBanks()).thenReturn(List.of(bank));
        when(customerAnton.getId()).thenReturn("111111");
        when(bank.getCustomers()).thenReturn(List.of(customerAnton));

        // when
        Customer customer = underTest.getCustomer("111111");

        // then
        assertEquals(customer, customerAnton);
    }
}
