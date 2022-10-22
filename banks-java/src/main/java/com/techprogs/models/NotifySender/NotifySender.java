package com.techprogs.models.NotifySender;

public class NotifySender implements INotifySender{
    private INotifySender _notifySender;

    public void sendMessage(String message){
        System.out.println(message);
    }
}
