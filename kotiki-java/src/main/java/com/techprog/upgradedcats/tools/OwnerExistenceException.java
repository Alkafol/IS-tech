package com.techprog.upgradedcats.tools;

public class OwnerExistenceException extends Exception {
    public OwnerExistenceException() {
        System.out.println("Owner doesn't exist");
    }
}