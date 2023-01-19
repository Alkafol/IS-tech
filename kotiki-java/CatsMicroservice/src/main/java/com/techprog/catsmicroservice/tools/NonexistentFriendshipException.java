package com.techprog.catsmicroservice.tools;

public class NonexistentFriendshipException extends Exception{
    public NonexistentFriendshipException(String message){
        super(message);
    }
}
