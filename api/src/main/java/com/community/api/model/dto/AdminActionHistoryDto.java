package com.community.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminActionHistoryDto {

    private Long id;
    private int actionType;
    private String username;
    private String content;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdDt;
}
