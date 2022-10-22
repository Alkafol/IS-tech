package com.techprogs.models;

import com.techprogs.models.Accounts.*;
import com.techprogs.models.Customer.Customer;
import com.techprogs.models.NotifySender.NotifySender;
import com.techprogs.tools.TransactionLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankTest {
    Bank underTest;

    @BeforeEach
    void setUp() {
        HashMap<Double, Double> depositInterest = new HashMap<>() {{
            put(25000.0, 4.5);
            put(100000.0, 6.0);
        }};
        underTest = new Bank("Sberbank", 10000, -10000, 10, depositInterest, 4);
    }

    @Test
    void createSimpleCustomer() {
        // given
        // when
        Customer customer = underTest.createSimpleCustomer("Ivan", "Ivanov");

        // then
        assertTrue(underTest.getCustomers().contains(customer));
    }

    @Test
    void createCustomerWithPassport() {
        // given
        // when
        Customer customer = underTest.createCustomerWithPassport("Ivan", "Ivanov", 224224);

        // then
        assertTrue(underTest.getCustomers().contains(customer));
    }

    @Test
    void createCustomerWithAddress() {
        // given
        // when
        Customer customer = underTest.createCustomerWithAddress("Ivan", "Ivanov", "St.Petersburg");

        // then
        assertTrue(underTest.getCustomers().contains(customer));
    }

    @Test
    void createFullCustomer() {
        // given
        // when
        Customer customer = underTest.createFullCustomer("Ivan", "Ivanov", 224224, "St.Petersburg");

        // then
        assertTrue(underTest.getCustomers().contains(customer));
    }

    @Test
    void addMoney() throws Exception {
        // given
        Account creditAccount = mock(CreditAccount.class);

        // when
        underTest.addMoney(null, creditAccount, 1000);

        // then
        verify(creditAccount).addMoney(Mockito.any(Transaction.class));
    }

    @Test
    void withdrawMoneyWithoutSuspicious() throws Exception {
        // given
        Account creditAccount = mock(CreditAccount.class);

        // when
        underTest.withdrawMoney(creditAccount, null, 1000);

        // then
        verify(creditAccount).withdrawMoney(Mockito.any(Transaction.class));
    }

    @Test
    void withdrawMoneyWithSuspicious() throws Exception{
        // given
        Account creditAccount = mock(CreditAccount.class);
        when(creditAccount.getSuspicious()).thenReturn(true);

        // when
        // then
        assertThrows(TransactionLimitException.class, () -> underTest.withdrawMoney(creditAccount, null, 11000));
    }

    @Test
    void changeCreditAccountsConfigurationWithoutNotification() throws Exception {
        // given
        Customer customer = mock(Customer.class);
        CreditAccount creditAccount = mock(CreditAccount.class);
        Field customersField = underTest.getClass().getDeclaredField("customers");
        customersField.setAccessible(true);
        customersField.set(underTest, List.of(customer));

        when(customer.getAccounts()).thenReturn(List.of(creditAccount));
        when(customer.getNotificationStatus()).thenReturn(false);

        // when
        underTest.changeCreditAccountsConfiguration(11, -1000);

        // then
        assertEquals(11, underTest.getCreditAccountCommission());
        assertEquals(-1000, underTest.getCreditLimit());
        verify(creditAccount).setCreditLimit(-1000);
        verify(creditAccount).setCommission(11);
    }

    @Test
    void changeCreditAccountsConfigurationWithNotification() throws Exception{
        // given
        Customer customer = mock(Customer.class);
        CreditAccount creditAccount = mock(CreditAccount.class);
        NotifySender notifySender = mock(NotifySender.class);
        Field customersField = underTest.getClass().getDeclaredField("customers");
        customersField.setAccessible(true);
        customersField.set(underTest, List.of(customer));

        when(customer.getAccounts()).thenReturn(List.of(creditAccount));
        when(customer.getNotificationStatus()).thenReturn(true);
        when(customer.getNotifySender()).thenReturn(notifySender);

        // when
        underTest.changeCreditAccountsConfiguration(11, -1000);

        // then
        assertEquals(11, underTest.getCreditAccountCommission());
        assertEquals(-1000, underTest.getCreditLimit());
        verify(creditAccount).setCreditLimit(-1000);
        verify(creditAccount).setCommission(11);
        verify(notifySender).sendMessage(Mockito.anyString());
    }

    @Test
    void changeDebitAccountsConfigurationWithoutNotification() throws Exception {
        // given
        Customer customer = mock(Customer.class);
        DebitAccount debitAccount = mock(DebitAccount.class);
        Field customersField = underTest.getClass().getDeclaredField("customers");
        customersField.setAccessible(true);
        customersField.set(underTest, List.of(customer));

        when(customer.getAccounts()).thenReturn(List.of(debitAccount));
        when(customer.getNotificationStatus()).thenReturn(false);

        // when
        underTest.changeDebitAccountsConfiguration(6.35);

        // then
        assertEquals(6.35, underTest.getDebitAccountInterest());
        verify(debitAccount).setInterest(6.35);
    }

    @Test
    void changeDebitAccountsConfigurationWithNotification() throws Exception {
        // given
        Customer customer = mock(Customer.class);
        DebitAccount debitAccount = mock(DebitAccount.class);
        NotifySender notifySender = mock(NotifySender.class);
        Field customersField = underTest.getClass().getDeclaredField("customers");
        customersField.setAccessible(true);
        customersField.set(underTest, List.of(customer));

        when(customer.getAccounts()).thenReturn(List.of(debitAccount));
        when(customer.getNotificationStatus()).thenReturn(true);
        when(customer.getNotifySender()).thenReturn(notifySender);

        // when
        underTest.changeDebitAccountsConfiguration(6.35);

        // then
        assertEquals(6.35, underTest.getDebitAccountInterest());
        verify(debitAccount).setInterest(6.35);
        verify(notifySender).sendMessage(Mockito.anyString());
    }

    @Test
    void changeDepositAccountsConfigurationWithoutNotification() throws Exception {
        // given
        Customer customer = mock(Customer.class);
        DepositAccount depositAccount = mock(DepositAccount.class);
        Field customersField = underTest.getClass().getDeclaredField("customers");
        customersField.setAccessible(true);
        customersField.set(underTest, List.of(customer));
        HashMap<Double, Double> depositInterest = new HashMap<>() {{
            put(25000.0, 4.5);
            put(100000.0, 6.0);
        }};

        when(customer.getAccounts()).thenReturn(List.of(depositAccount));
        when(customer.getNotificationStatus()).thenReturn(false);

        // when
        underTest.changeDepositAccountsConfiguration(depositInterest);

        // then
        assertEquals(depositInterest, underTest.getDepositAccountInterest());
    }

    @Test
    void changeDepositAccountConfigurationWithNotification() throws Exception{
        // given
        Customer customer = mock(Customer.class);
        DepositAccount depositAccount = mock(DepositAccount.class);
        NotifySender notifySender = mock(NotifySender.class);
        Field customersField = underTest.getClass().getDeclaredField("customers");
        customersField.setAccessible(true);
        customersField.set(underTest, List.of(customer));
        HashMap<Double, Double> depositInterest = new HashMap<>() {{
            put(25000.0, 4.5);
            put(100000.0, 6.0);
        }};

        when(customer.getAccounts()).thenReturn(List.of(depositAccount));
        when(customer.getNotificationStatus()).thenReturn(true);
        when(customer.getNotifySender()).thenReturn(notifySender);

        // when
        underTest.changeDepositAccountsConfiguration(depositInterest);

        // then
        assertEquals(depositInterest, underTest.getDepositAccountInterest());
        verify(notifySender).sendMessage(Mockito.anyString());
    }

    @Test
    void transferMoneyWithoutSuspicious() throws Exception{
        // given
        DebitAccount accountFrom = mock(DebitAccount.class);
        DebitAccount accountTo = mock(DebitAccount.class);
        when(accountFrom.getSuspicious()).thenReturn(false);

        // when
        underTest.transferMoney(accountFrom, accountTo, 1000);

        // then
        verify(accountFrom).withdrawMoney(Mockito.any(Transaction.class));
        verify(accountTo).addMoney(Mockito.any(Transaction.class));
    }

    @Test
    void transferMoneyWithSuspiciousAndSmallAmount() throws Exception{
        // given
        DebitAccount accountFrom = mock(DebitAccount.class);
        DebitAccount accountTo = mock(DebitAccount.class);
        when(accountFrom.getSuspicious()).thenReturn(true);

        // when
        underTest.transferMoney(accountFrom, accountTo, 1000);

        // then
        verify(accountFrom).withdrawMoney(Mockito.any(Transaction.class));
        verify(accountTo).addMoney(Mockito.any(Transaction.class));
    }

    @Test
    void transferMoneyWithSuspiciousAndBigAmount() throws Exception{
        // given
        DebitAccount accountFrom = mock(DebitAccount.class);
        DebitAccount accountTo = mock(DebitAccount.class);
        when(accountFrom.getSuspicious()).thenReturn(true);

        // when
        // then
        assertThrows(TransactionLimitException.class, () -> underTest.transferMoney(accountFrom, accountTo, 11000));
    }

    @Test
    void dailyInterestCall() throws Exception {
        // given
        Customer customer = mock(Customer.class);
        DepositAccount depositAccount = mock(DepositAccount.class);
        CreditAccount creditAccount = mock(CreditAccount.class);
        Field customersField = underTest.getClass().getDeclaredField("customers");
        customersField.setAccessible(true);
        customersField.set(underTest, List.of(customer));

        when(customer.getAccounts()).thenReturn(List.of(depositAccount, creditAccount));

        // when
        underTest.dailyInterestCall();

        // then
        verify(depositAccount).everydayUpdate();
        verify(creditAccount).everydayUpdate();
    }

    @Test
    void monthlyInterestCall() throws Exception {
        // given
        Customer customer = mock(Customer.class);
        DepositAccount depositAccount = mock(DepositAccount.class);
        CreditAccount creditAccount = mock(CreditAccount.class);
        Field customersField = underTest.getClass().getDeclaredField("customers");
        customersField.setAccessible(true);
        customersField.set(underTest, List.of(customer));

        when(customer.getAccounts()).thenReturn(List.of(depositAccount, creditAccount));

        // when
        underTest.monthlyInterestCall();

        // then
        verify(depositAccount).everyMonthUpdate();
        verify(creditAccount).everyMonthUpdate();
    }
}