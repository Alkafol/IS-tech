package com.techprog.upgradedcats.security;

public enum ApplicationUserPermission {
    ADD_CAT("cat:add"),
    ADD_OWNER("owner:add"),
    GET_CAT_BY_ID("cat:get_by_id"),
    GET_OWNER_BY_ID("owner:get_by_id"),
    GET_ALL_CATS("cat:get_all"),
    GET_ALL_OWNERS("owner:get_all"),
    GET_CAT_BY_COLOR("cat:get_by_color"),
    DELETE_CAT("cat:delete"),
    DELETE_OWNER("owner:delete"),
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
