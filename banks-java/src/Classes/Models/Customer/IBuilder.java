package Classes.Models.Customer;

public interface IBuilder {
    void addName(String name);
    void addSurname(String surname);
    void addAddress(String address);
    void addPassportNumber(int passportNumber);
    Customer getCustomer();
}
