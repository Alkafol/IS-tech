package com.company;

import Classes.ConsoleInterface;
import Tests.Tests;

public class Main {

    public static void main(String[] args) {
        ConsoleInterface newConsoleInterface = new ConsoleInterface();
        newConsoleInterface.readCommand();
        Tests newTests = new Tests();
        newTests.startsTesting();
    }
}
