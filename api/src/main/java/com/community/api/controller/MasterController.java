package com.community.api.controller;

import com.community.api.common.jwt.JwtTokenProvider;
import com.community.api.common.properties.JwtProperties;
import com.community.api.common.random.StringSecureRandom;
import com.community.api.common.response.Response;
import com.community.api.common.response.ResultCode;
import com.community.api.common.security.PrincipalDetails;
import com.community.api.model.ApprovedIp;
import com.community.api.model.BlockedIp;
import com.community.api.model.User;
import com.community.api.model.dto.*;
import com.community.api.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/master")
@RequiredArgsConstructor
public class MasterController {

    private final AdminActionHistoryService adminActionHistoryService;
    private final UserService userService;


    @GetMapping(value = "/test")
    public Response<Object> test() {
        return new Response(ResultCode.DATA_NORMAL_PROCESSING);
    }



    @GetMapping(value = "/adminActionHistory")
    public Response<Object> findActionHistory(
            String keyword,
            Pageable pageable) {
        Page<AdminActionHistoryDto> adminHistory = adminActionHistoryService.findAll(keyword, pageable);
        return new Response(ResultCode.DATA_NORMAL_PROCESSING, adminHistory);
    }

    @GetMapping(value = "/adiminUser")
    public Response<Object> findAllAdminUser(
            String keyword,
            Pageable pageable) {
        Page<UserReadDto> all = userService.findAllAdmin(keyword, pageable);
        return new Response(ResultCode.DATA_NORMAL_PROCESSING, all);
    }

    @PostMapping(value = "/saveAdmin")
    public Response<Object> saveAdmin(
            @RequestBody @Valid JoinRequestDto joinRequestDto
    ){

        userService.saveAdmin(joinRequestDto);
        return new Response(ResultCode.DATA_NORMAL_PROCESSING);
    }



}
