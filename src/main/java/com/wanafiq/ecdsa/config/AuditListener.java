package com.wanafiq.ecdsa.config;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.wanafiq.ecdsa.model.Audit;
import com.wanafiq.ecdsa.common.SecurityUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditListener {

    @PrePersist
    private void prePersist(Audit audit) {
        if (audit.getId() == null) {
            Ulid ulid = UlidCreator.getMonotonicUlid();
            audit.setId(ulid.toString());
        }

        if (audit.getCreatedAt() == null) {
            audit.setCreatedAt(Instant.now());
        }

        audit.setCreatedBy(getAuditor());
    }

    @PreUpdate
    private void preUpdate(Audit audit) {
        audit.setModifiedAt(Instant.now());
        audit.setModifiedBy(getAuditor());
    }

    private String getAuditor() {
        String auditor = SecurityUtils.getPrincipalName();
        return auditor != null ? auditor : "system";
    }

}
