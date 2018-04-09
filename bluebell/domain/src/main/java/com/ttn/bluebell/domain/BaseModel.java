package com.ttn.bluebell.domain;

import org.joda.time.DateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ttn on 18/10/16.
 */

@MappedSuperclass
public class BaseModel{

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="MODIFIED_BY")
    private String modifiedBy;

    @Column(name="DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name="DATE_MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @PrePersist
    void onCreate() {
        String currentUser = getCurrentUser();
        Date date = DateTime.now().toDate();
        this.setCreatedBy(currentUser);
        this.setCreatedDate(date);
        this.setModifiedBy(currentUser);
        this.setModifiedDate(date);
    }

    @PreUpdate
    void onPersist() {
        this.setModifiedBy(getCurrentUser());
        this.setModifiedDate(DateTime.now().toDate());
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                return (String) authentication.getPrincipal();
            } catch (NullPointerException npe) {
                return null;
            }
        }
        return null;
    }
}
