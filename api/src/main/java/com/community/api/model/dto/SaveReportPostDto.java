package com.community.api.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record SaveReportPostDto(
        @NotNull(message = "postType must not be blank")
        int postType,
        @NotBlank(message = "title must not be blank")
        String title,
        @NotBlank(message = "content must not be blank")
        String content,
        String thumbNail,
        @NotNull(message = "reportTyp must not be blank")
        int reportTyp,
        @NotBlank(message = "siteName must not be blank")
        String siteName,
        @NotBlank(message = "siteUrl must not be blank")
        String siteUrl,
        @NotBlank(message = "date must not be blank")
        String date,
        @NotNull(message = "amount must not be null")
        @Min(value = 0, message = "amount must be greater than or equal to 0")
        int amount,
        String accountNumber
) {

}
