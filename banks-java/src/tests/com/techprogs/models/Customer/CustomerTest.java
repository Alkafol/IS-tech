package com.techprogs.models.Customer;

import com.techprogs.models.Accounts.DebitAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void setAddressWithPassport() {
        // given
        DebitAccount debitAccount = mock(DebitAccount.class);
        CustomerBuilder customerBuilder = new CustomerBuilder();
        Customer customer = customerBuilder.addName("Ivan").addSurname("Ivanov").addPassportNumber(224224).getCustomer();
        customer.addAccount(debitAccount);

        // when
        customer.setAddress("St.Petersburg");

        // then
        verify(debitAccount).setSuspicious(false);
    }


    @Test
    void setPassportWithAddress() {
        // given
        DebitAccount debitAccount = mock(DebitAccount.class);
        CustomerBuilder customerBuilder = new CustomerBuilder();
        Customer customer = customerBuilder.addName("Ivan").addSurname("Ivanov").addAddress("St.Petersburg").getCustomer();
        customer.addAccount(debitAccount);

        // when
        customer.setPassportNumber(224224);

        // then
        verify(debitAccount).setSuspicious(false);
    }
}