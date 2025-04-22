package com.community.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_information__id")
    private Long id;
    @Column(name = "post_id")
    private Long postId;

    // 0.전부 1.토토핫 2.토찾사 3.토진사
    // 4.먹튀스팟 5.먹튀클럽 6.온카판
    // 7.토토군 8.슈어맨 9.토이소
    private int reportTyp;
    private String siteName;
    private String siteUrl;
    private String date;
    private int amount;
    private String accountNumber;
}
