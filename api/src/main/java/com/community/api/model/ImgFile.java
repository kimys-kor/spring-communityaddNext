package com.community.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ImgFile")
public class ImgFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, columnDefinition = "varchar(250)")
    private String origFileName;
    @Column(nullable = false, columnDefinition = "varchar(250)")
    private String fileName;
    @Column(nullable = false, columnDefinition = "varchar(250)")
    private String filePath;
    @Column(nullable = false)
    @CreatedDate
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime uploadDate;
}

