package Classes.Models;

public class DateTime {
    private int _currentTimeInHours = 0;

    public void scrollTimeInHours(int valueInHours){
        _currentTimeInHours += valueInHours;
    }
    public void scrollTimeInDays(int valueInDays){
        _currentTimeInHours += valueInDays * 60 * 24;
    }

    public int getCurrentTime(){
        return _currentTimeInHours;
    }
}
