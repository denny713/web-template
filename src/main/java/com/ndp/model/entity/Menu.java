package com.ndp.model.entity;

import com.ndp.model.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "icon")
    private String icon;
}
