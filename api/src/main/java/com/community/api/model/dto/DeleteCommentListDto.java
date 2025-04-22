package com.community.api.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteCommentListDto {
    public List<Long> idList;
}
