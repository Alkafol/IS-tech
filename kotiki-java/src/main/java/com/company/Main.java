package com.company;

import Controller.CatsController;
import Dao.CatDao;
import Dao.OwnerDao;
import Models.Cat;
import Models.Owner;
import Service.CatsService;

import Service.OwnersService;

import java.util.Calendar;
import java.util.Set;


public class Main {
        public static void main(String[] args) {
                CatsController catsController = new CatsController(new CatsService(new CatDao()), new OwnersService(new OwnerDao()));
                Calendar cal = Calendar.getInstance();

                cal.set(Calendar.YEAR, 1999);
                cal.set(Calendar.MONTH, Calendar.JULY);
                cal.set(Calendar.DAY_OF_MONTH, 26);

                Owner anton = catsController.addOwner("Kirill", cal);

                Set<Cat> friends = catsController.getAllCatFriends("de168728-63e1-41e4-9a23-1cecbebb1e44");
        }
}
