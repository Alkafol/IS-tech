package com.techprog.entities.user;

public enum ApplicationUserPermission {
    ADD_CAT("cat:add"),
    ADD_USER("user:add"),
    GET_CAT_BY_ID("cat:get_by_id"),
    GET_USER_BY_USERNAME("user:get_by_username"),
    GET_ALL_CATS("cat:get_all"),
    GET_ALL_USERS("user:get_all"),
    GET_CAT_BY_COLOR("cat:get_by_color"),
    GET_ALL_FRIENDS("cat:get_all_friends"),
    DELETE_CAT("cat:delete"),
    DELETE_USER("user:delete"),
    START_FRIENDSHIP("cat:start_friendship"),
    STOP_FRIENDSHIP("cat:stop_friendship");

    private final String permission;

    ApplicationUserPermission(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
