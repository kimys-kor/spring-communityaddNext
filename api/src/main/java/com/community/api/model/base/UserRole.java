package com.community.api.model.base;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_MASTER("master"),
    ROLE_ADMIN("admin"),
    ROLE_USER("user")
    ;

    private final String code;

    UserRole(String code) {
        this.code = code;
    }

    public static UserRole of(String code) {
        if (code == null) {
            throw new IllegalStateException();
        }

        for (UserRole ur : UserRole.values()) {
            if (ur.code.equals(code)) {
                return ur;
            }
        }
        throw new IllegalStateException("일치하는 유저 역할이 없습니다.");
    }

}
