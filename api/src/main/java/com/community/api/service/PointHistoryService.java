package com.community.api.service;

import com.community.api.model.PointHistory;
import com.community.api.model.dto.PointHistoryDto;
import com.community.api.repository.PointHistoryCustomRepository;
import com.community.api.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PointHistoryService {

    @Value("${key.signupPoint}")
    private String signupPoint;

    @Value("${key.loginPoint}")
    private String loginPoint;

    @Value("${key.savePostPoint}")
    private String savePostPoint;

    @Value("${key.saveCommentPoint}")
    private String saveCommentPoint;

    @Value("${key.savePromotionPoint}")
    private String savePromotionPoint;

    private final PointHistoryRepository pointHistoryRepository;
    private final PointHistoryCustomRepository pointHistoryCustomRepository;

    public Page<PointHistoryDto> findAll(String keyword, Pageable pageable){
        return pointHistoryCustomRepository.findAll(keyword, pageable);
    }

    public PointHistory save(String username, String nickname, String actionType, Long postId) {
        int point = getActionPoint(actionType);

        String pointContent = String.format("[%s]님 %s + %d포인트", nickname, getActionDescription(actionType), point);

        PointHistory pointHistory = PointHistory.builder()
                .username(username)
                .pointContent(pointContent)
                .point(point)
                .postId(postId)
                .build();

        return pointHistoryRepository.save(pointHistory);
    }

    private String getActionDescription(String actionType) {
        switch (actionType) {
            case "signup":
                return "회원 가입 축하";
            case "savePost":
                return "게시글 작성";
            case "savePromotion":
                return "홍보글 작성";
            case "saveComment":
                return "댓글 작성";
            default:
                return "포인트 획득";
        }
    }

    private int getActionPoint(String actionType) {
        if ("signup".equals(actionType)) {
            return Integer.parseInt(signupPoint);
        } else if ("savePost".equals(actionType)) {
            return Integer.parseInt(savePostPoint);
        } else if ("savePromotion".equals(actionType)) {
            return Integer.parseInt(savePromotionPoint);
        } else if ("saveComment".equals(actionType)) {
            return Integer.parseInt(saveCommentPoint);
        } else {
            return 0;
        }
    }
}