package com.community.api.controller;

import com.community.api.common.exception.BoardErrorCode;
import com.community.api.common.jwt.JwtTokenProvider;
import com.community.api.model.Comment;
import com.community.api.model.Dm;
import com.community.api.model.Post;
import com.community.api.model.User;
import com.community.api.model.base.UserRole;
import com.community.api.model.dto.*;
import com.community.api.service.*;
import com.community.api.common.properties.JwtProperties;
import com.community.api.common.response.Response;
import com.community.api.common.response.ResultCode;
import com.community.api.common.security.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Value("${key.savePostPoint}")
    private String savePostPoint;
    @Value("${key.saveCommentPoint}")
    private String saveCommentPoint;
    @Value("${key.saveCommentPoint}")
    private String savePromotionPoint;


    private final RefreshTokenService refreshTokenService;
    private final PointHistoryService pointHistoryService;
    private final JwtProperties jwtProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PostService postService;
    private final LikePostService likePostService;
    private final CommentService commentService;
    private final DmService dmService;
    private final ImgFileService imgFileService;
    private final AdminActionHistoryService adminActionHistoryService;


    @GetMapping(value = "/test")
    public Response<Object> test() {
        return new Response(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 토큰 리프레쉬
    @GetMapping(value = "/refresh")
    public Response<Object> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        System.out.println("토큰리프레쉬쉬쉬");
        String accessToken = refreshTokenService.refresh(request);
        response.addHeader(jwtProperties.headerString(), "Bearer " + accessToken);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 로그인한 유저 정보 가져오기
    @GetMapping(value = "/myinfo")
    public Response<Object> myinfo(
            @RequestHeader(value = "Authorization") String authorizationHeader
    ) {
        System.out.println("유저정보리프레쉬쉬쉬");
        UserResponseDto userResponseDto = null;
        if (authorizationHeader != null) {
            String username = jwtTokenProvider.safeResolveToken(authorizationHeader);
            User user = userService.findByUsernameSafe(username);
            if (user != null) {
                userResponseDto = new UserResponseDto(user);
            }
        }
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, userResponseDto);
    }

    // 로그아웃
    @GetMapping("/logout")
    public Response<Object> logout( HttpServletRequest request,
                          HttpServletResponse response) {
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 내정보 조회
    @GetMapping(value = "/my-info")
    public Response<Object> viewMyinfo(
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();

        User byUsername = userService.findByUsername(username);

        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, byUsername);
    }


    // 내 정보 수정
    @PutMapping(value = "/update/myinfo")
    public Response<Object> updateMyInfo(
            @RequestBody UserUpdateDto userUpdateDto,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();

        userService.updateMyInfo(username, userUpdateDto);

        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 내 비번 수정
    @PutMapping(value = "/update/mypw")
    public Response<Object> updateMyPW(
            @RequestBody UserPWUpdateDto userPWUpdateDto,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();

        userService.updateMyPW(username, userPWUpdateDto);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 회원 탈퇴
    @DeleteMapping(value = "/update/withdrawl")
    public Response<Object> updateWithdrawl(
            @RequestBody UserDeleteDto dto,
            Authentication authentication
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();

        String password = dto.getPassword();
        userService.updateWithdrawl(username, password);

        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }


    // 게시글 쓰기
    @PostMapping(value = "/save/post")
    public Response<Object> savePost(
            @RequestBody @Valid SavePostDto savePostDto,
            HttpServletRequest request,
            Authentication authentication
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();
        User user = userService.findByUsername(username);

        // 포인트 체크
        boolean isSpecialPostType = savePostDto.postType() == 16 || savePostDto.postType() == 17 || savePostDto.postType() == 18;
        boolean hasEnoughPoints = user.getPoint() >= Integer.parseInt(savePostPoint);
        if (isSpecialPostType && !hasEnoughPoints) {
            throw BoardErrorCode.POINT_NOT_ENOUGH.defaultException();
        }

        Post post = postService.savePost(request.getRemoteAddr(), username, savePostDto);
        
        // 포인트 추가
        if (savePostDto.postType() == 16 || savePostDto.postType() == 17 || savePostDto.postType() == 18) {
            userService.addPoint(user.getId(), -30);
            pointHistoryService.save(user.getUsername(), user.getNickname(), "savePromotion", post.getId());
        } else {
            userService.addPointExp(user.getId(), "savePost");
            pointHistoryService.save(user.getUsername(), user.getNickname(), "savePost", post.getId());
        }


        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(8, user.getUsername());
        }

        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 먹튀신고 쓰기
    @PostMapping(value = "/save/report-post")
    public Response<Object> saveReportPost(
            @RequestBody @Valid SaveReportPostDto saveReportPostDto,
            HttpServletRequest request,
            Authentication authentication
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();
        User user = userService.findByUsername(username);
        postService.saveReport(request.getRemoteAddr(), username, saveReportPostDto);

        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(16, user.getUsername());
        }

        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 먹튀신고 수정
    @PatchMapping(value = "/update/report-post")
    public Response<Object> updateReport(
            @RequestBody @Valid UpdateReportDto updateReportDto,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();
        User user = userService.findByUsername(username);

        postService.updateReport(username, updateReportDto);
        // 어드민일경우 로그 저장
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(17, user.getUsername());
        }

        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 먹튀신고 삭제
    @DeleteMapping(value = "/report/{postId}")
    public Response<Object> deleteReport(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();
        User user = userService.findByUsername(username);

        postService.deleteReport(username, postId);
        // 어드민일경우 로그 저장
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(18, user.getUsername());
        }
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 게시글 수정
    @PatchMapping(value = "/update/post")
    public Response<Object> updatePost(
            @RequestBody @Valid UpdatePostDto updatePostDto,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();
        User user = userService.findByUsername(username);

        postService.updatePost(username, updatePostDto);
        // 어드민일경우 로그 저장
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(9, user.getUsername());
        }

        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 게시글 좋아요
    @PatchMapping(value = "/like/post")
    public Response<Object> updateLikePost(
            @RequestParam Long boardId,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();

        likePostService.likePost(username, boardId);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 게시글 삭제
    @DeleteMapping(value = "/post/{postId}")
    public Response<Object> deletePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();
        User user = userService.findByUsername(username);

        postService.deletePost(username, postId);
        // 어드민일경우 로그 저장
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(6, user.getUsername());
        }
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 댓글 작성
    @PostMapping(value = "/write/comment")
    public Response<Object> writeComment(
            @RequestBody @Valid SaveCommentDto saveCommentDto,
            HttpServletRequest request,
            Authentication authentication
            ) {

        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();
        User user = userService.findByUsername(username);

        Comment comment = commentService.saveComment(request.getRemoteAddr(), username, saveCommentDto);
        // 유저 포인트, 경험치 증가
        userService.addPointExp(user.getId(), "saveComment");
        // 포인트 히스토리 저장
        pointHistoryService.save(user.getUsername(), user.getNickname(), "saveComment", comment.getPost().getId());
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(10, user.getUsername());
        }
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }



    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    private Response<Object> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication
    ){
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();
        User user = userService.findByUsername(username);

        commentService.deleteComment(username, commentId);
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(12, user.getUsername());
        }
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 댓글 수정
    @PatchMapping("/comment")
    private Response<Object> updateComment(
            @RequestBody SaveCommentDto saveCommentDto,
            Authentication authentication
    ){
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();
        User user = userService.findByUsername(username);

        commentService.updateComment(username, saveCommentDto);
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            adminActionHistoryService.save(11, user.getUsername());
        }
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 메세지 보내기
    @PostMapping("/send/dm")
    private Response<Object> sendDm(
            @RequestBody DmDto dmDto,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();

        Dm dm = dmService.sendDm(username, dmDto);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, dm);
    }

    // 메세지 리스트
    @GetMapping("/get/dmlist")
    private Response<Object> getDmList(
            Pageable pageable,
            Authentication authentication
    ) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetailis.getUsername();

        Page<ReadDmListDto> messageList = dmService.getDmList(username, pageable);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, messageList);
    }

    // 메세지 읽기
    @GetMapping("/get/{dmId}")
    private Response<Object> getDmContent(
            @PathVariable Long dmId
    ) {
        Dm dmContent = dmService.getDmContent(dmId);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, dmContent);
    }

    // 메세지 삭제
    @PutMapping("/delete/dm")
    private Response<Object> deleteDm(
            @RequestBody List<Long> dmIdList
    ) {
        dmService.deleteMessage(dmIdList);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 사진 업로드
    @PostMapping("/upload")
    public Response<Object> uploadImages(@RequestParam("files") MultipartFile[] files) {
        List<String> imgPaths = new ArrayList<>();
        for (MultipartFile file : files) {
            String imgPath = imgFileService.saveFile(file);
            imgPaths.add(imgPath);
        }
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, imgPaths);
    }



}
