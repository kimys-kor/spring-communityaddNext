package com.community.api.model.dto;

import com.community.api.model.base.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class UserReadDto {

    private Long id;
    private String username;
    private String phoneNum;
    private String fullName;
    private String nickname;
    private int point;
    private int exp;
    private UserStatus status;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private String createdDt;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private String lastLogin;
}
