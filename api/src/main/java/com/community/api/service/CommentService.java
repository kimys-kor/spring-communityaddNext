package com.community.api.service;


import com.community.api.common.exception.AuthenticationErrorCode;
import com.community.api.common.exception.BoardErrorCode;
import com.community.api.common.exception.CommentErrorCode;
import com.community.api.model.Comment;
import com.community.api.model.Post;
import com.community.api.model.User;
import com.community.api.model.base.UserRole;
import com.community.api.model.dto.ReadCommentDto;
import com.community.api.model.dto.SaveCommentDto;
import com.community.api.repository.CommentCustomRepository;
import com.community.api.repository.CommentRepository;
import com.community.api.repository.PostRepository;
import com.community.api.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    @PersistenceContext
    EntityManager em;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentCustomRepository commentCustomRepository;
    private final UserRepository userRepository;


    @Transactional
    public Comment saveComment(String remoteAddr, String username, SaveCommentDto saveCommentDto) {
        
        
        if (saveCommentDto.parentId() != null) {
            // 대댓글을 작성할 댓글이 없을때 에러처리
            Comment parentComment = commentRepository.findById(saveCommentDto.parentId()).orElseThrow(BoardErrorCode.PARENT_NOT_EXIST::defaultException);
            if(parentComment.getPost().getId() != saveCommentDto.boardId()){
                // 작성하는 대댓글의 postId와 대댓글을 작성하려는 댓글의 postId가 일치하지 않을때 에러처리
                throw BoardErrorCode.BAD_COMMENT_WRITE_REQUEST.defaultException();
            }
            if (parentComment.getParent() != null) {
                // 대댓글 이상 작성시 에러처리
                throw BoardErrorCode.COMMENT_ONLY_CAN_2STEP.defaultException();
            }
        }
        
        Post post = postRepository.findById(saveCommentDto.boardId()).orElseThrow(BoardErrorCode.POST_NOT_EXIST::defaultException);

        User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);

            Comment comment = Comment.builder()
                    .username(username)
                    .nickname(user.getNickname())
                    .userIp(remoteAddr)
                    .content(saveCommentDto.content())
                    .isDeleted(false)
                    .post(post)
                    .parent(
                            saveCommentDto.parentId() != null ?
                                    commentRepository.findById(saveCommentDto.parentId())
                                            .orElseThrow(CommentErrorCode.COMMENT_NOT_EXIST::defaultException) : null
                    )
                    .build();
        post.setReplyNum(post.getReplyNum()+1);
            em.flush();
            em.clear();

        Comment saveComment = commentRepository.save(comment);
        return saveComment;
    }

    public Map<String, Object> findCommentsByPostId(Long boardId, Pageable pageable) {
        Map<String, Object> returnMap = commentCustomRepository.findByboardId(boardId, pageable);
        Long total = (Long) returnMap.get("total");
        List<Comment> comments = (List<Comment>) returnMap.get("comments");
        List<ReadCommentDto> nestedComments = convertNestedStructure(comments);

        Map<String, Object> result = new HashMap<>();
        result.put("comments", nestedComments);
        result.put("total", total);

        return result;
    }

    public Map<String, Object> searchComment(String keyword, Pageable pageable) {
        Map<String, Object> result = commentCustomRepository.searchComment(keyword, pageable);
        return result;
    }

    @Transactional
    public void deleteComment(String username, Long commentId) {
        Comment comment = commentRepository.findCommentByIdWithParent(commentId);
        User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);

        if (!comment.getUsername().equals(username) && user.getRole().equals(UserRole.ROLE_USER)) {
            throw CommentErrorCode.COMMENT_WRITER_NOT_EQUALS.defaultException();
        }
        if(comment.getChildren().size() != 0) {
            comment.setDeleted(true);
        } else {
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
        Post post = postRepository.findById(comment.getPost().getId()).orElseThrow();
        int size = post.getCommentList().size();
        post.setReplyNum(size-1);
        em.flush();
        em.clear();
    }


    private Comment getDeletableAncestorComment(Comment comment) {
        Comment parent = comment.getParent();
        if(parent != null && parent.getChildren().size() == 1 && parent.isDeleted() == true)
            return getDeletableAncestorComment(parent);
        return comment;
    }

    private List<ReadCommentDto> convertNestedStructure(List<Comment> comments) {
        List<ReadCommentDto> result = new ArrayList<>();
        Map<Long, ReadCommentDto> map = new HashMap<>();
        comments.stream().forEach(c -> {
            ReadCommentDto dto = ReadCommentDto.convertCommentToDto(c);
            map.put(dto.getId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
            else result.add(dto);
        });
        return result;
    }

    @Transactional
    public void updateComment(String username, SaveCommentDto saveCommentDto) {
        Comment comment = commentRepository.findById(saveCommentDto.id()).orElseThrow(CommentErrorCode.COMMENT_NOT_EXIST::defaultException);
        User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);

        if (!comment.getUsername().equals(username) && user.getRole().equals(UserRole.ROLE_USER)) {
            throw CommentErrorCode.COMMENT_WRITER_NOT_EQUALS.defaultException();
        }

        comment.setContent(saveCommentDto.content());
        em.flush();
        em.clear();
    }



}
