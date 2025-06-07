package com.wanafiq.ecdsa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanafiq.ecdsa.common.Constant;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "users")
public class User extends Audit {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DEFAULT_DATE_FORMAT, timezone = Constant.DEFAULT_TIMEZONE)
    @Column(name = "registered_at")
    private Instant registeredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DEFAULT_DATE_FORMAT, timezone = Constant.DEFAULT_TIMEZONE)
    @Column(name = "activated_at")
    private Instant activatedAt;

    @JsonIgnore
    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @JsonIgnore
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @JsonIgnore
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @JsonIgnore
    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

}
