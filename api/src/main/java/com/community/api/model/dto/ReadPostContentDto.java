package com.community.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReadPostContentDto {
    private Long id;
    private String username;
    private String nickname;
    private String userIp;
    private String title;
    private String content;
    private int hit;
    private int hate;
    private int likes;
    private int replyNum;
    private boolean isLiked;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdDt;
    private boolean isNotification;
}
