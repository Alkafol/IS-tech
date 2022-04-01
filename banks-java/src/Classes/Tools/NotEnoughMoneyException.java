package Classes.Tools;

public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException() {
        System.out.println("Not enough money");
    }
}