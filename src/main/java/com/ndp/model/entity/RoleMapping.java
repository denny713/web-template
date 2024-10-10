package com.ndp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role_mapping")
public class RoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "view_access")
    private boolean viewAccess;

    @Column(name = "create_access")
    private boolean createAccess;

    @Column(name = "delete_access")
    private boolean deleteAccess;

    @Column(name = "edit_access")
    private boolean editAccess;
}
