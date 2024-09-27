package com.ndp.model.entity.base;

import com.ndp.util.AccountUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity extends IdEntity implements Serializable {

    @Column(name = "name")
    protected String name;

    @Column(name = "is_active")
    protected boolean active;

    @Column(name = "is_deleted")
    protected boolean deleted;

    @Column(name = "created_by", length = 50)
    protected String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_date")
    protected Date createdDate;

    @Column(name = "updated_by", length = 50)
    protected String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_date")
    protected Date updatedDate;

    @PrePersist
    public void prePersist() {
        this.active = true;
        this.deleted = false;
        this.setCreatedBy(AccountUtil.getCurrentUsername());
        this.setCreatedDate(new Date());
        this.setUpdatedBy(AccountUtil.getCurrentUsername());
        this.setUpdatedDate(new Date());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedBy(AccountUtil.getCurrentUsername());
        this.setUpdatedDate(new Date());
    }
}
