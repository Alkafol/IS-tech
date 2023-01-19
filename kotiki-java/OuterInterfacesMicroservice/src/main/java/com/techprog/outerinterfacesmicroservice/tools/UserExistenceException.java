package com.techprog.outerinterfacesmicroservice.tools;

public class UserExistenceException extends Exception{
    public UserExistenceException(String message){
        super(message);
    }
}
