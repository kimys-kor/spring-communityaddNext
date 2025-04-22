package com.community.api.model.dto;

import com.community.api.model.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadCommentDto {

    private Long id;
    private String content;
    private Long postId;
    private String username;
    private String nickname;
    private List<ReadCommentDto> children = new ArrayList<>();
    private boolean isDeleted;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdDt;


    public ReadCommentDto(Long id, String content, Long postId, String username, String nickname, boolean isDeleted, LocalDateTime createdDt) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.username = username;
        this.nickname = nickname;
        this.isDeleted = isDeleted;
        this.createdDt = createdDt;

    }

    public static ReadCommentDto convertCommentToDto(Comment comment) {
        return comment.isDeleted() == true ?
                new ReadCommentDto(comment.getId(), "삭제된 댓글입니다.", null, null,null,true,null) :
                new ReadCommentDto(comment.getId(), comment.getContent(), comment.getPost().getId(), comment.getUsername(),
                        comment.getNickname(), comment.isDeleted(), comment.getCreatedDt());
    }
}
