package com.community.api.model;

import com.community.api.model.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    //    1.파트너
    //    2.해외축구분석 3.아시아축구분석 4.MLB분석 5.KBO/NPB분석 6.NBA분석 7.국내외농구분석 8.배구분석
    //    9.안구정화 10.유머이슈 11.나는분석왕 12.자유게시판 13.피해사례
    //    14.이벤트 15.포인트교환신청
    //    16.일반홍보 17.꽁머니홍보 18.구인구직
    //    19.공지사항 20.1:1문의 21: 먹튀사이트
    private int postType;
    private boolean notification;
    private String username;
    private String nickname;
    private String userIp;
    private String thumbNail;
    private String title;
    @Lob
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String content;
    private String code;
    private int hit;
    private int hate;
    private int likes;
    private boolean isDeleted;
    private int replyNum;


    @Builder.Default
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    List<Comment> commentList = new ArrayList<Comment>();

}
