package com.community.api.service;

import com.community.api.common.exception.AuthenticationErrorCode;
import com.community.api.common.exception.BoardErrorCode;

import com.community.api.model.*;
import com.community.api.model.base.UserRole;
import com.community.api.model.dto.*;
import com.community.api.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

        @PersistenceContext
        EntityManager em;
        private final PostCustomRepository postCustomRepository;
        private final PostRepository postRepository;
        private final UserRepository userRepository;
        private final LikePostRepository likePostRepository;
        private final ReportInformationRepository reportInformationRepository;

        public Page<ReadPostListDto> getList(int typ, String keyword, Pageable pageable) {
                return postCustomRepository.getList(typ, keyword, pageable);
        }

        public Page<ReadReportListDto> getReportList(int typ, String keyword, Integer reportTyp, Pageable pageable) {
                return postCustomRepository.getReportList(typ, keyword, reportTyp, pageable);
        }

        public Page<ReadBestPostListDto> getBestList(String period, Pageable pageable) {
                return postCustomRepository.getBestList(period, pageable);
        }

        public Page<ReadBestPostListDto> getNewList(List<Integer> typeList, Pageable pageable) {
                return postCustomRepository.getBetweenList(typeList, pageable);
        }

        public Page<ReadPartnerPostListDto> getPartnerList(String keyword, int postType, Pageable pageable) {
                return postCustomRepository.getPartnerList(keyword, postType, pageable);
        }

        @Transactional
        public ReadPostContentDto getContent(String username, Long id) {
                Post post = postRepository.findById(id).orElseThrow(BoardErrorCode.POST_NOT_EXIST::defaultException);
                post.setHit(post.getHit()+1);
                return mapToDTO(username, post);
        }


        private ReadPostContentDto mapToDTO(String username, Post post) {
                ReadPostContentDto dto = new ReadPostContentDto();
                dto.setId(post.getId());
                dto.setUsername(post.getUsername());
                dto.setNickname(post.getNickname());
                dto.setUserIp(post.getUserIp());
                dto.setTitle(post.getTitle());
                dto.setContent(post.getContent());
                dto.setHit(post.getHit());
                dto.setHate(post.getHate());
                dto.setLikes(post.getLikes());
                dto.setReplyNum(post.getReplyNum());
                dto.setCreatedDt(post.getCreatedDt());
                dto.setNotification(post.isNotification());
                if (username == null) {
                        dto.setLiked(false);
                } else {
                        Optional<LikePost> likePost = likePostRepository.findByUsernameAndPostIdEquals(username, post.getId());
                        dto.setLiked(likePost.isPresent());
                }
                return dto;
        }


        public List<ReadPostListDto> getNoticeList(int typ) {
                return postCustomRepository.getNoticeList(typ);
        }

        public Post savePost(String userIp, String username, SavePostDto savePostDto) {
                User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);
                Post post = Post.builder()
                        .postType(savePostDto.postType())
                        .notification(savePostDto.notification())
                        .username(username)
                        .nickname(user.getNickname())
                        .userIp(userIp)
                        .thumbNail(savePostDto.thumbNail())
                        .title(savePostDto.title())
                        .content(savePostDto.content())
                        .hit(1)
                        .hate(0)
                        .likes(0)
                        .isDeleted(false)
                        .replyNum(0)
                        .build();
                Post savePost = postRepository.save(post);
                return savePost;
        }

        @Transactional
        public ReadReportContentDto getReportContent(String username, Long boardId) {
                Post post = postRepository.findById(boardId).orElseThrow(BoardErrorCode.POST_NOT_EXIST::defaultException);
                post.setHit(post.getHit()+1);

                return postCustomRepository.getReportContent(boardId);
        }

        public void saveReport(String userIp, String username, SaveReportPostDto saveReportPostDto) {
                User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);
                Post post = Post.builder()
                        .postType(saveReportPostDto.postType())
                        .notification(false)
                        .username(username)
                        .nickname(user.getNickname())
                        .userIp(userIp)
                        .thumbNail(saveReportPostDto.thumbNail())
                        .title(saveReportPostDto.title())
                        .content(saveReportPostDto.content())
                        .hit(1)
                        .hate(0)
                        .likes(0)
                        .isDeleted(false)
                        .replyNum(0)
                        .build();
                Post savePost = postRepository.save(post);

                ReportInformation reportInformation = ReportInformation.builder()
                        .postId(savePost.getId())
                        .reportTyp(saveReportPostDto.reportTyp())
                        .siteName(saveReportPostDto.siteName())
                        .siteUrl(saveReportPostDto.siteUrl())
                        .date(saveReportPostDto.date())
                        .amount(saveReportPostDto.amount())
                        .accountNumber(saveReportPostDto.accountNumber())
                        .build();
                reportInformationRepository.save(reportInformation);

        }

        @Transactional
        public void updateReport(String username, UpdateReportDto updateReportDto) {
                Post post = postRepository.findById(updateReportDto.postId()).orElseThrow(
                        BoardErrorCode.POST_NOT_EXIST::defaultException);

                ReportInformation reportInformation = reportInformationRepository.findByPostId(updateReportDto.postId()).orElseThrow();

                User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);

                if (!post.getUsername().equals(username) && user.getRole().equals(UserRole.ROLE_USER)) {
                        throw BoardErrorCode.POST_WRITER_NOT_EQUALS.defaultException();
                }

                post.setThumbNail(updateReportDto.thumbNail());
                post.setTitle(updateReportDto.title());
                post.setContent(updateReportDto.content());

                reportInformation.setReportTyp(updateReportDto.reportTyp());
                reportInformation.setSiteName(updateReportDto.siteName());
                reportInformation.setSiteUrl(updateReportDto.siteUrl());
                reportInformation.setDate(updateReportDto.date());
                reportInformation.setAmount(updateReportDto.amount());
                reportInformation.setAccountNumber(updateReportDto.accountNumber());
                em.flush();
                em.clear();
        }

        public void deleteReport(String username, Long postId) {
                Post post = postRepository.findById(postId).orElseThrow(BoardErrorCode.POST_NOT_EXIST::defaultException);
                User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);

                if (!post.getUsername().equals(username) && user.getRole().equals(UserRole.ROLE_USER)) {
                        throw BoardErrorCode.POST_WRITER_NOT_EQUALS.defaultException();
                }
                postRepository.delete(post);
                reportInformationRepository.deleteByPostId(postId);
        }

        @Transactional
        public void updatePost(String username, UpdatePostDto updatePostDto) {
                Post post = postRepository.findById(updatePostDto.postId()).orElseThrow(
                        BoardErrorCode.POST_NOT_EXIST::defaultException);

                User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);

                if (!post.getUsername().equals(username) && user.getRole().equals(UserRole.ROLE_USER)) {
                        throw BoardErrorCode.POST_WRITER_NOT_EQUALS.defaultException();
                }

                post.setNotification(updatePostDto.notification());
                post.setThumbNail(updatePostDto.thumbNail());
                post.setTitle(updatePostDto.title());
                post.setContent(updatePostDto.content());
                em.flush();
                em.clear();
        }

        public void deletePost(String username, Long postId) {
                Post post = postRepository.findById(postId).orElseThrow(BoardErrorCode.POST_NOT_EXIST::defaultException);
                User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);

                if (!post.getUsername().equals(username) && user.getRole().equals(UserRole.ROLE_USER)) {
                        throw BoardErrorCode.POST_WRITER_NOT_EQUALS.defaultException();
                }
                postRepository.delete(post);
        }

        @Transactional
        public void transferPost(int postType, String username, Long postId) {
                Post post = postRepository.findById(postId)
                        .orElseThrow(BoardErrorCode.POST_NOT_EXIST::defaultException);
                User user = userRepository.findByUsername(username)
                        .orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);

                if (!post.getUsername().equals(username) && user.getRole().equals(UserRole.ROLE_USER)) {
                        throw BoardErrorCode.POST_WRITER_NOT_EQUALS.defaultException();
                }

                post.setPostType(postType);
                postRepository.save(post);
        }



}
