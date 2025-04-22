package com.community.api.model.dto;

import lombok.Data;

@Data
public class UserUpdateDto {

    private String fullName;
    private String nickname;
    private String phoneNum;

}
