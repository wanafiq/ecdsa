package com.wanafiq.ecdsa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanafiq.ecdsa.config.AuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@EntityListeners(AuditListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Audit {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @JsonIgnore
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @JsonIgnore
    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @Column(name = "modified_at")
    @LastModifiedDate
    private Instant modifiedAt;

    @JsonIgnore
    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

}
