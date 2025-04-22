package com.community.api.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResultCode {


    // 정상 처리 0XXXXXXX
    // DATA 관련 010001XX
    DATA_SEARCH_SUCCESSFULLY(HttpStatus.OK, "00001100", "정상 데이터 입니다."),
    DATA_NORMAL_PROCESSING(HttpStatus.OK, "00001101", "정상 처리 되었습니다."),

    // USER 관련 010002XX
    USER_JOIN_SUCCESSFULLY(HttpStatus.OK, "00001001", "회원가입이 완료되었습니다."),
    USER_NOT_DUPLICATE_ID(HttpStatus.OK, "00001002", "중복된 아이디가 아닙니다."),
    USER_NOT_DUPLICATE_NICKNAME(HttpStatus.OK, "00001003", "중복된 닉네임이 아닙니다."),


    // 비정상 처리 91XXXXXX
    AUTH_PERMISSION_DENY(HttpStatus.OK, "91009991", "접근 권한이 없습니다."),

    // DATA 관련 910001XX
    DATA_UPDATE_WRONG(HttpStatus.OK, "91000101", "데이터 수정에 실패하였습니다."),
    DATA_NOT_DUPLICATE(HttpStatus.OK, "91000102", "중복된 DATA 입니다."),
    DATA_ALREADY_PROCESSED(HttpStatus.OK, "91000103", "이미 처리된 DATA 입니다."),


    // USER 관련 910002XX
    USER_NO_ID_SEARCHED(HttpStatus.OK, "91000201", "검색된 아이디가 없습니다."),
    USER_NOT_USE_ACCOUNT(HttpStatus.OK, "91000202", "비활성화 계정 입니다."),
    USER_JOIN_EMPTY_ACCOUNT(HttpStatus.OK, "91000203", "아이디를 입력하여 주시기 바랍니다."),
    USER_JOIN_EMPTY_PWD(HttpStatus.OK, "91000204", "비밀번호를 입력하여 주시기 바랍니다."),
    USER_JOIN_RESUBSCRIPTION_IS_NOT_VALID(HttpStatus.OK, "91000214", "탈퇴 후 재가입 유효기간이 아닙니다."),
    USER_JOIN_DUPLICATE_ACCOUNT(HttpStatus.OK, "91000215", "중복된 계정 입니다."),
    USER_JOIN_DUPLICATE_PHONENUMBER(HttpStatus.OK, "91000218", "중복된 핸드폰 번호 입니다."),
    USER_JOIN_PHONENUMBER_NOT_AVAILABLE(HttpStatus.OK, "91000219", "사용할 수 없는 핸드폰 번호 입니다."),
    USER_INFORMATION_IS_NOT_RIGHT(HttpStatus.OK, "91000221", "로그인정보가 옳바르지 않습니다."),
    USER_TOKEN_ID_IS_NOT_RIGHT(HttpStatus.OK, "91000222", "토큰 ID가 옳바르지 않습니다."),
    USER_NO_SEARCHED(HttpStatus.OK, "91000223", "검색된 고객이 없습니다."),
    USER_JOIN_DUPLICATE_NICKNAME(HttpStatus.OK, "91000224", "중복된 닉네임 입니다."),
    USER_JOIN_IS_NULL(HttpStatus.OK, "91000225", "닉네임을 입력하여 주시기 바랍니다."),
    USER_PHONENUM_NO_SEARCHED(HttpStatus.OK, "91000226", "가입되지 않은 휴대폰 번호입니다."),
    USER_PHONENUM_EMAIL_NOT_MATCH(HttpStatus.OK, "91000227", "이메일과 폰번호가 일치하지 않습니다."),
    USER_ROLE_NOT_SHOP(HttpStatus.OK, "91000228", "상점 아이디 권한이 없습니다."),

    ;



    private final HttpStatus status;
    private final String statusCode;
    private final String statusMessage;

    ResultCode(HttpStatus status, String statusCode, String statusMessage) {
        this.status = status;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }


}
