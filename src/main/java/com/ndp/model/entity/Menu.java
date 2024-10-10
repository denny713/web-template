package com.ndp.model.entity;

import com.ndp.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menus")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "icon")
    private String icon;
}
