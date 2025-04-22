package com.community.api.model.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {

    @CreatedDate
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime createdDt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime updatedDt;


    @PrePersist
    protected void onCreate() {
        this.createdDt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.updatedDt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

}
