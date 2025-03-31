package com.demo.gateway.domain.entity;


import com.demo.gateway.domain.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(
            name = "created_by",
            updatable = false,
            length = 100
    )
    @CreatedBy
    private Integer createdBy;
    @Column(
            name = "modified_at",
            insertable = false
    )
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Column(
            name = "modified_by",
            length = 100
    )
    @LastModifiedBy
    private Integer modifiedBy;
    @Column(
            name = "status",
            length = 10,
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public BaseEntity() {
    }

    @PrePersist
    public void beforeSave() {
        this.createdAt = new Date();
        this.status = Status.ACTIVE;
        this.modifiedAt = new Date();
    }

    @PreUpdate
    public void afterUpdate() {
        this.modifiedAt = new Date();
    }

}
