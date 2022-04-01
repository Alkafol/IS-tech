package Classes.Tools;

public class TransactionLimitException extends Exception {
    public TransactionLimitException() {
        System.out.println("Transaction limit was exceeded");
    }
}