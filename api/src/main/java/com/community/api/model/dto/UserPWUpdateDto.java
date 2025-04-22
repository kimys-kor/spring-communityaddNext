package com.community.api.model.dto;

import lombok.Data;

@Data
public class UserPWUpdateDto {

    private String oldpassword;
    private String newpassword;


}
