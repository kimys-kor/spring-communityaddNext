package com.community.api.service;

import com.community.api.common.exception.BoardErrorCode;
import com.community.api.model.LikePost;
import com.community.api.model.Post;
import com.community.api.repository.LikePostRepository;
import com.community.api.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikePostService {

    @PersistenceContext
    EntityManager em;
    private final LikePostRepository likePostRepository;
    private final PostRepository postRepository;

    @Transactional
    public void likePost(String username, Long boardId) {
        Post post = postRepository.findById(boardId).orElseThrow(BoardErrorCode.POST_NOT_EXIST::defaultException);
        Optional<LikePost> OptionalLikePost = likePostRepository.findByUsernameAndPostIdEquals(username, boardId);
        if (OptionalLikePost.isEmpty()) {
            LikePost likePost = LikePost.builder()
                    .postId(boardId)
                    .username(username)
                    .build();
            likePostRepository.save(likePost);
            post.setLikes(post.getLikes()+1);
        } else {
            likePostRepository.deleteByUsernameAndPostIdEquals(username, boardId);
            post.setLikes(post.getLikes()-1);
        }

        em.flush();
        em.clear();

    }
}
