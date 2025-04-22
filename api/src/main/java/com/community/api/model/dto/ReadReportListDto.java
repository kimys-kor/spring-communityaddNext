package com.community.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReadReportListDto {
    private Long id;
    private int postType;
    private String username;
    private String nickname;
    private String userIp;
    private String title;
    private String thumbNail;
    private int hit;
    private int hate;
    private int likes;
    private int replyNum;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdDt;
    private int reportTyp;
    private String siteName;
    private String siteUrl;
    private String date;
    private int amount;
    private String accountNumber;

}
