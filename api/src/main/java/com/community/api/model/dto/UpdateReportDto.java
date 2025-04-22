package com.community.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateReportDto(
        @NotNull(message = "postId must not be blank")
        Long postId,

        @NotBlank(message = "title must not be blank")
        String title,
        @NotBlank(message = "content must not be blank")
        String content,
        String thumbNail,
        int reportTyp,
        String siteName,
        String siteUrl,
        String date,
        int amount,
        String accountNumber
) {

}