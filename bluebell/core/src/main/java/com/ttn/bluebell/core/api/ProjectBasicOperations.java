package com.ttn.bluebell.core.api;

import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.durable.model.project.Project;

import java.util.List;

/**
 * Created by praveshsaini on 23/9/16.
 */
public interface ProjectBasicOperations {

    Project findById (Long projectId);
    void deactivateProjectOnEndDate();
    void delete(Long projectId);
    Project findProjectByName(String name);
    List<EProject> findProjectByRegion(String regionName);
}
