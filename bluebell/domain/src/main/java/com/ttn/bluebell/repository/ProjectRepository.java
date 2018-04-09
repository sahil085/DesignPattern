package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.durable.model.project.Project;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by praveshsaini on 22/9/16.
 */
@Repository
public interface ProjectRepository extends BaseProjectRepository<EProject> {
    EProject findOneByProjectNameAndActive(String projectName, boolean flag);
    List<EProject> findByRegion(String region);
    List<EProject> findByProjectType(Project.Type projectType);
    List<EProject> findAllByActive(Boolean active);
}

