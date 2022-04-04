package Tests;

import Classes.Models.Accounts.IAccount;
import Classes.Models.Bank;
import Classes.Models.Customer.Customer;
import Classes.Services.BankServices;
import Classes.Services.IBankServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests{
    private IBankServices _bankService;
    private Bank sberBank;
    private Bank tinkoff;
    private Customer anton;
    private Customer kirill;
    private IAccount antonCreditAccount;
    private IAccount antonDebitAccount;
    private IAccount antonDepositAccount;
    private IAccount kirillCreditAccount;
    private IAccount kirillDebitAccount;
    private IAccount kirillDepositAccount;

    @BeforeEach
    public void startsTesting() {

            _bankService = new BankServices();
            HashMap<Double, Double> depositSberInterest = new HashMap<Double, Double>() {{
                    put(25000.0, 4.5);
                    put(100000.0, 6.0);
            }};
            HashMap<Double, Double> depositTinkoffInterest = new HashMap<Double, Double>() {{
                    put(50000.0, 5.0);
                    put(100000.0, 7.0);
            }};

            sberBank = _bankService.createBank("Sber", 10000, -10000, 10, depositSberInterest, 4);
            anton = _bankService.createCustomer(sberBank, "Anton", "Romanov");
            antonCreditAccount = _bankService.createCreditAccount(sberBank, anton, 100);
            antonDebitAccount = _bankService.createDebitAccount(sberBank, anton, 100);
            antonDepositAccount = _bankService.createDepositAccount(sberBank, anton, 100, 20000);
            _bankService.addMoney(sberBank, antonDebitAccount, 30000);
            _bankService.addMoney(sberBank, antonCreditAccount, 5000);

            tinkoff = _bankService.createBank("Tinkoff", 15000, -40000, 15, depositTinkoffInterest, 5);
            kirill = _bankService.createCustomer(tinkoff, "Kirill", "Popov");
            kirillCreditAccount = _bankService.createCreditAccount(tinkoff, kirill, 100);
            kirillDebitAccount = _bankService.createDebitAccount(tinkoff, kirill, 100);
            kirillDepositAccount = _bankService.createDepositAccount(tinkoff, kirill, 100, 90000);
            _bankService.addMoney(tinkoff, kirillDebitAccount, 20000);
            _bankService.addMoney(tinkoff, kirillCreditAccount, 5000);
    }

    @Test
    public void TransferTest() {
            _bankService.transferMoney(tinkoff, kirillDebitAccount, sberBank, antonDebitAccount, 1000);
            assertEquals(antonDebitAccount.getBalance(), 31000);
            assertEquals( kirillDebitAccount.getBalance(), 19000);
    }

    @Test
    public void SuspiciousTransactionCheck() {
            _bankService.transferMoney(sberBank, antonDebitAccount, tinkoff, kirillDebitAccount, 11000);
            assertEquals(antonDebitAccount.getBalance(),30000);
            assertEquals(kirillDebitAccount.getBalance(), 20000);
    }

    @Test
    public void SuspiciousAfterAddingPassportCheck() {
            _bankService.addCustomerPassportNumber(sberBank, anton, 333765);
            _bankService.transferMoney(sberBank, antonDebitAccount, tinkoff, kirillDebitAccount, 11000);
            assertEquals(antonDebitAccount.getBalance(), 19000);
            assertEquals(kirillDebitAccount.getBalance(), 31000);
    }

    @Test
    public void NegativeDebitBalanceCheck() {
            _bankService.transferMoney(sberBank, antonDebitAccount, tinkoff, kirillDebitAccount, 21000);
            assertEquals(antonDebitAccount.getBalance(), 30000);
    }

    @Test
    public void WithdrawalCheck() {
            _bankService.withdrawMoney(tinkoff, kirillDebitAccount, 1000);
            assertEquals(kirillDebitAccount.getBalance(), 19000);
    }

    @Test
    public void DepositWithdrawalCheck() {
            _bankService.withdrawMoney(tinkoff, kirillDepositAccount, 1000);
            assertEquals(kirillDepositAccount.getBalance(), 90000);
    }

    @Test
    public void CancellingTransactionCheck() {
            String transactionId = _bankService.transferMoney(sberBank, antonDebitAccount, tinkoff, kirillDebitAccount, 5000);
            assertEquals(antonDebitAccount.getBalance(), 25000);
            assertEquals(kirillDebitAccount.getBalance(), 25000);
            _bankService.cancelTransaction(transactionId);
            assertEquals(antonDebitAccount.getBalance(), 30000);
            assertEquals(kirillDebitAccount.getBalance(), 20000);
    }

    @Test
    public void TimeScrollingCheck() {
            _bankService.setNotificationStatus(tinkoff, kirill, true);
            _bankService.withdrawMoney(tinkoff, kirillCreditAccount, 6000);
            assertEquals(kirillCreditAccount.getBalance(), -1000);

            _bankService.scrollTime(24 * 30);

            assertEquals(kirillCreditAccount.getBalance(), -1450);
            assertEquals((int) kirillDebitAccount.getBalance(), 20082);
            assertEquals((int) kirillDepositAccount.getBalance(), 90517);
    }

    @Test
    public void ConfigurationChangingCheck() {
            HashMap<Double, Double> newDepositInterest = new HashMap<Double, Double>() {{
                    put(25000.0, 4.5);
                    put(100000.0, 6.0);
            }};

            _bankService.withdrawMoney(tinkoff, kirillCreditAccount, 6000);
            _bankService.changeDebitAccountConfiguration(tinkoff, 6);
            _bankService.changeCreditAccountConfiguration(tinkoff, 5, -20000);
            _bankService.changeDepositAccountConfiguration(tinkoff, newDepositInterest);

            _bankService.scrollTime(24 * 30);

            assertEquals(kirillCreditAccount.getBalance(), -1150);
            assertEquals((int) kirillDebitAccount.getBalance(), 20098);
            assertEquals((int) kirillDepositAccount.getBalance(), 90443);
    }
}


