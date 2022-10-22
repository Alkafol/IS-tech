package com.techprogs.models.Customer;

public class CustomerBuilder implements ICustomerBuilder {
    private Customer customer = new Customer();

    public CustomerBuilder() {
        reset();
    }

    public void reset() {
        customer = new Customer();
    }

    public ICustomerBuilder addName(String name) {
        customer.setName(name);
        return this;
    }

    public ICustomerBuilder addSurname(String surname) {
        customer.setSurname(surname);
        return this;
    }

    public ICustomerBuilder addAddress(String address) {
        customer.setAddress(address);
        return this;
    }

    public ICustomerBuilder addPassportNumber(int passportNumber) {
        customer.setPassportNumber(passportNumber);
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }
}
