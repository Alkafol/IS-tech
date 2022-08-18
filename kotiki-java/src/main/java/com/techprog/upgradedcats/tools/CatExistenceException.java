package com.techprog.upgradedcats.tools;

public class CatExistenceException extends Exception {
    public CatExistenceException() {
        System.out.println("Cat doesn't exist");
    }
}
