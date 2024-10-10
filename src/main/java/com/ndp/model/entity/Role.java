package com.ndp.model.entity;

import com.ndp.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleMapping> roleMapping;
}
