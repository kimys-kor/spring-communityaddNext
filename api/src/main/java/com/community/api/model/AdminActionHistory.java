package com.community.api.model;

import com.community.api.model.base.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminActionHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1.유저포인트추가감 2.유저접근차단 3.포인트히스토리조회 4.ip추가 5.ip삭제
    // 6.게시글삭제 7.게시글이동 8.게시글작성 9.게시글수정 10.댓글작성
    // 11.댓글수정 12.댓글삭제 13.배너삭제
    private int actionType;
    private String username;
    private String content;

}
