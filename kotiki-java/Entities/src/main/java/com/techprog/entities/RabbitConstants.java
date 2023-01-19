package com.techprog.entities;

public final class RabbitConstants {
    public static final String ADD_CAT_QUEUE = "cat.add";
    public static final String GET_CAT_QUEUE = "cat.get";
    public static final String GET_ALL_CATS_QUEUE = "cat.get.all";
    public static final String START_FRIENDSHIP_QUEUE = "cat.start.friendship";
    public static final String STOP_FRIENDSHIP_QUEUE = "cat.stop.friendship";
    public static final String DELETE_CAT_QUEUE = "cat.delete";
    public static final String GET_CATS_BY_COLOR_QUEUE = "cat.get.colored";
    public static final String DELETE_OWNERSHIP_QUEUE = "owner.delete.ownership";
    public static final String START_OWNERSHIP_QUEUE = "owner.start.ownership";
    public static final String GET_OWNER_QUEUE = "owner.get";
    public static final String ADD_OWNER_QUEUE = "owner.add";
    public static final String DELETE_OWNER_QUEUE = "owner.delete";

    public static final String APP_EXCHANGE = "app.exchange";

    private RabbitConstants(){
    }
}
