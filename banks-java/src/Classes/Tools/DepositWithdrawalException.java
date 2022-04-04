package Classes.Tools;

public class DepositWithdrawalException extends Exception {
    public DepositWithdrawalException() {
        System.out.println("Withdrawal before expiry day is unavailable");
    }
}
