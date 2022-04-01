package Classes.Models.Customer;

public class CustomerBuilder implements IBuilder{
    private Customer _customer = new Customer();

    public CustomerBuilder(){
        reset();
    }

    public void reset(){
        _customer = new Customer();
    }

    public void addName(String name){
        _customer.setName(name);
    }

    public void addSurname(String surname){
        _customer.setSurname(surname);
    }

    public void addAddress(String address){
        _customer.setAddress(address);
    }

    public void addPassportNumber(int passportNumber){
        _customer.setPassportNumber(passportNumber);
    }

    public Customer getCustomer(){
        return _customer;
    }
}
