package com.community.api.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransferPostListDto {
    private int postType;
    private List<Long> idList;
}
