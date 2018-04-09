package com.ttn.bluebell.durable.model.project;

import com.ttn.bluebell.durable.model.common.ResponseContent;

/**
 * Created by ashutoshmeher on 20/9/16.
 */
public class ProjectResponseBean implements ResponseContent{

    private Long projectId;

    private String projectName;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
