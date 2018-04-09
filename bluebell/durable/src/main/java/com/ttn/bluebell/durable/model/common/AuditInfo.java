package com.ttn.bluebell.durable.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class AuditInfo {
    @JsonProperty
    private ZonedDateTime createdOn;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private ZonedDateTime updatedOn;
    @JsonProperty
    private  String updatedBy;

    public AuditInfo(
            @JsonProperty("createdOn") ZonedDateTime createdOn,
            @JsonProperty("createdBy") String createdBy,
            @JsonProperty("updatedOn") ZonedDateTime updatedOn,
            @JsonProperty("updatedBy") String updatedBy) {
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ZonedDateTime getUpdatedOn() {
        return updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    private AuditInfo(Builder builder) {
        this.createdOn = builder.createdOn;
        this.createdBy = builder.createdBy;
        this.updatedOn = builder.updatedOn;
        this.updatedBy = builder.updatedBy;
    }

    public static class Builder {
        private ZonedDateTime createdOn;
        private ZonedDateTime updatedOn;
        private String createdBy;
        private  String updatedBy;

        public Builder createdOn(ZonedDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedOn(ZonedDateTime updatedOn) {
            this.updatedOn = updatedOn;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public AuditInfo build() {
            return new AuditInfo(this);
        }
    }
}
