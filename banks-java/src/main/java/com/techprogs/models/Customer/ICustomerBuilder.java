package com.techprogs.models.Customer;

public interface ICustomerBuilder {
    ICustomerBuilder addName(String name);
    ICustomerBuilder addSurname(String surname);
    ICustomerBuilder addAddress(String address);
    ICustomerBuilder  addPassportNumber(int passportNumber);
    Customer getCustomer();
}
