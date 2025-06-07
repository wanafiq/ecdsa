package com.wanafiq.ecdsa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "roles")
public class Role extends Audit {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
