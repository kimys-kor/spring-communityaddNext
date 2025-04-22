package com.community.api.common.security;

import com.community.api.common.exception.AuthenticationErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");

        if(exception == null) {
            setResponse(response, AuthenticationErrorCode.AUTHENTICATION_FAILED);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(AuthenticationErrorCode.INVALID_TOKEN.name())) {
            setResponse(response, AuthenticationErrorCode.INVALID_TOKEN);
        }
        //토큰 만료된 경우
        else if(exception.equals(AuthenticationErrorCode.EXPIRED_TOKEN.name())) {
            setResponse(response, AuthenticationErrorCode.EXPIRED_TOKEN);
        }
        //지원되지 않는 토큰인 경우
        else if(exception.equals(AuthenticationErrorCode.UNSUPPORTED_TOKEN.name())) {
            setResponse(response, AuthenticationErrorCode.UNSUPPORTED_TOKEN);
        }
        else {
            setResponse(response, AuthenticationErrorCode.ACCESS_DENIED);
        }
    }
    //한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, AuthenticationErrorCode authenticationErrorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", authenticationErrorCode.getMessage());
        responseJson.put("status", authenticationErrorCode.defaultHttpStatus());

        response.getWriter().print(responseJson);
    }
}