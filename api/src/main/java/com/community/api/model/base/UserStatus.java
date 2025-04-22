package com.community.api.model.base;

import lombok.Getter;

@Getter
public enum UserStatus {
    NORMAL("normal"),
    BLOCKED("blocked"),
    DELETED("deleted")
    ;

    private final String code;

    UserStatus(String code) {
        this.code = code;
    }

    public static UserStatus of(String code) {
        if (code == null) {
            throw new IllegalStateException();
        }

        for (UserStatus us : UserStatus.values()) {
            if (us.code.equals(code)) {
                return us;
            }
        }
        throw new IllegalStateException("일치하는 유저 상태가 없습니다.");
    }
}
