package com.techprog.outerInterface.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.techprog.outerInterface.security.ApplicationUserPermission.*;


public enum ApplicationUserRole {
    ADMIN(Stream.of(ADD_CAT, ADD_OWNER, GET_CAT_BY_ID, GET_OWNER_BY_ID, GET_ALL_CATS, GET_ALL_OWNERS, GET_CAT_BY_COLOR, DELETE_CAT, DELETE_OWNER, START_FRIENDSHIP, STOP_FRIENDSHIP).collect(Collectors.toSet())),
    OWNER(Stream.of(ADD_CAT, GET_CAT_BY_ID, GET_ALL_CATS, GET_CAT_BY_COLOR, DELETE_CAT).collect(Collectors.toSet()));

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
