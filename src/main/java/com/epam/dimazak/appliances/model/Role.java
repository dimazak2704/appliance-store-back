package com.epam.dimazak.appliances.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CLIENT,
    EMPLOYEE,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}