package com.community.api.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeletePostListDto {
    public List<Long> idList;
}
