package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.ProjectBasicOperations;
import com.ttn.bluebell.core.exception.EntityNotFoundException;
import com.ttn.bluebell.core.exception.UnSupportedTypeException;
import com.ttn.bluebell.core.mapper.project.FixedPriceProjectMapper;
import com.ttn.bluebell.core.mapper.project.NonBillableProjectMapper;
import com.ttn.bluebell.core.mapper.project.TimeAndMoneyProjectMapper;
import com.ttn.bluebell.domain.project.EFixedPriceProject;
import com.ttn.bluebell.domain.project.ENonBillableProject;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.project.ETimeAndMoneyProject;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.repository.ProjectRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by praveshsaini on 23/9/16.
 */
@EnableCaching(proxyTargetClass = true)
@Service
@Named("projectBasicTemplate")
public class ProjectBasicTemplate implements ProjectBasicOperations {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Project findById(Long projectId) {
        EProject dbProject = projectRepository.findByProjectIdAndActiveIsTrue(projectId);
        if (dbProject == null){
            throw new EntityNotFoundException("exception.entity.notfound.project");
        }
//        ProjectTemplate.filterInActiveStaff(dbProject);
        Project project = map(dbProject);
        return project;
    }

    @Override
    public void deactivateProjectOnEndDate(){
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            DateTime currDate = new DateTime(fmt.parse(LocalDate.now().toString() + " 05:30:00"));
            List<EProject> projectList = projectRepository.findAllByActive(true);
            projectList.forEach(eProject -> {
                if(eProject.getEndDate()!=null &&
                        (currDate.isAfter(new DateTime(eProject.getEndDate())) ||
                                currDate.isEqual(new DateTime(eProject.getEndDate())))){
                    delete(eProject.getProjectId());
                }
            });
        }catch (ParseException pe){}
    }

    @Override
    public void delete(Long projectId) {
        EProject eProject = projectRepository.findByProjectIdAndActiveIsTrue(projectId);


        eProject.setActive(false);
        eProject.getStaffRequests().stream().forEach(eProjectStaffingRequest -> {
            eProjectStaffingRequest.setActive(false);
        });

        projectRepository.saveAndFlush(eProject);

    }

    @Override
    public Project findProjectByName(String name){
        EProject eProject = projectRepository.findOneByProjectNameAndActive(name, true);
        if(eProject == null){
            return null;
        }
        Project project = map(eProject);
        return project;
    }

    @Override
    public List<EProject> findProjectByRegion(String region){
        return  projectRepository.findByRegion(region);
    }

    public static Project map(EProject dbProject) {

        if (dbProject instanceof EFixedPriceProject){
            return FixedPriceProjectMapper.durable.apply((EFixedPriceProject) dbProject);
        }
        if (dbProject instanceof ETimeAndMoneyProject){
            return TimeAndMoneyProjectMapper.durable.apply((ETimeAndMoneyProject) dbProject);
        }
        if (dbProject instanceof ENonBillableProject){
            return NonBillableProjectMapper.durable.apply((ENonBillableProject) dbProject);
        }
        throw new UnSupportedTypeException();
    }
}
