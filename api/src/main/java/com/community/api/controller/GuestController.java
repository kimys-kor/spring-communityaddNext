package com.community.api.controller;

import com.community.api.common.exception.AuthenticationErrorCode;
import com.community.api.common.jwt.JwtTokenProvider;
import com.community.api.common.properties.JwtProperties;
import com.community.api.common.random.StringSecureRandom;
import com.community.api.common.response.Response;
import com.community.api.common.response.ResultCode;
import com.community.api.common.security.PrincipalDetails;
import com.community.api.model.Banner;
import com.community.api.model.User;
import com.community.api.model.base.UserStatus;
import com.community.api.model.dto.*;
import com.community.api.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestController {

    @Value("${key.signupPoint}")
    private String signupPoint;
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final RefreshTokenService refreshTokenService;
    private final PointHistoryService pointHistoryService;
    private final StringSecureRandom stringSecureRandom;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final BannerService bannerService;

    @GetMapping(value = "/test")
    public Response<Object> test() {
        return new Response(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 로그인
    @PostMapping(value = "/login")
    public Response<Object> login(
            @RequestBody @Valid LoginRequestDto loginRequestDto,
            HttpServletResponse response
    ) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.username(),
                        loginRequestDto.password());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        PrincipalDetails principalDetails = (PrincipalDetails) authenticate.getPrincipal();
        String username = principalDetails.getUser().getUsername();
        User user = userService.findByUsername(username);


        if (!user.getStatus().equals(UserStatus.NORMAL)) {
            throw AuthenticationErrorCode.USER_NOT_EXIST.defaultException();
        }

        String jwtToken = jwtTokenProvider.generateToken(principalDetails.getUser().getId(), username);
        response.addHeader(jwtProperties.headerString(), "Bearer " + jwtToken);

        String refreshToken = stringSecureRandom.next(20);
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setMaxAge(2_592_000); // 60 seconds × 60 minutes × 24 hours × 30 days
        cookie.setDomain("");
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.addHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue()
                + "; Max-Age=" + cookie.getMaxAge()
                + "; Path=" + cookie.getPath()
                + "; Domain=" + cookie.getDomain()
                + "; HttpOnly; Secure; SameSite=None");

        UserResponseDto userResponseDto = new UserResponseDto(user);
        refreshTokenService.save(principalDetails.getUser().getUsername(), refreshToken);
        return new Response(ResultCode.DATA_NORMAL_PROCESSING, userResponseDto);
    }

    // 회원가입
    @PostMapping(value = "/join")
    public Response<Object> join(
            @RequestBody @Valid JoinRequestDto joinRequestDto
    ){

        User user = userService.join(joinRequestDto);
        // 포인트 히스토리 저장
        pointHistoryService.save(user.getUsername(), user.getNickname(), "signup", null);
        return new Response(ResultCode.DATA_NORMAL_PROCESSING);
    }

    // 아이디, 비밀번호 찾기 (문자인증)

    // 댓글 리스트
    @GetMapping(value = "/list/comment")
    public Response<Object> listComment(
            Long boardId,
            Pageable pageable
    ) {
        Map<String, Object> commentsByPostId = commentService.findCommentsByPostId(boardId, pageable);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, commentsByPostId);
    }

    // 게시글 리스트
    @GetMapping(value = "/list")
    public Response<Object> BoardList(
            int typ,
            String keyword,
            Pageable pageable
    ) {
        Page<ReadPostListDto> list = postService.getList(typ, keyword, pageable);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, list);
    }

    // 먹튀신고 리스트
    @GetMapping(value = "/list/report-post")
    public Response<Object> ReportList(
            int typ,
            String keyword,
            Integer reportTyp,
            Pageable pageable
    ) {
        Page<ReadReportListDto> list = postService.getReportList(typ, keyword, reportTyp, pageable);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, list);
    }


    // 먹튀신고 상세
    @GetMapping(value = "/content/report-post")
    public Response<Object> ReportContent(
            Long boardId,
            Authentication authentication
    ) {
        String username = null;
        if (authentication != null) {
            PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
            username = principalDetailis.getUsername();
        }

        ReadReportContentDto post = postService.getReportContent(username, boardId);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, post);
    }

    // 베스트 게시글 리스트
    @GetMapping(value = "/bestList")
    public Response<Object> BestBoardList(
            String period,
            Pageable pageable
    ) {
        Page<ReadBestPostListDto> list = postService.getBestList(period, pageable);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, list);
    }

    // 최근 게시글 리스트
    @GetMapping(value = "/newList")
    public Response<Object> NewBoardList(
            @RequestParam List<Integer> typeList,
            Pageable pageable
    ) {
        Page<ReadBestPostListDto> list = postService.getNewList(typeList, pageable);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, list);
    }

    // 파트너 게시글 리스트
    @GetMapping(value = "/partnerList")
    public Response<Object> PartnerList(
            String keyword,
            Pageable pageable
    ) {
        Page<ReadPartnerPostListDto> list = postService.getPartnerList(keyword,1,pageable);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, list);
    }

    // 포토 게시글 리스트
    @GetMapping(value = "/photoList")
    public Response<Object> PhotoList(
            int postType,
            String keyword,
            Pageable pageable
    ) {
        Page<ReadPartnerPostListDto> list = postService.getPartnerList(keyword, postType, pageable);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, list);
    }

    // 게시글 상세
    @GetMapping(value = "/content")
    public Response<Object> BoardContent(
            Long boardId,
            Authentication authentication
    ) {
        String username = null;
        if (authentication != null) {
            PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
            username = principalDetailis.getUsername();
        }


        ReadPostContentDto post = postService.getContent(username, boardId);
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, post);
    }

    // 배너리스트
    @GetMapping(value = "/bannerlist")
    public Response<Object> bannerList(
    ) {
        List<Banner> allBanners = bannerService.getAllBanners();
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, allBanners);
    }

    // 3배너리스트
    @GetMapping(value = "/threeBannerlist")
    public Response<Object> banner3List(
    ) {
        List<Banner> threeBanners = bannerService.get3Banners();
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING, threeBanners);
    }

    // 배너 클릭
    @GetMapping(value = "/clickBanner")
    public Response<Object> clickBanner(
            @RequestParam(value = "bannerId") Long bannerId
    ) {
        if (bannerId != null) {
            bannerService.clickBanner(bannerId);
        }
        return new Response<>(ResultCode.DATA_NORMAL_PROCESSING);
    }


}
