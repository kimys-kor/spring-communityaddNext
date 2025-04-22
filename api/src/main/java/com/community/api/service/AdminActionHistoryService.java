package com.community.api.service;

import com.community.api.model.AdminActionHistory;
import com.community.api.model.dto.AdminActionHistoryDto;
import com.community.api.repository.AdminActionHistoryCustomRepository;
import com.community.api.repository.AdminActionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminActionHistoryService {

    private final AdminActionHistoryRepository adminActionHistoryRepository;
    private final AdminActionHistoryCustomRepository adminActionHistoryCustomRepository;

    public AdminActionHistory save(int actionType, String username) {
        String content;

        switch (actionType) {
            case 1:
                content = "유저 포인트 추가/감소";
                break;
            case 2:
                content = "유저 접근 차단";
                break;
            case 3:
                content = "포인트 히스토리 조회";
                break;
            case 4:
                content = "IP 추가";
                break;
            case 5:
                content = "IP 삭제";
                break;
            case 6:
                content = "게시글 삭제";
                break;
            case 7:
                content = "게시글 이동";
                break;
            case 8:
                content = "게시글 작성";
                break;
            case 9:
                content = "게시글 수정";
                break;
            case 10:
                content = "댓글 작성";
                break;
            case 11:
                content = "댓글 수정";
                break;
            case 12:
                content = "댓글 삭제";
                break;
            case 13:
                content = "배너 추가";
                break;
            case 14:
                content = "배너 삭제";
                break;
            case 15:
                content = "배너 수정";
                break;
            case 16:
                content = "먹튀 신고 작성";
                break;
            case 17:
                content = "먹튀 신고 수정";
                break;
            case 18:
                content = "먹튀 신고 삭제";
                break;
            default:
                content = "알 수 없는 작업";
        }

        AdminActionHistory adminActionHistory = AdminActionHistory.builder()
                .actionType(actionType)
                .username(username)
                .content(content)
                .build();
        return adminActionHistoryRepository.save(adminActionHistory);
    }

    public Page<AdminActionHistoryDto> findAll(String keyword, Pageable pageable) {
        return adminActionHistoryCustomRepository.findAll(keyword, pageable);
    }
}
