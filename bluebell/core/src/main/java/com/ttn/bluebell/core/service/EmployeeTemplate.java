package com.ttn.bluebell.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.api.EmployeeReviewerFeedbackOperations;
import com.ttn.bluebell.core.exception.EmployeeNotFoundException;
import com.ttn.bluebell.domain.employee.*;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.region.Region;
import com.ttn.bluebell.domain.staffing.EStaffing;
import com.ttn.bluebell.durable.model.constant.RabbitMqQueueNameConstant;
import com.ttn.bluebell.durable.model.employee.*;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.staffing.Staffing;
import com.ttn.bluebell.durable.enums.BillableType;
import com.ttn.bluebell.integration.api.SuccessFactorOperations;
import com.ttn.bluebell.repository.*;
import com.ttn.bluebell.repository.config.EmployeeRoleRepository;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.collections.map.HashedMap;
import org.dozer.DozerBeanMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by praveshsaini on 4/10/16.
 */
@Service
@PropertySource("classpath:application.properties")
public class EmployeeTemplate implements EmployeeOperations {

    private static Cache cache = CacheManager.getInstance().getCache("employees");

    @Resource
    Environment env;

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    private EmployeeReviewerFeedbackOperations employeeReviewerFeedbackOperations;

    @Autowired
    private SuccessFactorOperations successFactor;

    @Autowired
    private EmployeeCompetencyRatingRepository employeeCompetencyRatingRepository;

    @Autowired
    private DozerBeanMapper beanMapper;

    @Autowired
    private StaffingRepository staffingRepository;

    @Autowired
    private EmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private EmployeeRegionRepository employeeRegionRepository;
    @Autowired
    private EmployeeMentovisorRepository employeeMentovisorRepository;
    @Autowired
    private RegionService regionService;
    @Autowired
    private RegionRepository regionRepository;
    @Value("#{'${employee.functions}'.split(',')}")
    private List<String> employeeFunctions;
    @Autowired
    private EmployeeResignationRepository resignationRepository;

    private void dumpCache() {
        cache.removeAll();
    }

    private void prepareCache(Employee employee) {
        cache.putIfAbsent(new Element(employee.getEmailAddress(), employee));
    }

    private void prepareCache(Set<Employee> employees) {
        employees.forEach(this::prepareCache);
    }

    private Element checkCache(String email) {
        return cache.get(email);
    }

    public Map<String, Region> getAllEmployeesEmailWithRegion() {
        Map<String, Region> regionMap = new HashMap<>();
        Set<String> emails = new HashSet<>();
        cache.getKeys().forEach(email -> emails.add(email.toString()));
        emails.forEach(email -> {
            List<EEmployeeRegions> employeeRegionList = employeeRegionRepository.findByEmail(email);
            if (employeeRegionList.size() > 0) {
                EEmployeeRegions employeeRegions = employeeRegionList.stream().findFirst().get();
                regionMap.put(email, employeeRegions.getRegionvalue());
            }

        });
        return regionMap;
    }

    @Override
    @PostConstruct
    public void findAllEmployee() {
        Set<Employee> employeeSet = successFactor.findAllEmployee();
        associateReviewerFeedback(employeeSet);
        //associateCompetency(employeeSet, "competency");
        dumpCache();
        prepareCache(employeeSet);
        regionService.refreshRegionData();
        syncEmployeeRegionAndMentovisor(employeeSet);

        findAllEmployeeByCompetency(null, false, true);
    }

    @Override
    public Set<Employee> findAllDeactiveEmployee() {
        return successFactor.findAllDeactiveEmployee();
    }

    @Override
    public void updateEmployeeRegion(String employeeEmail, String employeeNewRegion) {
        List<EEmployeeRegions> eEmployeeRegions = employeeRegionRepository.findByEmail(employeeEmail);
        EEmployeeRegions eEmployeeRegion = eEmployeeRegions.stream().findFirst().get();
        eEmployeeRegion.setRegionvalue(regionService.findRegion(employeeNewRegion));
        eEmployeeRegion.setRegion(employeeNewRegion);
        employeeRegionRepository.save(eEmployeeRegions);
    }

    private void syncEmployeeRegionAndMentovisor(Set<Employee> employees) {
        employees.forEach(employee -> {
            List<EEmployeeRegions> employeeRegionList = employeeRegionRepository.findByEmail(employee.getEmailAddress());
            EEmployeeRegions employeeRegion;
            if (employeeRegionList.size() == 0) {
                employeeRegion = new EEmployeeRegions();
                employeeRegion.setEmail(employee.getEmailAddress());
                employeeRegion.setRegion(employee.getDepartmentOfEmployee());
                employeeRegion.setRegionvalue(regionService.findRegion(employee.getDepartmentOfEmployee()));
            } else {
                employeeRegion = employeeRegionList.stream().findFirst().get();
                if (employeeRegion.getRegionvalue() == null) {
                    employeeRegion.setRegionvalue(regionService.findRegion(employee.getDepartmentOfEmployee()));
                }
            }
            employeeRegionRepository.save(employeeRegion);
            syncMentovisors(employee);

        });

    }

    public void updateEmployee(EmployeeUpdateDTO employeeUpdateDTO) throws ParseException {
        Employee employee = findEmployeeByEmail(employeeUpdateDTO.getUsername());
        if (employee == null) {
            employee = new Employee();
        }
        if (employeeUpdateDTO.getRegion() != null) {
            employee.setDepartmentOfEmployee(employeeUpdateDTO.getRegion().getName());
            updateEmployeeRegion(employee.getEmailAddress(), employeeUpdateDTO.getRegion().getName());
        }
        if (employeeUpdateDTO.getPerformanceReviewer() != null || employeeUpdateDTO.getReportingManager() != null) {

            employee.setPerformanceReviewerEmail(employeeUpdateDTO.getPerformanceReviewer().getUsername());
            ChangeMentovisorDTO changeMentovisorDTO = new ChangeMentovisorDTO(Long.parseLong(employee.getCode()), employeeUpdateDTO.getReportingManager().getUsername(), employeeUpdateDTO.getPerformanceReviewer().getUsername());
            updateMentovisorwithoutUpdatingQueue(changeMentovisorDTO);
        }
        employee.setName((employeeUpdateDTO.getName() != null) ? employeeUpdateDTO.getName() : employee.getName());
        employee.setBusinessUnit((employeeUpdateDTO.getBusinessUnit() != null) ? employeeUpdateDTO.getBusinessUnit().getName() : employee.getBusinessUnit());
        employee.setCode((employeeUpdateDTO.getEmployeeId() != null) ? employeeUpdateDTO.getEmployeeId() : employee.getCode());
        if (employeeUpdateDTO.getCompetency() != null) {
            Competency competency = new Competency();
            competency.setName(employeeUpdateDTO.getCompetency().getName());
            employee.setCompetency(competency);
        }
        employee.setEmailAddress(employeeUpdateDTO.getUsername());
        employee.setFunction((employeeUpdateDTO.getFunction() != null) ? employeeUpdateDTO.getFunction().getName() : employee.getFunction());
        employee.setTitle((employeeUpdateDTO.getDesignation() != null) ? employeeUpdateDTO.getDesignation() : employee.getTitle());
        employee.setJoiningDate((employeeUpdateDTO.getDateOfJoining() != null) ? employeeUpdateDTO.getDateOfJoining() : employee.getJoiningDate());
        employee.setMentovisor((employeeUpdateDTO.getReportingManager() != null) ? employeeUpdateDTO.getReportingManager().getUsername() : employee.getMentovisor());
        employee.setImageUrl((employeeUpdateDTO.getProfilePicUrl() != null) ? employeeUpdateDTO.getProfilePicUrl() : employee.getImageUrl());
        cache.remove(employeeUpdateDTO.getUsername());
        Set<Employee> employeeSet = new HashSet<>();
        employeeSet.add(employee);
        associateReviewerFeedback(employeeSet);
        associateAdditionalInfo(employeeSet);
        prepareCache(employee);
    }

    private void syncMentovisors(Employee employee) {
        EMentovisor mentovisor = employeeMentovisorRepository.findByEmployeeEmail(employee.getEmailAddress());
        if (mentovisor == null) {
            mentovisor = new EMentovisor(Long.parseLong(employee.getCode()), employee.getName(), employee.getEmailAddress(), employee.getMentovisor(), employee.getMentovisor());
            if (employee.getMentovisor() != null)
                employeeMentovisorRepository.save(mentovisor);
        }

    }

    private void updateMentovisorwithoutUpdatingQueue(ChangeMentovisorDTO changeMentovisorDTO) {
        EMentovisor mentovisor = employeeMentovisorRepository.findById(changeMentovisorDTO.getEmpCode());
        if (mentovisor != null) {
            mentovisor.setMentovisorEmail(changeMentovisorDTO.getMentovisorEmail() != null ? changeMentovisorDTO.getMentovisorEmail() : mentovisor.getMentovisorEmail());
            mentovisor.setPerformanceReviewerEmail(changeMentovisorDTO.getPerformanceReviewerEmail() != null ? changeMentovisorDTO.getPerformanceReviewerEmail() : mentovisor.getPerformanceReviewerEmail());
            employeeMentovisorRepository.save(mentovisor);

        }

    }

    @Override
    public Set<Employee> findAllEmployeeByCompetency(String competency, boolean requireAll, Boolean isassociateAdditionalInfo) {
        Set<Employee> employeeSet = new HashSet<>();
        if (requireAll) {
            cache.getAll(cache.getKeys()).entrySet().forEach(entry -> {
                employeeSet.add((Employee) entry.getValue().getObjectValue());
            });
        } else {
            if (StringUtils.isEmpty(competency)) {
                cache.getAll(cache.getKeys()).entrySet().forEach(entry -> {
                    Employee employee = (Employee) entry.getValue().getObjectValue();
                    if (!StringUtils.isEmpty(employee.getFunction()) &&
                            !StringUtils.isEmpty(employee.getBusinessUnit()) &&
                            employee.getBusinessUnit().equalsIgnoreCase("Technology") &&
                            employeeFunctions.contains(employee.getFunction()))
                        employeeSet.add((Employee) entry.getValue().getObjectValue());
                });
            } else {
                cache.getAll(cache.getKeys()).entrySet().forEach(entry -> {
                    Employee employee = (Employee) entry.getValue().getObjectValue();
                    if (!StringUtils.isEmpty(employee.getFunction()) && !StringUtils.isEmpty(employee.getBusinessUnit()) && employee.getBusinessUnit().equalsIgnoreCase("Technology") && employeeFunctions.contains(employee.getFunction()))
                        if (employee.getCompetency() != null && employee.getCompetency().getName().equalsIgnoreCase(competency))
                            employeeSet.add(employee);
                });
            }
        }
        if (isassociateAdditionalInfo) {

            associateAdditionalInfo(employeeSet);
        }
        includeMentovisor(employeeSet);
        return employeeSet;
    }

    private void includeMentovisor(Set<Employee> employees) {
        employees.forEach(employee -> {
            if (employee.getMentovisor() != null) {
                employee.setMentovisorData(findEmployeeByEmail(employee.getMentovisor()));
            }
        });
    }

    public Employee findEmployeeByEmail(String email) {
        Element element = checkCache(email);
        if (element != null)
            return (Employee) element.getObjectValue();
        Employee employee = successFactor.findEmployeeByEmail(email);
        if (employee == null || StringUtils.isEmpty(employee.getEmailAddress())) {
            throw new EmployeeNotFoundException("exception.employee.notfound");
        }
        Set<Employee> employeeSet = new HashSet<>();
        employeeSet.add(employee);
        associateReviewerFeedback(employeeSet);
        associateAdditionalInfo(employeeSet);
        associateCompetency(employeeSet, "competency");
        prepareCache(employee);
        return employee;
    }

    public Employee hasExist(String email) {
        return findEmployeeByEmail(email);
    }

    private void associateCompetency(Set<Employee> employeeSet, String competency) {
        //todo

        Set<String> empCodes = new HashSet<>();
        employeeSet.forEach(employee -> {
            String empCode = employee.getCode();
            if (empCode != null) {
                empCodes.add(empCode);
            }

        });

        Map<String, Competency> empCompMap = findEmployeesCompetency(empCodes);


        employeeSet.forEach(employee -> {
            employee.setCompetency(empCompMap.get(employee.getCode()));
        });
    }

     void associateAdditionalInfo(Set<Employee> employeeSet) {
        //todo
        for (Employee employee : employeeSet) {
            List<EStaffing> allocatedStaffings = staffingRepository.findByEmailAndStateAndStaffingRequestActiveIsTrue(employee.getEmailAddress(), Staffing.State.Allocated);
            List<EStaffing> deallocatedStaffings = staffingRepository.findByEmailAndStateAndStaffingRequestActiveIsTrue(employee.getEmailAddress(), Staffing.State.DeAllocated);
            Map<String, StaffRequest> currentBillableProjects = new HashedMap();
            Map<String, StaffRequest> currentNonBillableProjects = new HashedMap();
            Map<String, StaffRequest> currentShadowProjects = new HashedMap();
            int billableAllocation = 0, nonBillableAllocation = 0, shadowAllocation = 0;
            for (EStaffing allocatedStaffing : allocatedStaffings) {
                boolean present = false;
                for (EStaffing deallocatedStaffing : deallocatedStaffings) {
                    if (allocatedStaffing.getStaffingRequest().getId().longValue() == deallocatedStaffing.getStaffingRequest().getId().longValue() &&
                            allocatedStaffing.getId().longValue() < deallocatedStaffing.getId().longValue() &&
                            allocatedStaffing.getEmail().equalsIgnoreCase(deallocatedStaffing.getEmail()))
                        present = true;
                }
                if (!present) {
                    EProject project = allocatedStaffing.getProject();
                    StaffRequest staffRequest = beanMapper.map(allocatedStaffing.getStaffingRequest(), StaffRequest.class);
                    staffRequest.setProjectId(allocatedStaffing.getStaffingRequest().getProject().getProjectId());
                    if (project.getProjectType().equals(Project.Type.Billable) && staffRequest.getBillableType().equals(BillableType.BILLABLE)) {
                        currentBillableProjects.put(project.getProjectName(), staffRequest);
                        billableAllocation += allocatedStaffing.getStaffingRequest().getAllocationPercentage();
                    } else if(staffRequest.getBillableType().equals(BillableType.NON_BILLABLE)){
                        currentNonBillableProjects.put(project.getProjectName(), staffRequest);
                        nonBillableAllocation += allocatedStaffing.getStaffingRequest().getAllocationPercentage();
                    }
                    else if(staffRequest.getBillableType().equals(BillableType.SHADOW)){
                        currentShadowProjects.put(project.getProjectName(),staffRequest);
                        shadowAllocation += allocatedStaffing.getStaffingRequest().getAllocationPercentage();
                    }
                }
            }

            Employee.AdditionalInfo additionalInfo = new Employee.AdditionalInfo();
            additionalInfo.setCurrentBillableProjects(currentBillableProjects);
            additionalInfo.setCurrentNonBillableProjects(currentNonBillableProjects);
            additionalInfo.setCurrentShadowProjects(currentShadowProjects);
            additionalInfo.setTotalBillableAllocation(billableAllocation);
            additionalInfo.setTotalNonBillableAllocation(nonBillableAllocation);
            additionalInfo.setTotalShadowAllocation(shadowAllocation);
            additionalInfo.setPreviousProjects(getEmployeeAllPreviousProjects(employee.getEmailAddress()));
            employee.setAdditionalInfo(additionalInfo);
        }
    }

    @Override
    public Set<String> getEmployeeAllPreviousProjects(String employeeEmail) {
        List<EStaffing> eStaffings = staffingRepository.findByEmail(employeeEmail);
        Set<String> projectsName = new HashSet<>();
        eStaffings.forEach(eStaffing -> {
            if (eStaffing.getState().equals(Staffing.State.DeAllocated) || (!eStaffing.getStaffingRequest().getActive())) {
                projectsName.add(eStaffing.getProject().getProjectName());
            }
        });
        return projectsName;
    }

    @Override
    public List<NonBillableEmployeeDTO> getAllNonBillableEmployees() {
        Set<Employee> employeeList = findAllEmployeeByCompetency(null, false, false);
        List<NonBillableEmployeeDTO> nonBillableEmployeeDTO = new ArrayList<>();

        employeeList.forEach(employee -> {
            List<EStaffing> staffings = staffingRepository.findByEmail(employee.getEmailAddress());
            List<EStaffing> filteredStaffing = staffings.stream().filter(staffing -> ((staffing.getState() == Staffing.State.Allocated) && (staffing.getStaffingRequest().getActive()) && (staffing.getStaffingRequest().getEndDate().after(new Date())) && (!staffing.getStaffingRequest().getBillableType().equals(BillableType.BILLABLE)))).collect(Collectors.toList());

            if (filteredStaffing.size() > 0) {
                List<String> projectName = new ArrayList<>();
                filteredStaffing.forEach(eStaffing -> {
                    projectName.add(eStaffing.getProject().getProjectName());
                });

                NonBillableEmployeeDTO nonBillableEmployee = new NonBillableEmployeeDTO(employee, projectName);
                nonBillableEmployeeDTO.add(nonBillableEmployee);
            }
        });
        return nonBillableEmployeeDTO;
    }

    private void associateReviewerFeedback(Set<Employee> employeeSet) {
        Set<String> emailAddresses = employeeSet.stream().map(employee -> employee.getEmailAddress()).collect(Collectors.toSet());
        /*if(env.getProperty("employee.reviewer.feedback").equalsIgnoreCase("true"))
            setDummyReviewerFeedback(emailAddresses);*/
        Map<String, List<ReviewerFeedback>> reviewerFeedback = employeeReviewerFeedbackOperations.findReviewersRating(emailAddresses);
        employeeSet.stream().forEach(
                employee -> employee.setReviewerFeedback(reviewerFeedback.get(employee.getEmailAddress()))
        );
    }

    public Map<String, Competency> findEmployeesCompetency(Set<String> employeeCodes) {
        Set<EEmployeCompetencyRating> competencyRatings = employeeCompetencyRatingRepository.findByCodeIn(employeeCodes);
        Map<String, Competency> empCompMap = new HashMap<>();
        competencyRatings.stream().forEach(employeCompetencyRating -> {
            Competency competency = beanMapper.map(employeCompetencyRating.getCompetency(), Competency.class);
            empCompMap.put(employeCompetencyRating.getCode(), competency);
        });
        return empCompMap;
    }

    public Set<String> findEmployeeRoles(String email) {
        List<EEmployeeRoles> rolesList = employeeRoleRepository.findByEmail(email);
        return rolesList.stream().map(role -> role.getRole()).collect(Collectors.toSet());
    }

    @Override
    public List<EmployeeAuthorityDTO> findEmployeesAuthority() {
        List<EEmployeeRegions> empRegions = employeeRegionRepository.findAll();
        List<EEmployeeRoles> employeeRoles = employeeRoleRepository.findAll();

        List<EmployeeAuthorityDTO> authorityDTOList = new ArrayList<>();

        Map<String, String> regionMap = empRegions.stream().collect(Collectors.toMap(EEmployeeRegions::getEmail,
                r -> r.getRegionvalue() != null ? r.getRegionvalue().getRegionName() : "", (r1, r2) -> r1 + "," + r2));

        Map<String, String> roleMap = employeeRoles.stream().collect(Collectors.toMap(r -> r.getEmail(),
                r -> r.getRole(), (r1, r2) -> r1 + "," + r2));

        Set<String> emails = new HashSet<>();
        emails.addAll(regionMap.keySet());
        emails.addAll(roleMap.keySet());

        for (String email : emails) {
            EmployeeAuthorityDTO authorityDTO = new EmployeeAuthorityDTO();
            authorityDTO.setEmail(email);
            if (regionMap.containsKey(email)) {
                authorityDTO.setRegions(StringUtils.commaDelimitedListToSet(regionMap.get(email)));
            }

            if (roleMap.containsKey(email)) {
                authorityDTO.setRoles(StringUtils.commaDelimitedListToSet(roleMap.get(email)));
                authorityDTO.setEmployee(findEmployeeByEmail(email));
                authorityDTOList.add(authorityDTO);
            }
        }
        return authorityDTOList;
    }

    @Override
    public Set<Employee> findEmployeesOnBench() {
        Set<Employee> allEmployees = findAllEmployeeByCompetency(null, false, false);
        Set<Employee> employeesOnBench = new HashSet<>();
        for (Employee employee : allEmployees) {
            /*EStaffing eStaffing = staffingRepository.findTopByEmailOrderByIdDesc(employee.getEmailAddress());
            if(eStaffing==null || !eStaffing.getState().name().equalsIgnoreCase(Staffing.State.Allocated.name())) *//*|| eStaffing.getDate().isBefore(LocalDate.now()))*//*
                employeesOnBench.add(employee);*/
            if (employee.getAdditionalInfo().getTotalBillableAllocation() == 0 &&
                    employee.getAdditionalInfo().getTotalNonBillableAllocation() == 0) {
                employeesOnBench.add(employee);
            }
        }
        return employeesOnBench;
    }

    @Override
    public Set<String> findEmployeeRegions(String email) {
        Set<String> regions = new HashSet<>();
        List<EEmployeeRegions> regionList = employeeRegionRepository.findAllByEmail(email);
        for (EEmployeeRegions eEmployeeRegion : regionList)
            regions.add(eEmployeeRegion.getRegion());
        return regions;
    }

    @Override
    @Transactional
    public void addEmployeeAuthority(EmployeeAuthorityDTO employeeAuthorityDTO) {
        addEmployeeRoles(employeeAuthorityDTO.getEmail(), employeeAuthorityDTO.getRoles());
        addEmployeeRegions(employeeAuthorityDTO.getEmail(), employeeAuthorityDTO.getRegions());
    }

    private void addEmployeeRegions(String email, Set<String> regions) {

        if (regions == null)
            return;
        employeeRegionRepository.deleteByEmail(email);
        List<EEmployeeRegions> regionsList = new ArrayList<>();
        regions.stream().forEach(region -> {
            EEmployeeRegions employeeRegions = new EEmployeeRegions();
            employeeRegions.setRegion(region);
            employeeRegions.setEmail(email);
            employeeRegions.setRegionvalue(regionService.findRegion(region));
            regionsList.add(employeeRegions);
        });


        employeeRegionRepository.save(regionsList);
        employeeRegionRepository.flush();

    }

    @Transactional
    void addEmployeeRoles(String email, Set<String> roles) {

        if (roles == null)
            return;
        roles = roles.stream().filter(r -> Role.getRole(r) != null).
                collect(Collectors.toSet());
        employeeRoleRepository.deleteByEmail(email);
        for (String role : roles) {
            EEmployeeRoles eEmployeeRoles = new EEmployeeRoles();
            eEmployeeRoles.setEmail(email);
            eEmployeeRoles.setRole(role);
            employeeRoleRepository.saveAndFlush(eEmployeeRoles);
        }
    }

    private void setDummyReviewerFeedback(Set<String> emailAddresses) {
        Random randomGenerator = new Random();
        List<String> ratings = new ArrayList<>();
        ratings.add("GREAT");
        ratings.add("AVERAGE");
        ratings.add("POOR");
        String currentCycle = "Sep16-Mar17";
        Map<String, List<ReviewerFeedback>> reviewerFeedback = employeeReviewerFeedbackOperations.findReviewersRating(emailAddresses);
        emailAddresses.stream().forEach(
                emailAddress -> {
                    if (reviewerFeedback.get(emailAddress).isEmpty()) {
                        ERating eRating = new ERating();
                        eRating.setCycle(currentCycle);
                        eRating.setValue(ratings.get(randomGenerator.nextInt(ratings.size())));
                        EEmployeeReviewerFeedback eEmployeeReviewerFeedback = new EEmployeeReviewerFeedback();
                        eEmployeeReviewerFeedback.setEmail(emailAddress);
                        eEmployeeReviewerFeedback.setRating(eRating);
                        employeeReviewerFeedbackOperations.save(eEmployeeReviewerFeedback);
                    }
                }
        );
    }

    @Override
    public Set<Employee> findAllEmployeeByCompetencyTitleAndSearch(EmployeeCompetencyTitleListDTO employeeCompetencyTitleListDTO) {
        Set<String> competencies = employeeCompetencyTitleListDTO.getCompetencies();
        Set<String> titles = employeeCompetencyTitleListDTO.getTitles();
        Boolean includeResigned = employeeCompetencyTitleListDTO.getIncludeResigned();
        String search = employeeCompetencyTitleListDTO.getSearch();
        Set<Employee> employeeSet = findAllEmployeeByCompetency(null, false, false);
        Set<Employee> employeeSetResult = new HashSet<>();
        String search1 = "(?i:.*" + search + ".*)";
        List<EmployeeResignation> employeeResignationList = resignationRepository.findAll();
        List<String> codeList = employeeResignationList.stream()
                .map(EmployeeResignation::getEmployeeCode)
                .collect(Collectors.toList());

        employeeSetResult.addAll(employeeSet.stream()
                .filter(employee ->
                        ((competencies.size() > 0 ? competencies.contains(employee.getCompetency().getName()) : true)
                                && (titles.size() > 0 ? titles.contains(employee.getTitle()) : true)
                                && (includeResigned ? true : !codeList.contains(employee.getCode()))
                                && (employeeCompetencyTitleListDTO.getBillableMax() >= employee.getAdditionalInfo().getTotalBillableAllocation()
                                || employeeCompetencyTitleListDTO.getBillableMax() == 100)
                                && (employeeCompetencyTitleListDTO.getBilliableMin() <= employee.getAdditionalInfo().getTotalBillableAllocation())
                                && (employeeCompetencyTitleListDTO.getNonBillableMax() >= employee.getAdditionalInfo().getTotalNonBillableAllocation()
                                || employeeCompetencyTitleListDTO.getNonBillableMax() == 100)
                                && (employeeCompetencyTitleListDTO.getNonBillableMin() <= employee.getAdditionalInfo().getTotalNonBillableAllocation())
                                && (employee.getName().matches(search1)
                                || employee.getEmailAddress().matches(search1)
                                || employee.getCode().matches(search1))))
                .collect(Collectors.toSet()));
        includeMentovisor(employeeSetResult);
        return employeeSetResult;

    }

    @Override
    public void getProjectsInfoForNW(Set<Employee> employees) {
        associateAdditionalInfo(employees);
    }

    public List<CompetencyBenchDTO> getAllEmployeesWithCompetencyBench() {
        Set<Employee> employeeSet = findEmployeesOnBench();
        Map<String, Integer> competencyBenchMap = new HashMap<>();
        List<CompetencyBenchDTO> competencyBenchList = new ArrayList<>();
        employeeSet.forEach(employee -> {
                    if (competencyBenchMap.containsKey(employee.getCompetency().getName())) {
                        competencyBenchMap.put(employee.getCompetency().getName(), competencyBenchMap.get(employee.getCompetency().getName()) + 1);
                    } else
                        competencyBenchMap.put(employee.getCompetency().getName(), 1);
                }
        );
        competencyBenchMap.forEach((k, v) -> {
            competencyBenchList.add(new CompetencyBenchDTO(k, v));
        });
        return competencyBenchList;

    }

    @Override
    public void pushAllEmployeesProjectsInfo() {
        Set<Employee> employeeSet = new HashSet<>();
        cache.getAll(cache.getKeys()).entrySet().forEach(entry -> {
            employeeSet.add((Employee) entry.getValue().getObjectValue());
        });
        getProjectsInfoForNW(employeeSet);

        employeeSet.forEach(employee->{
            Map<String,StaffRequest> billableProjects = employee.getAdditionalInfo().getCurrentBillableProjects();
            Map<String,StaffRequest> nonBillableProjects = employee.getAdditionalInfo().getCurrentNonBillableProjects();
            Map<String,StaffRequest> totalProjects = new HashMap<>();
            totalProjects.putAll(billableProjects);
            totalProjects.putAll(nonBillableProjects);
            totalProjects.forEach((k,v)->{
                if (v.getState().equals(StaffRequest.State.Closed)) {
                    ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO(employee.getEmailAddress());
                    projectDetailsDTO.setProjectId(v.getProjectId());
                    projectDetailsDTO.setProjectName(k);
                    projectDetailsDTO.setAllocationPercentage(v.getAllocationPercentage());
                    projectDetailsDTO.setAllocated(true);
                    try {
                        pushProjectsInfo(projectDetailsDTO);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });


        });
    }

    @Override
    public void pushProjectsInfo(ProjectDetailsDTO projectDetailsDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(projectDetailsDTO);
        amqpTemplate.convertAndSend(RabbitMqQueueNameConstant.PROJECT_INFO_QUEUE, jsonString);

    }

}