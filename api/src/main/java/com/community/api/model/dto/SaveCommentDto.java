package com.community.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SaveCommentDto(
        Long id,
        Long boardId,
        
        // 댓글은 parentId null, 대댓글이면 달려는 댓글의 아이디
        Long parentId,
        @NotBlank(message = "content must not be blank")
        String content
) {

}
