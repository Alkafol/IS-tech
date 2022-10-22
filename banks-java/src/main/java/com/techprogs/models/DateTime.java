package com.techprogs.models;

public class DateTime {
    private int currentTimeInHours = 0;

    public void scrollTimeInHours(int valueInHours){
        currentTimeInHours += valueInHours;
    }

    public void scrollTimeInDays(int valueInDays){
        currentTimeInHours += valueInDays * 60 * 24;
    }

    public int getCurrentTime(){
        return currentTimeInHours;
    }
}
