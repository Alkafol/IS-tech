package Classes.Tools;

public class ExistedBankException extends Exception {
    public ExistedBankException() {
        System.out.println("Bank with this name already existed");
    }
}
