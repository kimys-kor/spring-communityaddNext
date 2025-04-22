package com.community.api.model;

import com.community.api.model.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Banner extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partnerName;
    // 썸네일url
    @Column(unique = true)
    private String thumbNail;
    // 파트너링크url
    private String partnerUrl;


    private int clickNum;

}
