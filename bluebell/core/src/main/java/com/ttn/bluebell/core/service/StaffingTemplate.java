package com.ttn.bluebell.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.api.StaffingOperations;
import com.ttn.bluebell.core.exception.BusinessValidationFailureException;
import com.ttn.bluebell.core.exception.EntityNotFoundException;
import com.ttn.bluebell.core.exception.StaffAllocationFailedException;
import com.ttn.bluebell.core.exception.StaffDeAllocationFailedException;
import com.ttn.bluebell.core.util.DateUtil;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.staffing.EProjectStaffingRequest;
import com.ttn.bluebell.domain.staffing.EStaffing;
import com.ttn.bluebell.durable.model.common.MailType;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.employee.ProjectDetailsDTO;
import com.ttn.bluebell.durable.model.employee.UpcomingDeallocationDTO;
import com.ttn.bluebell.durable.model.event.notification.*;
import com.ttn.bluebell.durable.model.staffing.AllocationDetails;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.staffing.Staffing;
import com.ttn.bluebell.repository.ProjectRepository;
import com.ttn.bluebell.repository.StaffRequestRepository;
import com.ttn.bluebell.repository.StaffingRepository;
import com.ttn.bluebell.repository.customRepository.EmployeeRegionRepositoryImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.dozer.DozerBeanMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffingTemplate implements StaffingOperations {
    private static final Logger logger = LoggerFactory.getLogger(NotificationRequest.class);

    @Autowired
    private EmployeeOperations employeeOperations;

    @Autowired
    private EmployeeRegionRepositoryImpl employeeRegionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private StaffRequestRepository staffRequestRepository;
    @Autowired
    private DozerBeanMapper beanMapper;
    @Autowired
    private EmployeeOperations employeeService;
    @Autowired
    private StaffingRepository staffingRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StaffingTemplate.class);
    @Value("#{'${email.cc.constants}'.split(',')}")
    private List<String> ccEmails;
    @Value("${spring.profiles.active}")
    private String environment;
    @Resource
    Environment env;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public StaffRequest updateStaffingRequest(Long projectId, StaffRequest staffRequest, String loggedInUser) {

        EProject dbProject = findProjectEntity(projectId);
        EProjectStaffingRequest staffingRequest = beanMapper.map(staffRequest, EProjectStaffingRequest.class);
        staffingRequest.setProject(dbProject);
        //for addtional staff request
        if (staffingRequest.getId() == null) {
            staffingRequest.setState(StaffRequest.State.Open);
            staffingRequest = staffRequestRepository.saveAndFlush(staffingRequest);
        } else {
            try {

                staffingRequest = createOrUpdateEProjectStaffingRequest(staffingRequest);

            } catch (CloneNotSupportedException e) {

            }
        }
        if (staffRequest.getIsAddNew()) {
            publishMailingEvent(staffRequest, dbProject, MailType.NeedGenerated, loggedInUser);
        }
        return beanMapper.map(staffingRequest, StaffRequest.class);
    }

    private EProjectStaffingRequest createOrUpdateEProjectStaffingRequest(EProjectStaffingRequest staffingRequest) throws CloneNotSupportedException {
        EProjectStaffingRequest updateStaffingRequest = staffRequestRepository.findOne(staffingRequest.getId());
        EProjectStaffingRequest newstaffingRequest = null;
        if (!updateStaffingRequest.getAllocationPercentage().equals(staffingRequest.getAllocationPercentage())) {
            newstaffingRequest = createEProjectStaffingRequest(staffingRequest, updateStaffingRequest);
        } else {
            staffRequestRepository.saveAndFlush(staffingRequest);
        }
        List<EStaffing> eStaffings = staffingRepository.findByStaffingRequestAndProjectAndState(staffingRequest,staffingRequest.getProject(), Staffing.State.Allocated);
        if (eStaffings.size()>0) {
            String username = eStaffings.get(0).getEmail();
            ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO(username);
            projectDetailsDTO.setProjectId(staffingRequest.getProject().getProjectId());
            projectDetailsDTO.setAllocationPercentage(staffingRequest.getAllocationPercentage());
            projectDetailsDTO.setProjectName(staffingRequest.getProject().getProjectName());
            Boolean isAllocated =false;
            if (staffingRequest.getState().equals(StaffRequest.State.Closed))
                isAllocated = true;
            projectDetailsDTO.setAllocated(isAllocated);
            try {
                employeeOperations.pushProjectsInfo(projectDetailsDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return newstaffingRequest != null ? newstaffingRequest : staffingRequest;
    }

    private EProjectStaffingRequest createEProjectStaffingRequest(EProjectStaffingRequest staffingRequest, EProjectStaffingRequest updateStaffingRequest) throws CloneNotSupportedException {
        EProjectStaffingRequest newstaffingRequest = (EProjectStaffingRequest) staffingRequest.clone();
        newstaffingRequest.setStartDate(new Date());
        newstaffingRequest.setReallocated(false);
        newstaffingRequest = staffRequestRepository.saveAndFlush(newstaffingRequest);
        List<EStaffing> eStaffings = staffingRepository.findByStaffingRequestAndState(updateStaffingRequest, Staffing.State.Allocated);
        List<EStaffing> eStaffingsTObeSaved = new ArrayList<>();
        for (EStaffing eStaffing : eStaffings) {
            if (eStaffing.getStaffingRequest().getActive()) {
                EStaffing staffing = new EStaffing(eStaffing, newstaffingRequest);
                eStaffingsTObeSaved.add(staffing);
            }
        }
        staffingRepository.save(eStaffingsTObeSaved);
        updateStaffingRequest.setEndDate(DateUtil.yesterday());
        updateStaffingRequest.setActive(false);
        updateStaffingRequest.setReallocated(true);
        staffRequestRepository.save(updateStaffingRequest);
        return newstaffingRequest;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long projectId, Long staffId) {
        EProjectStaffingRequest eProjectStaffingRequest = staffRequestRepository.findByIdAndActiveIsTrue(staffId);
        eProjectStaffingRequest.setActive(false);
        staffRequestRepository.saveAndFlush(eProjectStaffingRequest);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void allocate(Long projectId, Long staffingRequestId, String email, String loggedInUser) {
        Employee employee = employeeService.findEmployeeByEmail(email);
        EProject dbProject = findProjectEntity(projectId);
        EProjectStaffingRequest dbStaffingRequest = staffRequestRepository.findByIdAndProjectAndActiveIsTrue(staffingRequestId, dbProject);
        if (dbStaffingRequest == null) {
            throw new EntityNotFoundException("exception.entity.notfound.staffing");
        }
        if (dbStaffingRequest.getState() == StaffRequest.State.Closed) {
            throw new StaffAllocationFailedException("exception.staff.allocation.served");
        }
        long noOfActiveAllocationInProject = staffingRepository.countByProjectAndStateAndEmailBetweenStartDateAndEndDate(dbProject, Staffing.State.Allocated, email,
                dbStaffingRequest.getEndDate(), dbStaffingRequest.getStartDate());
        long noOfAllocation = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(dbStaffingRequest, dbProject, Staffing.State.Allocated, email);
        long noOfDeAllocations = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(dbStaffingRequest, dbProject, Staffing.State.DeAllocated, email);
        if (noOfAllocation == noOfDeAllocations && noOfActiveAllocationInProject == 0) {
            EStaffing dbStaffing = new EStaffing();
            dbStaffing.setProject(dbProject);
            dbStaffing.setStaffingRequest(dbStaffingRequest);
            dbStaffing.setState(Staffing.State.Allocated);
            dbStaffing.setDate(LocalDate.now());
            dbStaffing.setEmail(email);
            dbStaffing.setEmployeeName(employee.getName());
            staffingRepository.saveAndFlush(dbStaffing);
            dbStaffingRequest.setState(StaffRequest.State.Closed);
            staffRequestRepository.saveAndFlush(dbStaffingRequest);
            sendRejectionMails(dbStaffingRequest, dbProject, employee, email, loggedInUser);
            DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
            employeeService.findEmployeeByEmail(dbStaffing.getEmail());
            ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO(email);
            projectDetailsDTO.setProjectId(dbProject.getProjectId());
            projectDetailsDTO.setProjectName(dbProject.getProjectName());
            projectDetailsDTO.setAllocationPercentage(dbStaffingRequest.getAllocationPercentage());
            if (dbStaffingRequest.getState().equals(StaffRequest.State.Closed))
                projectDetailsDTO.setAllocated(true);
            try {
                employeeOperations.pushProjectsInfo(projectDetailsDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            publishMailingEvent(employee, dbProject, MailType.Allocated, loggedInUser, outputFormatter.format(dbStaffingRequest.getStartDate()), outputFormatter.format(dbStaffingRequest.getEndDate()));
//            publishMailingEvent(employee, dbProject, MailType.AllocatedNewer, loggedInUser, outputFormatter.format(dbStaffingRequest.getStartDate()), outputFormatter.format(dbStaffingRequest.getEndDate()));

        } else
            throw new StaffAllocationFailedException("exception.staff.allocation.emp");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deAllocate(Long projectId, Long staffingRequestId, String email, String loggedInUser, Date lastWorkingDate) {
        Employee employee = employeeService.findEmployeeByEmail(email);
        EProject dbProject = findProjectEntity(projectId);
        EProjectStaffingRequest dbStaffingRequest = staffRequestRepository.findByIdAndProjectAndActiveIsTrue(staffingRequestId, dbProject);
        if (dbStaffingRequest == null) {
            throw new EntityNotFoundException("exception.entity.notfound.staffing");
        }
        if (dbStaffingRequest.getState() != StaffRequest.State.Closed) {
            throw new StaffDeAllocationFailedException("exception.staff.deallocation.served");
        }
        long noOfAllocation = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(dbStaffingRequest, dbProject, Staffing.State.Allocated, email);
        long noOfDeAllocations = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(dbStaffingRequest, dbProject, Staffing.State.DeAllocated, email);
        if (noOfAllocation > noOfDeAllocations) {
            EStaffing deAllocatingStaff = new EStaffing();
            deAllocatingStaff.setProject(dbProject);
            deAllocatingStaff.setStaffingRequest(dbStaffingRequest);
            deAllocatingStaff.setState(Staffing.State.DeAllocated);
            deAllocatingStaff.setDate(LocalDate.now());
            deAllocatingStaff.setEmployeeName(employee.getName());
            deAllocatingStaff.setEmail(email);
            staffingRepository.saveAndFlush(deAllocatingStaff);
            Date lastday = lastWorkingDate == null ? new Date() : lastWorkingDate;
            dbStaffingRequest.setEndDate(lastday);
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                if (dbStaffingRequest.getEndDate().before(fmt.parse(LocalDate.now().toString())) || DateUtils.isSameDay(dbStaffingRequest.getEndDate(), fmt.parse(LocalDate.now().toString()))) {
                    dbStaffingRequest.setActive(false);
                }
                employeeService.findEmployeeByEmail(email);
            } catch (ParseException pe) {
            }
            dbStaffingRequest.setReallocated(false);
            staffRequestRepository.saveAndFlush(dbStaffingRequest);
            DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");

            ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO(email);
            projectDetailsDTO.setProjectName(dbProject.getProjectName());
            projectDetailsDTO.setProjectId(dbProject.getProjectId());
            projectDetailsDTO.setAllocationPercentage(dbStaffingRequest.getAllocationPercentage());
            if (dbStaffingRequest.getState().equals(StaffRequest.State.Closed))
                projectDetailsDTO.setAllocated(false);
            try {
                employeeOperations.pushProjectsInfo(projectDetailsDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            publishMailingEvent(employee, dbProject, MailType.DeAllocated, loggedInUser, outputFormatter.format(dbStaffingRequest.getStartDate()), outputFormatter.format(dbStaffingRequest.getEndDate()));
//            publishMailingEvent(employee, dbProject, MailType.DeAllocatedNewer, loggedInUser, outputFormatter.format(dbStaffingRequest.getStartDate()), outputFormatter.format(dbStaffingRequest.getEndDate()));
        } else
            throw new StaffDeAllocationFailedException("exception.staff.deallocation.emp");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void nominate(Long projectId, Long staffingRequestId, String email, String loggedInUser) {
        Employee employee = employeeService.findEmployeeByEmail(email);
        EProject dbProject = findProjectEntity(projectId);
        EProjectStaffingRequest dbStaffingRequest = staffRequestRepository.findByIdAndProjectAndActiveIsTrue(staffingRequestId, dbProject);
        long noOfNominations = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(dbStaffingRequest, dbProject, Staffing.State.Nominated, email);
        long noOfRejections = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(dbStaffingRequest, dbProject, Staffing.State.NominationRejected, email);
        EStaffing eStaffing = staffingRepository.findTopByStaffingRequestAndEmailOrderByIdDesc(dbStaffingRequest, email);
        if (dbStaffingRequest == null)
            throw new EntityNotFoundException("exception.entity.notfound.staffing");
        if (dbStaffingRequest.getState() == StaffRequest.State.Closed)
            throw new StaffAllocationFailedException("exception.staff.allocation.served");
        if (noOfNominations == noOfRejections || eStaffing.getState().name().equalsIgnoreCase(Staffing.State.DeAllocated.name()) || eStaffing.getState().name().equalsIgnoreCase(Staffing.State.NominationRejected.name())) {
            EStaffing nominateStaffing = new EStaffing();
            nominateStaffing.setEmail(email);
            nominateStaffing.setEmployeeName(employee.getName());
            nominateStaffing.setProject(dbProject);
            nominateStaffing.setStaffingRequest(dbStaffingRequest);
            nominateStaffing.setState(Staffing.State.Nominated);
            nominateStaffing.setDate(LocalDate.now());
            staffingRepository.saveAndFlush(nominateStaffing);
            DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
            publishMailingEvent(employee, dbProject, MailType.Nominated, loggedInUser, outputFormatter.format(dbStaffingRequest.getStartDate()), outputFormatter.format(dbStaffingRequest.getEndDate()));
        } else
            throw new StaffAllocationFailedException("exception.staff.nomination.emp");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectNomination(Long projectId, Long staffingRequestId, String email, String loggedInUser) {
        Employee employee = employeeService.findEmployeeByEmail(email);
        EProject dbProject = findProjectEntity(projectId);
        EProjectStaffingRequest dbStaffingRequest = staffRequestRepository.findByIdAndProjectAndActiveIsTrue(staffingRequestId, dbProject);
        long noOfNominations = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(dbStaffingRequest, dbProject, Staffing.State.Nominated, email);
        long noOfRejections = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(dbStaffingRequest, dbProject, Staffing.State.NominationRejected, email);
        if (dbStaffingRequest == null)
            throw new EntityNotFoundException("exception.entity.notfound.staffing");
        if (dbStaffingRequest.getState() == StaffRequest.State.Closed)
            throw new StaffAllocationFailedException("exception.staff.allocation.served");
        if (noOfNominations > noOfRejections) {
            EStaffing rejectStaffing = new EStaffing();
            rejectStaffing.setEmail(email);
            rejectStaffing.setEmployeeName(employee.getName());
            rejectStaffing.setProject(dbProject);
            rejectStaffing.setStaffingRequest(dbStaffingRequest);
            rejectStaffing.setState(Staffing.State.NominationRejected);
            rejectStaffing.setDate(LocalDate.now());
            staffingRepository.saveAndFlush(rejectStaffing);
//            publishMailingEvent(employee,dbProject,MailType.NominationRejected,loggedInUser);
        } else
            throw new StaffAllocationFailedException("exception.staff.reject.no");
    }

    @Override
    public Staffing findLatestByStaffRequestAndState(StaffRequest staffRequest, Staffing.State state) {
        EProjectStaffingRequest eProjectStaffingRequest = beanMapper.map(staffRequest, EProjectStaffingRequest.class);
        EStaffing eStaffing = staffingRepository.findTopByStaffingRequestAndStateOrderByIdDesc(eProjectStaffingRequest, state);
        if (eStaffing == null)
            return null;
        return beanMapper.map(eStaffing, Staffing.class);
    }

    @Override
    public List<Staffing> findByStaffRequestAndProjectAndState(EProjectStaffingRequest staffingRequest, EProject project, Staffing.State state) {
        List<EStaffing> eStaffings = staffingRepository.findByStaffingRequestAndProjectAndState(staffingRequest, project, state);
        List<Staffing> staffings = new ArrayList<>();
        for (EStaffing eStaffing : eStaffings)
            staffings.add(beanMapper.map(eStaffing, Staffing.class));
        return staffings;
    }

    @Override
    public List<Staffing> validStaffing(StaffRequest staffRequest, Staffing.State state) {
        EProjectStaffingRequest eProjectStaffingRequest = beanMapper.map(staffRequest, EProjectStaffingRequest.class);
        List<Staffing> staffings = findByStaffRequestAndState(staffRequest, state);
        List<Staffing> validStaffing = new ArrayList<>();
        for (Staffing staffing : staffings) {
            EStaffing latestStaffing = staffingRepository.findTopByStaffingRequestAndEmailOrderByIdDesc(eProjectStaffingRequest, staffing.getEmail());
            if (staffing.getId().longValue() == latestStaffing.getId().longValue())
                validStaffing.add(staffing);
        }
        return validStaffing;
    }

    @Async
    void sendRejectionMails(EProjectStaffingRequest dbStaffingRequest, EProject dbProject, Employee employee, String email, String loggedInUser) {
        EStaffing nominationIfAny = staffingRepository.findTopByStaffingRequestAndStateAndEmailOrderByIdDesc(dbStaffingRequest, Staffing.State.Nominated, email);
        List<Staffing> nominations = findByStaffRequestAndProjectAndState(dbStaffingRequest, dbProject, Staffing.State.Nominated);
        List<Staffing> rejectedNominations = findByStaffRequestAndProjectAndState(dbStaffingRequest, dbProject, Staffing.State.NominationRejected);
        List<Staffing> validStaffings = new ArrayList<>();
        for (Staffing nomination : nominations) {
            boolean flag = true;
            if (nominationIfAny != null && nomination.getId().longValue() == nominationIfAny.getId().longValue())
                flag = false;
            for (Staffing rejectedNomination : rejectedNominations)
                if (nomination.getEmail().equalsIgnoreCase(employee.getEmailAddress()) || nomination.getEmail().equalsIgnoreCase(rejectedNomination.getEmail()) && nomination.getId() < rejectedNomination.getId())
                    flag = false;
            if (flag)
                validStaffings.add(nomination);
        }
        for (Staffing validStaffing : validStaffings)
            autoRejectNomination(dbProject, dbStaffingRequest, validStaffing.getEmail(), loggedInUser);
    }

    @Async
    void publishMailingEvent(Employee employee, EProject project, MailType state, String loggedInUser, String startStaffingDate, String lastStaffingDate) {
        List<Employee> managers = findManagers(project, loggedInUser);
        if (MailType.Allocated.name().equalsIgnoreCase(state.name())) {
            AllocationNotificationRequest allocationNotificationRequest = new AllocationNotificationRequest(
                    employee.getName(),
                    managers.get(0).getEmailAddress(),
                    project.getProjectName(),
                    managers,
                    startStaffingDate, lastStaffingDate);
            logger.info("Sending Mail with following details : " + allocationNotificationRequest);
            publisher.publishEvent(allocationNotificationRequest);
        } else if (MailType.DeAllocated.name().equalsIgnoreCase(state.name())) {
            DeAllocationNotificationRequest deAllocationNotificationRequest = new DeAllocationNotificationRequest(
                    employee.getName(),
                    managers.get(0).getEmailAddress(),
                    project.getProjectName(),
                    managers,
                    startStaffingDate, lastStaffingDate
            );
            logger.info("Sending Mail with following details : " + deAllocationNotificationRequest);
            publisher.publishEvent(deAllocationNotificationRequest);
        } else if (MailType.Nominated.name().equalsIgnoreCase(state.name())) {
            NominationNotificationRequest nominationNotificationRequest = new NominationNotificationRequest(
                    employee.getName(),
                    managers.get(0).getEmailAddress(),
                    project.getProjectName(),
                    managers,
                    startStaffingDate, lastStaffingDate
            );
            logger.info("Sending Mail with following details : " + nominationNotificationRequest);
            publisher.publishEvent(nominationNotificationRequest);
        } else if (MailType.NominationRejected.name().equalsIgnoreCase(state.name())) {
            RejectionNotificationRequest rejectionNotificationRequest = new RejectionNotificationRequest(
                    employee.getName(),
                    managers.get(0).getEmailAddress(),
                    project.getProjectName(),
                    findManagers(project, loggedInUser)
            );
            logger.info("Sending Mail with following details : " + rejectionNotificationRequest);
            publisher.publishEvent(rejectionNotificationRequest);
        } else if (MailType.AllocatedNewer.name().equalsIgnoreCase(state.name())) {
            AllocationNewerNotificationRequest allocationNewerNotificationRequest = new AllocationNewerNotificationRequest(
                    employee.getName(),
                    employee.getEmailAddress(),
                    project.getProjectName(),
                    managers,
                    startStaffingDate, lastStaffingDate);
            logger.info("Sending Mail with following details : " + allocationNewerNotificationRequest);
            publisher.publishEvent(allocationNewerNotificationRequest);
        } else if (MailType.DeAllocatedNewer.name().equalsIgnoreCase(state.name())) {
            DeAllocationNewerNotificationRequest deAllocationNewerNotificationRequest = new DeAllocationNewerNotificationRequest(
                    employee.getName(),
                    managers.get(0).getEmailAddress(),
                    project.getProjectName(),
                    managers,
                    startStaffingDate, lastStaffingDate
            );
            logger.info("Sending Mail with following details : " + deAllocationNewerNotificationRequest);
            publisher.publishEvent(deAllocationNewerNotificationRequest);
        }
    }

    private void publishMailingEvent(StaffRequest staffingRequest, EProject project, MailType state, String loggedInUser) {
        if (MailType.NeedGenerated.name().equalsIgnoreCase(state.name())) {
            StaffingRequestNotificationRequest staffingRequestNotificationRequest = new StaffingRequestNotificationRequest(
                    staffingRequest,
                    project.getProjectName(),
                    findManagers(project, loggedInUser)
            );
            logger.info("Sending Mail with following details : " + staffingRequestNotificationRequest);
            publisher.publishEvent(staffingRequestNotificationRequest);
        }
    }

    private void autoRejectNomination(EProject project, EProjectStaffingRequest staffingRequest, String email, String loggedInUser) {
        Employee employee = employeeService.findEmployeeByEmail(email);
        long noOfNominations = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(staffingRequest, project, Staffing.State.Nominated, email);
        long noOfRejections = staffingRepository.countByStaffingRequestAndProjectAndStateAndEmail(staffingRequest, project, Staffing.State.NominationRejected, email);
        if (noOfNominations > noOfRejections) {
            EStaffing rejectStaffing = new EStaffing();
            rejectStaffing.setEmployeeName(employee.getName());
            rejectStaffing.setEmail(email);
            rejectStaffing.setProject(project);
            rejectStaffing.setStaffingRequest(staffingRequest);
            rejectStaffing.setState(Staffing.State.NominationRejected);
            rejectStaffing.setDate(LocalDate.now());
            staffingRepository.saveAndFlush(rejectStaffing);
//            publishMailingEvent(employee, project, MailType.NominationRejected, loggedInUser);
        }
    }

    public EProject findProjectEntity(Long projectId) {
        EProject dbProject = projectRepository.findByProjectIdAndActiveIsTrue(projectId);
        if (dbProject == null)
            throw new EntityNotFoundException("exception.entity.notfound.project");
        return dbProject;
    }

    private List<Staffing> findByStaffRequestAndState(StaffRequest staffRequest, Staffing.State state) {
        EProjectStaffingRequest eProjectStaffingRequest = beanMapper.map(staffRequest, EProjectStaffingRequest.class);
        List<EStaffing> eStaffings = staffingRepository.findByStaffingRequestAndState(eProjectStaffingRequest, state);
        List<Staffing> staffings = new ArrayList<>();
        for (EStaffing eStaffing : eStaffings)
            staffings.add(beanMapper.map(eStaffing, Staffing.class));
        return staffings;
    }

    public List<Employee> findManagers(EProject project, String loggedInUser) {
//        List<EProjectStaffingRequest> staffingRequests = staffRequestRepository.findByProjectAndTitleAndState(project,"PROJECT MANAGER",StaffRequest.State.Closed);
//        List<EStaffing> allocated = staffingRepository.findByStaffingRequestInAndState(staffingRequests, Staffing.State.Allocated);
//        List<EStaffing> deallocated = staffingRepository.findByStaffingRequestInAndState(staffingRequests, Staffing.State.DeAllocated);
        List<Employee> activeHeads = new ArrayList<>();
//        for (EStaffing projectManagerAllocated:allocated){
//            boolean flag = true;
//            for(EStaffing projectManagerDeAllocated:deallocated){
//                if(projectManagerAllocated.getEmail().equalsIgnoreCase(projectManagerDeAllocated.getEmail()) && projectManagerAllocated.getId().longValue()<projectManagerDeAllocated.getId().longValue()){
//                    flag = false;
//                    break;
//                }
//            }
//            if(flag)
//                activeHeads.add(employeeService.findEmployeeByEmail(projectManagerAllocated.getEmail()));
//        }
//        if(activeHeads==null || activeHeads.size()<1){
//            activeHeads = new ArrayList<>();
//            activeHeads.addAll(employeeRegionRepository.getRegionHeadForRegion(project.getRegion()).stream().map(email -> employeeService.findEmployeeByEmail(email)).collect(Collectors.toList()));
//        }
        if (environment.equals("production")) {
            activeHeads.addAll(employeeRegionRepository.getRegionHeadForRegion(project.getRegionvalue()).stream().map(email -> employeeService.findEmployeeByEmail(email)).collect(Collectors.toList()));
        }

        activeHeads.addAll(ccEmails.stream().map(email -> employeeService.findEmployeeByEmail(email)).collect(Collectors.toList()));
        if (!StringUtils.isEmpty(loggedInUser)) {
            Employee employee = employeeService.findEmployeeByEmail(loggedInUser);
            if (employee != null)
                activeHeads.add(employee);
        }
        return activeHeads;
    }

    @Override
    public Map<String, Integer> countUpcomingDeallocations(int days, String competency, ArrayList<String> regions) {
        List<EStaffing> eStaffings = staffingRepository.findByStateAndDateAfterAndDateBefore(Staffing.State.Allocated, DateTime.now().minusDays(1).toDate(), DateTime.now().plusDays(days).toDate());
        Map<String, Integer> competencyList = new HashMap<>();
        if (regions != null && !regions.isEmpty()) {
            List<EStaffing> eStaffingsRegionFilter = new ArrayList<>();
            eStaffings.forEach(eStaffing -> regions.forEach(region -> {
                if (eStaffing.getProject().getRegionvalue().getRegionName().equalsIgnoreCase(region))
                    eStaffingsRegionFilter.add(eStaffing);
            }));
            eStaffings = eStaffingsRegionFilter;
        }
        for (EStaffing eStaffing : eStaffings) {
            if (eStaffing.getStaffingRequest().getState().equals(StaffRequest.State.Closed) &&
                    eStaffing.getStaffingRequest().getActive()) {
                Integer count = competencyList.get(eStaffing.getStaffingRequest().getCompetency());
                if (count == null)
                    competencyList.put(eStaffing.getStaffingRequest().getCompetency(), 1);
                else
                    competencyList.put(eStaffing.getStaffingRequest().getCompetency(), count + 1);
            }
        }
        return competencyList;
    }

    @Override
    public Set<UpcomingDeallocationDTO> getUpcomingDeallocations(int days, ArrayList<String> regions, String competency) {
        List<EStaffing> eStaffings = staffingRepository.findByStateAndDateAfterAndDateBefore(Staffing.State.Allocated, DateTime.now().minusDays(1).toDate(), DateTime.now().plusDays(days).toDate());
        Set<UpcomingDeallocationDTO> upcomingDeallocationList = new TreeSet<>();

        if (regions != null && !regions.isEmpty()) {
            List<EStaffing> eStaffingsRegionFilter = new ArrayList<>();
            eStaffings.forEach(eStaffing -> regions.forEach(region -> {
                if (eStaffing.getProject().getRegionvalue().getRegionName().equalsIgnoreCase(region))
                    eStaffingsRegionFilter.add(eStaffing);
            }));
            eStaffings = eStaffingsRegionFilter;
        }

        if (!StringUtils.isEmpty(competency)) {
            List<EStaffing> eStaffingsCompetencyFilter = new ArrayList<>();
            eStaffings.forEach(eStaffing -> {
                if (eStaffing.getStaffingRequest().getCompetency().equalsIgnoreCase(competency)) {
                    eStaffingsCompetencyFilter.add(eStaffing);
                }
            });
            eStaffings = eStaffingsCompetencyFilter;
        }


        for (EStaffing eStaffing : eStaffings) {
            if (eStaffing.getStaffingRequest().getState().equals(StaffRequest.State.Closed) &&
                    eStaffing.getStaffingRequest().getActive()) {
                String employeeCompetency = employeeService.findEmployeeByEmail(eStaffing.getEmail()).getCompetency().getName();
                UpcomingDeallocationDTO dto = new UpcomingDeallocationDTO();
                dto.setProjectName(eStaffing.getProject().getProjectName());
                dto.setTitle(eStaffing.getStaffingRequest().getTitle());
                dto.setEmployeeName(eStaffing.getEmployeeName());
                dto.setDate(eStaffing.getStaffingRequest().getEndDate());
                dto.setProjectId(eStaffing.getProject().getProjectId().toString());
                dto.setBillableType(eStaffing.getStaffingRequest().getBillableType().toString());
                dto.setRegion(eStaffing.getProject().getRegionvalue().getRegionName());
                dto.setRegionId(eStaffing.getProject().getRegionvalue().getId());
                dto.setCompetency(employeeCompetency);
                dto.setCode(employeeService.findEmployeeByEmail(eStaffing.getEmail()).getCode());
                upcomingDeallocationList.add(dto);
            }
        }
        return upcomingDeallocationList;
    }

    @Override
    public Set<AllocationDetails> getAllocationForEmail(String email) {
        List<EStaffing> staffingList = staffingRepository.findByEmail(email);
        Map<Long, AllocationDetails> allocationDetailsMap = new HashMap<>();
        staffingList.forEach(eStaffing -> {
            if (eStaffing.getState().equals(Staffing.State.Allocated) || eStaffing.getState().equals(Staffing.State.DeAllocated)) {
                if (allocationDetailsMap.get(eStaffing.getStaffingRequest().getId()) == null) {
                    AllocationDetails allocationDetails = new AllocationDetails();
                    allocationDetails.setProjectName(eStaffing.getProject().getProjectName());
                    allocationDetails.setAllocationPercentage(eStaffing.getStaffingRequest().getAllocationPercentage());
                    allocationDetails.setCompetency(eStaffing.getStaffingRequest().getCompetency());
                    allocationDetails.setRegion(eStaffing.getProject().getRegionvalue().getRegionName());
                    allocationDetails.setTitle(eStaffing.getStaffingRequest().getTitle());
                    if (eStaffing.getState().equals(Staffing.State.Allocated)) {
                        allocationDetails.setStartDate(eStaffing.getDate());
                    } else if (eStaffing.getState().equals(Staffing.State.DeAllocated)) {
                        allocationDetails.setEndDate(eStaffing.getDate());
                    }
                    allocationDetailsMap.put(eStaffing.getStaffingRequest().getId(), allocationDetails);
                } else {
                    AllocationDetails allocationDetails = allocationDetailsMap.get(eStaffing.getStaffingRequest().getId());
                    if (eStaffing.getState().equals(Staffing.State.Allocated)) {
                        allocationDetails.setStartDate(eStaffing.getDate());
                    } else if (eStaffing.getState().equals(Staffing.State.DeAllocated)) {
                        allocationDetails.setEndDate(eStaffing.getDate());
                    }
                    allocationDetailsMap.put(eStaffing.getStaffingRequest().getId(), allocationDetails);
                }
            }
        });

        Set<AllocationDetails> allocationDetailsSet = new TreeSet<>();
        allocationDetailsMap.values().forEach(allocationDetailsSet::add);

        return allocationDetailsSet;
    }

    @Override
    public Set<AllocationDetails> getAllocationForEmployeeOnBench(Employee employee) {

        List<EStaffing> staffingList = staffingRepository.findByEmail(employee.getEmailAddress());
        Map<Long, AllocationDetails> allocationDetailsMap = new HashMap<>();
        staffingList.forEach(eStaffing -> {
            if ((allocationDetailsMap.get(eStaffing.getStaffingRequest().getId()) == null) && (eStaffing.getStaffingRequest().getActive()) && (eStaffing.getState().equals(Staffing.State.Allocated))) {
                if (!(employee.getAdditionalInfo().getTotalBillableAllocation() == 0 &&
                        employee.getAdditionalInfo().getTotalNonBillableAllocation() == 0)) {

                    AllocationDetails allocationDetails = createAllocationDetails(eStaffing);
                    allocationDetailsMap.put(eStaffing.getStaffingRequest().getId(), allocationDetails);
                }
            }
        });
        Set<AllocationDetails> allocationDetailsSet = new TreeSet<>();
        allocationDetailsSet.addAll(allocationDetailsMap.values());
        return allocationDetailsSet;
    }

    @Override
    public Set<AllocationDetails> getAllocationForEmployeeReport(Employee employee) {
        List<EStaffing> staffingList = staffingRepository.findByEmail(employee.getEmailAddress());
        Map<Long, AllocationDetails> allocationDetailsMap = new HashMap<>();
        staffingList.forEach(eStaffing -> {
            if ((allocationDetailsMap.get(eStaffing.getStaffingRequest().getId()) == null) && (!eStaffing.getState().equals(Staffing.State.Nominated)) && (eStaffing.getState().equals(Staffing.State.Allocated))) {
                AllocationDetails allocationDetails = createAllocationDetails(eStaffing);
                allocationDetailsMap.put(eStaffing.getStaffingRequest().getId(), allocationDetails);
            }

        });
        Set<AllocationDetails> allocationDetailsSet = new TreeSet<>();
        allocationDetailsSet.addAll(allocationDetailsMap.values());
        return allocationDetailsSet;
    }

    private AllocationDetails createAllocationDetails(EStaffing eStaffing) {
        AllocationDetails allocationDetails = new AllocationDetails(eStaffing.getProject().getProjectName(), eStaffing.getStaffingRequest().getAllocationPercentage(), eStaffing.getStaffingRequest().getCompetency(), eStaffing.getEmail(), eStaffing.getStaffingRequest().getTitle(), eStaffing.getProject().getRegionvalue().getRegionName());
        LocalDate startDate = new java.sql.Date(eStaffing.getStaffingRequest().getStartDate().getTime()).toLocalDate();
        if (eStaffing.getDate() != null) {
            startDate = startDate.isAfter(eStaffing.getDate()) ? startDate : eStaffing.getDate();
        }
        allocationDetails.setBillableType(eStaffing.getStaffingRequest().getBillableType().getBillableType());
        allocationDetails.setStartDate(startDate);
        allocationDetails.setEndDate(new java.sql.Date(eStaffing.getStaffingRequest().getEndDate().getTime()).toLocalDate());
        if(eStaffing.getStaffingRequest() !=null && eStaffing.getStaffingRequest().getProject()!=null && eStaffing.getStaffingRequest().getProject().getClient()!=null){
            allocationDetails.setClientName(eStaffing.getStaffingRequest().getProject().getClient().getClientName());
        }
        return allocationDetails;

    }

    @Override
    public List<StaffRequest> getDeallocateSummary(long projectId) {
        LOGGER.debug("getDeallocateSummary Method Begins for Project ID =" + projectId);
        EProject eProject = projectRepository.findOne(projectId);
        Date date = (DateUtil.minusDays(new Date(), Integer.parseInt(env.getProperty("recent.deallocation.days"))));
        List<EProjectStaffingRequest> staffingRequests = staffRequestRepository.findAllByProjectAndIsReallocatedAndEndDateBetween(eProject,false, date, new Date());
        List<StaffRequest> staffRequests = new ArrayList<>();
        for (EProjectStaffingRequest staffingRequest : staffingRequests) {
            EStaffing eStaffing = staffingRepository.findTopByStaffingRequestAndStateOrderByIdDesc(staffingRequest, Staffing.State.DeAllocated);
            if (eStaffing == null && staffingRequests.size() > 0) {
                staffRequests = staffRequests.stream().filter(staffRequest -> filterReallocatedEmployees(staffRequest, staffingRequest)).collect(Collectors.toList());
            } else {
                StaffRequest request = beanMapper.map(staffingRequest, StaffRequest.class);
                Staffing staffing = beanMapper.map(eStaffing, Staffing.class);
                request.setAllocatedStaffing(staffing);
                staffRequests = addDeallocatedEmployee(staffRequests, request);
            }


        }
        LOGGER.debug("getDeallocateSummary Method Ends. Total Deallocated Employees in last 10 days" + staffRequests.size());
        return staffRequests;
    }

    private boolean filterReallocatedEmployees(StaffRequest staffRequest, EProjectStaffingRequest staffingRequest) {
        boolean request = true;
        EStaffing latestStaffing = staffingRepository.findTopByStaffingRequestAndStateOrderByIdDesc(staffingRequest, Staffing.State.Allocated);
        if (staffRequest != null && latestStaffing != null) {
            EProjectStaffingRequest projectStaffingRequest = staffRequestRepository.findOne(staffRequest.getId());
            List<EStaffing> staffing = staffingRepository.findByStaffingRequestAndState(projectStaffingRequest, Staffing.State.DeAllocated);
            request = !staffing.stream().findFirst().get().getEmail().equals(latestStaffing.getEmail());
        }
        return request;
    }

    private List<StaffRequest> addDeallocatedEmployee(List<StaffRequest> staffRequests, StaffRequest request) {
        List<StaffRequest> tempStaffRequests = new ArrayList<>();
        boolean addNewEntry = true;
        for (StaffRequest projectStaffingRequest : staffRequests) {
            boolean predicate = true;
            if (projectStaffingRequest.getAllocatedStaffing().getEmail().equals(request.getAllocatedStaffing().getEmail())) {

                predicate = projectStaffingRequest.getEndDate().after(request.getEndDate());
                addNewEntry = false;
            }
            if (!predicate) {
                addNewEntry = true;
            } else {
                tempStaffRequests.add(projectStaffingRequest);
            }


        }
        if (addNewEntry) {
            tempStaffRequests.add(request);
        }
        return tempStaffRequests;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void reAllocate(StaffRequest staffRequest, String loggedInUser) {
        LOGGER.debug("reAllocate Method Begins");
        LOGGER.debug("reAllocate User:" + staffRequest.getAllocatedStaffing().getEmail() + "reAllocate EndDate:" + staffRequest.getEndDate() + "reAllocate Billable:" + staffRequest.getBillableType() + "reAllocate Allocation percentage:" + staffRequest.getAllocationPercentage() + "reAllocated On:" + new Date() + "reAllocated By" + loggedInUser);

        try {
            System.out.println(staffRequest.getId());
            Employee employee = employeeService.findEmployeeByEmail(staffRequest.getAllocatedStaffing().getEmail());
            EProjectStaffingRequest projectStaffingRequest = beanMapper.map(staffRequest, EProjectStaffingRequest.class);
            EProjectStaffingRequest updateStaffingRequest = staffRequestRepository.findOne(staffRequest.getId());
            if (projectStaffingRequest.getEndDate().before(updateStaffingRequest.getEndDate()))
                throw new BusinessValidationFailureException("End Date is less than previous request end date ");
            updateStaffingRequest.setActive(true);
            projectStaffingRequest.setActive(true);
            projectStaffingRequest.setProject(updateStaffingRequest.getProject());
            staffRequestRepository.saveAndFlush(updateStaffingRequest);
            EProjectStaffingRequest newstaffingRequest = createEProjectStaffingRequest(projectStaffingRequest, updateStaffingRequest);
            staffRequestRepository.saveAndFlush(newstaffingRequest);
            System.out.println(newstaffingRequest.getId());
            DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
            EProject dbProject = findProjectEntity(newstaffingRequest.getProject().getProjectId());
            publishMailingEvent(employee, dbProject, MailType.Allocated, loggedInUser, outputFormatter.format(newstaffingRequest.getStartDate()), outputFormatter.format(newstaffingRequest.getEndDate()));
            ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO(employee.getEmailAddress());
            projectDetailsDTO.setProjectName(dbProject.getProjectName());
            projectDetailsDTO.setProjectId(dbProject.getProjectId());
            projectDetailsDTO.setAllocationPercentage(updateStaffingRequest.getAllocationPercentage());
            if (updateStaffingRequest.getState().equals(StaffRequest.State.Closed))
                projectDetailsDTO.setAllocated(true);
            try {
                employeeOperations.pushProjectsInfo(projectDetailsDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


        } catch (CloneNotSupportedException e) {
            LOGGER.debug(e.getMessage());
        }
        LOGGER.debug("reAllocate Method Ends");
    }

}