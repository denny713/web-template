package com.ndp.model.entity;

import com.ndp.model.entity.base.IdEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role_mapping")
public class RoleMapping extends IdEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "read_access")
    private boolean readAccess;

    @Column(name = "write_access")
    private boolean writeAccess;
}
