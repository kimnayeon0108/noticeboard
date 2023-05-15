package com.example.noticeboard.type;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum UserRole {
    ROLE_USER, ROLE_ADMIN;

    public static UserRole getUserRole(String role) {
        if (StringUtils.isBlank(role)) {
            return null;
        }

        if (role.equals(ROLE_USER.name())) {
            return ROLE_USER;
        }

        if (role.equals(ROLE_ADMIN.name())) {
            return ROLE_ADMIN;
        }

        return null;
    }
}
