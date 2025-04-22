package com.community.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReadSearchCommentDto {
    private Long id;
    private String content;
    private String username;
    private String nickname;
    private boolean isDeleted;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdDt;
}
