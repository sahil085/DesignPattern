package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.ProjectOperations;
import com.ttn.bluebell.core.exception.EntityNotFoundException;
import com.ttn.bluebell.core.exception.UnSupportedOperationException;
import com.ttn.bluebell.domain.client.EClient;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.staffing.EProjectStaffingRequest;
import com.ttn.bluebell.durable.model.common.ResponseBean;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.project.ProjectResponseBean;
import com.ttn.bluebell.durable.model.project.billing.BillingInfo;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.repository.BaseProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by praveshsaini on 16/9/16.
 */
public abstract class ProjectTemplate<D extends Project, E extends EProject,T extends BillingInfo> implements ProjectOperations<D,T> {

    private BaseProjectRepository<E> baseProjectRepository;
    private Function<D, E> entity;
    private Function<E, D> durable;

    @Autowired
    private ClientTemplate clientTemplate;

    public ProjectTemplate(BaseProjectRepository<E> baseProjectRepository, Function<D, E> entity, Function<E, D> durable) {
        this.baseProjectRepository = baseProjectRepository;
        this.entity = entity;
        this.durable = durable;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseBean create(D project) {

        E dbProject = entity.apply(project);
        EClient eClient = clientTemplate.findClientById(project.getClient().getClientId());
        dbProject.setClient(eClient);
        //Assigning staff request state as not staffed
        dbProject.getStaffRequests().stream().forEach(
                eProjectStaffingRequest -> eProjectStaffingRequest.setState(StaffRequest.State.Open)
        );
        baseProjectRepository.saveAndFlush(dbProject);
        return createResponse(dbProject);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Set<D> findAll() {
        List<E> dbProjects = baseProjectRepository.findAllByActiveIsTrueOrderByProjectNameAsc();
       Set<D>  projects = dbProjects.stream().map(
                e ->    {
                    filterInActiveStaff(e);
                    D project = durable.apply(e);
                    updateOpenStaffRequests(project);
                    return project;
                }
        ).collect(Collectors.toSet());
        return projects;
    }

    @Override
    public ResponseBean update(Long projectId, D project) {

        projectExist(projectId);

        project.setProjectId(projectId);
        E dbProject = entity.apply(project);

        EClient eClient = clientTemplate.findClientById(project.getClient().getClientId());

        dbProject.setClient(eClient);
        baseProjectRepository.saveAndFlush(dbProject);
        return createResponse(dbProject);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T updateBillingInfo(Long projectId, T billingInfo) {
        throw new UnSupportedOperationException("exception.unsupported.operation");
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public BillingInfo getBillingInfo(Long projectId) {
        throw new UnSupportedOperationException("exception.unsupported.operation");
    }

    protected E findEntityById(Long projectId) {
        E dbProject = baseProjectRepository.findByProjectIdAndActiveIsTrue(projectId);
        if (dbProject == null){
            throw new EntityNotFoundException("exception.entity.notfound.project");
        }
        filterInActiveStaff(dbProject);
        return dbProject;
    }

    protected boolean projectExist(Long projectId){
        return findEntityById(projectId) != null;
    }

    public static void filterInActiveStaff(EProject eProject){
        List<EProjectStaffingRequest > staffList = eProject.getStaffRequests().stream().filter(
                staff -> (staff.getActive()==true) ).collect(Collectors.toList());
        eProject.setStaffRequests(staffList);
    }

    public static void updateOpenStaffRequests(Project project){
        List<StaffRequest> requests = project.getStaffRequests().stream().filter( staffRequest ->
                staffRequest.getState()== StaffRequest.State.Open
        ).collect(Collectors.toList());

        project.setOpenStaffRequests(requests.size());
    }

    private ResponseBean createResponse(EProject project) {
        ResponseBean responseBean = new ResponseBean();
        ProjectResponseBean responseContent = new ProjectResponseBean();
        responseContent.setProjectName(project.getProjectName());
        responseContent.setProjectId(project.getProjectId());
        responseBean.setContent(responseContent);
        responseBean.setStatus("SUCCESS");
        return responseBean;
    }
}
