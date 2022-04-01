package Classes;

import Classes.Models.Accounts.IAccount;
import Classes.Models.Bank;
import Classes.Models.Customer.Customer;
import Classes.Services.BankServices;
import Classes.Services.IBankServices;

import java.util.HashMap;
import java.util.Scanner;

public class ConsoleInterface {
    private IBankServices _bankService;

    public ConsoleInterface(){
        _bankService = new BankServices();
    }

    public void readCommand(){
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (command.equals("Create bank")){
            System.out.println("Enter bank name");
            String bankName = scanner.nextLine();
            System.out.println("Enter transaction limit");
            double transactionLimit = Double.valueOf(scanner.nextLine());
            System.out.println("Enter credit limit");
            double creditLimit = Double.valueOf(scanner.nextLine());
            System.out.println("Enter credit account commission");
            double creditAccountCommission = Double.valueOf(scanner.nextLine());
            System.out.println("Enter deposit account interest");
            String[] splitInterest = scanner.nextLine().split("\\s+");
            HashMap<Double, Double> depositInterest = new HashMap<Double, Double>();
            for (int i = 0; i < splitInterest.length; i++){
                if (i % 2 == 0){
                    depositInterest.put(Double.valueOf(splitInterest[i]), Double.valueOf(splitInterest[i + 1]));
                }
            }
            System.out.println("Enter debit account interest");
            double debitAccountInterest = Double.valueOf(scanner.nextLine());
            _bankService.createBank(bankName, transactionLimit, creditLimit, creditAccountCommission, depositInterest, debitAccountInterest);
        }
        else if (command.equals("Create customer")){
            System.out.println("Enter bank name");
            String bankName = scanner.nextLine();
            Bank bank = _bankService.getBankByName(bankName);
            System.out.println("Enter customer name");
            String name = scanner.nextLine();
            System.out.println("Enter customer surname");
            String surname = scanner.nextLine();
            _bankService.createCustomer(bank, name, surname);
        }
        else if (command.equals("Scroll time")){
            System.out.println("Enter time in hours");
            int timeToScroll = Integer.valueOf(scanner.nextLine());
            _bankService.scrollTime(timeToScroll);
        }
        else if (command.equals("Add money") || command.equals("Withdraw money")){
            System.out.println("Enter bank name");
            String bankName = scanner.nextLine();
            Bank bank = _bankService.getBankByName(bankName);
            System.out.println("Enter account id");
            String accountId = scanner.nextLine();
            IAccount account = _bankService.getAccountById(accountId);
            System.out.println("Enter amount of money");
            double amount = Double.valueOf(scanner.nextLine());
            if (command.equals("Add money")) {
                _bankService.addMoney(bank, account, amount);
            }
            else{
                _bankService.withdrawMoney(bank, account, amount);
            }
        }
        else if (command.equals("Transfer money")){
            System.out.println("Enter sending bank name");
            String bankFromName = scanner.nextLine();
            Bank bankFrom = _bankService.getBankByName(bankFromName);
            System.out.println("Enter receiving bank name");
            String bankToName = scanner.nextLine();
            Bank bankTo = _bankService.getBankByName(bankToName);
            System.out.println("Enter sending account id");
            String accountFromId = scanner.nextLine();
            IAccount accountFrom = _bankService.getAccountById(accountFromId);
            System.out.println("Enter receiving account id");
            String accountToId = scanner.nextLine();
            IAccount accountTo = _bankService.getAccountById(accountToId);
            System.out.println("Enter amount of money");
            Double amount = Double.valueOf(scanner.nextLine());
            _bankService.transferMoney(bankFrom, accountFrom, bankTo, accountTo, amount);
        }
        else if (command.equals("Change credit account configuration")){
            System.out.println("Enter bank name");
            String bankName = scanner.nextLine();
            Bank bank = _bankService.getBankByName(bankName);
            System.out.println("Enter new commission");
            double newCommission = Double.valueOf(scanner.nextLine());
            System.out.println("Enter new credit limit");
            double newCreditLimit = Double.valueOf(scanner.nextLine());
            _bankService.changeCreditAccountConfiguration(bank, newCommission, newCreditLimit);
        }
        else if (command.equals("Change debit account configuration")){
            System.out.println("Enter bank name");
            String bankName = scanner.nextLine();
            Bank bank = _bankService.getBankByName(bankName);
            System.out.println("Enter new interest");
            double newInterest = Double.valueOf(scanner.nextLine());
            _bankService.changeDebitAccountConfiguration(bank, newInterest);
        }
        else if (command.equals("Change deposit account configuration")){
            System.out.println("Enter bank name");
            String bankName = scanner.nextLine();
            Bank bank = _bankService.getBankByName(bankName);
            System.out.println("Enter new deposit account interest");
            String[] splitInterest = scanner.nextLine().split("\\s+");
            HashMap<Double, Double> depositInterest = new HashMap<Double, Double>();
            for (int i = 0; i < splitInterest.length; i++){
                if (i % 2 == 0){
                    depositInterest.put(Double.valueOf(splitInterest[i]), Double.valueOf(splitInterest[i + 1]));
                }
            }
            _bankService.changeDepositAccountConfiguration(bank, depositInterest);
        }
        else if (command.equals("Create debit account") || command.equals("Create deposit account") || command.equals("Create credit account")){
            System.out.println("Enter bank name");
            String bankName = scanner.nextLine();
            Bank bank = _bankService.getBankByName(bankName);
            System.out.println("Enter customer name");
            String name = scanner.nextLine();
            System.out.println("Enter customer surname");
            String surname = scanner.nextLine();
            Customer customer = _bankService.getCustomer(bank, name, surname);
            System.out.println("Enter amount of days before expiry");
            String date = scanner.nextLine();
            int expiryDate = Integer.valueOf(date);
            if (command.equals("Create debit account")){
            _bankService.createDebitAccount(bank, customer, expiryDate);
            }
            else if (command.equals("Create deposit account")){
                System.out.println("Enter amount");
                Double amount = Double.valueOf(scanner.nextLine());
                _bankService.createDepositAccount(bank, customer, expiryDate, amount);
            }
            else{
                _bankService.createCreditAccount(bank, customer, expiryDate);
            }

        }
        this.readCommand();
    }

}
