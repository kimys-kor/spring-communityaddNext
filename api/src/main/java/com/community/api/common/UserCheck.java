package com.community.api.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

public class UserCheck {

    private boolean userChk;

    private User user;


    public UserCheck(Authentication authentication) {
        this.userChk = true;

        try {
            this.user = (User) authentication.getPrincipal();
        } catch (Exception e) {
            this.userChk = false;
        }
    }

    public boolean isUserChk() {
        return userChk;
    }

    public User getUser() {
        return user;
    }
}
