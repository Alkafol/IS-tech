package Classes.Models.Customer;

import Classes.Models.Accounts.IAccount;
import Classes.Models.NotifySender.INotifySender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private String _name;
    private String _surname;
    private int _passportNumber = 0;
    private String _address;
    private String Id = UUID.randomUUID().toString();
    private List<IAccount> _allAccounts = new ArrayList<>();
    private INotifySender _notifySender;
    private boolean _isNotification;

    public void setNotifyConfig(INotifySender notifySender){
        _notifySender = notifySender;
    }

    public void setAddress(String address){
        if (_passportNumber == 0) {
            for (IAccount account: _allAccounts) {
                account.setSuspicious(false);
            }
        }
        _address = address;
    }

    public void setPassportNumber(int passportNumber){
        if (_address == "") {
            for (IAccount account: _allAccounts) {
                account.setSuspicious(false);
            }
        }
        _passportNumber = passportNumber;
    }

    public void createAccount(IAccount account){
        if (_passportNumber == 0 || _address.equals("")){
            account.setSuspicious(true);
        }
        _allAccounts.add(account);
    }

    public void setNotificationStatus(boolean newStatus){ _isNotification = newStatus; }

    public void setName(String name) { _name = name; }

    public void setSurname(String surname){ _surname = surname; }

    public List<IAccount> getAccounts(){ return _allAccounts; }

    public boolean getNotificationStatus(){
        return _isNotification;
    }

    public INotifySender getNotifySender(){ return _notifySender; }

    public String getName() { return _name; }

    public String getSurname() { return _surname; }

    public String getAddress(){ return _address; }

    public int getPassportNumber(){ return _passportNumber; }
}

