package com.techprog.entities.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ApplicationUserRole {
    ADMIN(Stream.of(ApplicationUserPermission.ADD_CAT, ApplicationUserPermission.ADD_USER, ApplicationUserPermission.GET_CAT_BY_ID, ApplicationUserPermission.GET_USER_BY_USERNAME, ApplicationUserPermission.GET_ALL_CATS, ApplicationUserPermission.GET_ALL_USERS, ApplicationUserPermission.GET_CAT_BY_COLOR, ApplicationUserPermission.DELETE_CAT, ApplicationUserPermission.DELETE_USER, ApplicationUserPermission.START_FRIENDSHIP, ApplicationUserPermission.STOP_FRIENDSHIP, ApplicationUserPermission.GET_ALL_FRIENDS).collect(Collectors.toSet())),
    OWNER(Stream.of(ApplicationUserPermission.ADD_CAT, ApplicationUserPermission.GET_CAT_BY_ID, ApplicationUserPermission.GET_ALL_CATS, ApplicationUserPermission.GET_CAT_BY_COLOR, ApplicationUserPermission.DELETE_CAT, ApplicationUserPermission.START_FRIENDSHIP, ApplicationUserPermission.STOP_FRIENDSHIP, ApplicationUserPermission.GET_ALL_FRIENDS).collect(Collectors.toSet()));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE" + '_' + this.name()));
        return permissions;
    }
}
