package com.community.api.model.dto;

import com.community.api.model.base.UserStatus;
import lombok.Data;

@Data
public class UserUpdateAdminDto {
    private String username;
    private String password;
    private String fullName;
    private String nickname;
    private String phoneNum;

}
