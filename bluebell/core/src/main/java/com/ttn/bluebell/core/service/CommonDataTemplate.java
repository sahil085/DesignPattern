package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.CommonDataOperations;
import com.ttn.bluebell.domain.common.ESystemParameter;
import com.ttn.bluebell.domain.employee.EMentovisor;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.region.Region;
import com.ttn.bluebell.domain.staffing.EStaffing;
import com.ttn.bluebell.durable.model.common.RegionDTO;
import com.ttn.bluebell.durable.model.common.SystemParameter;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.staffing.Staffing;
import com.ttn.bluebell.integration.api.SuccessFactorOperations;
import com.ttn.bluebell.repository.*;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by praveshsaini on 22/9/16.
 */
@Service
public class CommonDataTemplate implements CommonDataOperations {

    private static final Logger logger = LoggerFactory.getLogger(CommonDataTemplate.class);
    @Autowired
    private SuccessFactorOperations successFactorOperations;

    @Autowired
    private SystemParameterRepository systemParameterRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DozerBeanMapper beanMapper;
    @Autowired
    private ReportingService reportingService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private EmployeeTemplate employeeTemplate;
    @Autowired
    private EmployeeMentovisorRepository employeeMentovisorRepository;
    @Autowired
    private StaffingRepository staffingRepository;

    @PostConstruct
    void SetintimationDays() {
//        List<ESystemParameter> param = systemParameterRepository.findByType(SystemParameter.ParameterType.DEALLOCATION_INTIMATION_DAYS.name());
//        List<ESystemParameter> eSystemParameters = param.stream().parallel().filter(eSystemParameter -> eSystemParameter.getValue().equals("4")).map(this::updateSystemParameter).collect(Collectors.toList());
//        if (param.stream().parallel().noneMatch(eSystemParameter -> eSystemParameter.getValue().equals("1"))) {
//            ESystemParameter eSystemParameter = new ESystemParameter("", "1", SystemParameter.ParameterType.DEALLOCATION_INTIMATION_DAYS.name(), null);
//            eSystemParameters.add(eSystemParameter);
//        }
//        systemParameterRepository.save(eSystemParameters);
    }

    private ESystemParameter updateSystemParameter(ESystemParameter systemParameter) {
        systemParameter.setValue("7");
        return systemParameter;

    }

    @Override
    public List<String> findAllBusinessUnits() {
        return successFactorOperations.findAllBusinessUnits();
    }

    @Override
    public Set<String> findAllRegions() {
        return successFactorOperations.findAllRegions();
    }

    @Override
    public Set<String> fetchAllRegions() {
        logger.debug("New Region are fetch from successFactor");
        return successFactorOperations.fetchAllRegions().stream().map(RegionDTO::getName).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findAllActiveRegions() {
        Set<String> set = new HashSet<>();
        for (Region region : regionRepository.getAll()) {
            if (region != null) {
                if (region.getStatus() != null && region.getStatus()) {
                    String regionName = region.getRegionName();
                    set.add(regionName);
                }
            }
        }
        return set;
    }

    @Override
    public Set<String> findAllCompetencies() {
        return successFactorOperations.findAllCompetencies();
    }

    @Override
    public List<String> findAllEmployeeTitles() {
        return successFactorOperations.findAllEmployeeTitles();
    }

    @Override
    public List<SystemParameter> findEmployeeExperience(String type) {
        List<ESystemParameter> parameterList = systemParameterRepository.findByType(type);
        List<SystemParameter> systemParameters = new ArrayList<>();
        beanMapper.map(parameterList, systemParameters);
        return systemParameters;
    }

    @Override
    public List<String> findProjectList(String type, ArrayList<String> regions) {
        List<EProject> projectList = new ArrayList<EProject>();
        if (type.equalsIgnoreCase("Non-Billable")) {
            projectList = projectRepository.findByProjectType(Project.Type.NonBillable);
        } else if (type.equalsIgnoreCase("Billable")) {
            projectList = projectRepository.findByProjectType(Project.Type.Billable);
        } else {
            projectList = projectRepository.findAll();
        }
        List<String> projects = new ArrayList<>();
        projectList.forEach(eProject -> {
            if (regions == null || regions.size() == 0 || regions.contains(eProject.getRegionvalue().getRegionName())) {
                projects.add(eProject.getProjectName());
            }
        });
        return projects;
    }

    @Override
    public Map<String, Map<String, Object>> employeeOnBench(int durationInDays) {
        List completeAllocationDataANdEmployeeInfo = reportingService.allocationSummary(LocalDate.now().plusDays(durationInDays), LocalDate.now().plusDays(durationInDays + 1));
        Date durationEndDate = java.sql.Date.valueOf(LocalDate.now().plusDays(durationInDays));
        Map<String, Map<String, Double>> employeeAllocationData = (Map<String, Map<String, Double>>) completeAllocationDataANdEmployeeInfo.get(0);
        Map<String, Employee> employeeList = (Map<String, Employee>) completeAllocationDataANdEmployeeInfo.get(1);
        Map<String, Map<String, Object>> bench = new HashMap<>();
//        staffings.stream().forEach(nom ->
//                System.out.println("Employee Name "+nom.getEmployeeName()+" Project Id "+nom.getProject().getProjectId()+" Project Name "+nom.getProject().getProjectName()));

        List<EStaffing> staffings = staffingRepository.findEStaffingByStaffingRequestStateAndState(StaffRequest.State.Open, Staffing.State.Nominated);
        List<EMentovisor> mentovisors = employeeMentovisorRepository.getAll();
        employeeAllocationData.forEach((key, empdata) -> {
            if (key.contains("@") && (empdata.get("Bench") > 0)) {
                String mentovisorEmail = mentovisors.stream().parallel().filter(eMentovisor -> eMentovisor.getEmployeeEmail().equals(key)).map(EMentovisor::getMentovisorEmail).findFirst().get();
                Employee employee = employeeTemplate.findEmployeeByEmail(mentovisorEmail);
                Map<String, Object> benchRecord = new HashMap<>();
                benchRecord.put("bench", empdata.get("Bench") * 100);
                benchRecord.put("employeeName", employeeList.get(key).getName());
                boolean flag[]={false};

                staffings.forEach(empName -> {
                    String employeeName=empName.getEmployeeName();
//                    System.out.println(" Employee Name "+employeeName+" Project Name "+empName.getProject().getProjectName());
                    if (employeeName.equals(employeeList.get(key).getName())) {
                        List<String> nominatedProjectNames = new ArrayList<>();
//                        System.out.println("Employee Name "+empName.getEmployeeName()+" Project Name "+empName.getProject().getProjectName());
                        staffings.forEach((nomProjectNames) -> {

                                    if (employeeName.equals(nomProjectNames.getEmployeeName())) {
                                        nominatedProjectNames.add(nomProjectNames.getProject().getProjectName());
                                        System.out.println(" Employee Name " + nomProjectNames.getEmployeeName() + " Project Name " + nomProjectNames.getProject().getProjectName()+" Project ID "+nomProjectNames.getProject().getProjectId());
                                        System.out.println(" ID "+nomProjectNames.getId());
                                        flag[0]=true;
                                    }
                                }
                        );
                        if(flag[0])
                        benchRecord.put("nominatedProjects",nominatedProjectNames);
                    }

                });
                benchRecord.put("code", employeeList.get(key).getCode());
                benchRecord.put("title", employeeList.get(key).getTitle());
                benchRecord.put("mentovisorName", employee.getName());
                benchRecord.put("mentovisorEmail", employee.getEmailAddress());
                benchRecord.put("competency", employeeList.get(key).getCompetency() != null ? employeeList.get(key).getCompetency().getName() : "");
                benchRecord.put("leavesCount", employeeList.get(key).getLeavesCount());
                Map<String, StaffRequest> currentProjectDetails = new HashMap<>();
                currentProjectDetails.putAll(employeeList.get(key).getAdditionalInfo().getCurrentBillableProjects());
                currentProjectDetails.putAll(employeeList.get(key).getAdditionalInfo().getCurrentNonBillableProjects());
                currentProjectDetails.putAll(employeeList.get(key).getAdditionalInfo().getCurrentShadowProjects());
                List<String> projectNames = new ArrayList<>();


                currentProjectDetails.forEach((projectkey, staffRequest) -> {
                    if (benchRecord.containsKey("currentProjects")) {
                        Date availableFrom = staffRequest.getEndDate().after((Date) benchRecord.get("availableFrom")) && staffRequest.getEndDate().after(new Date()) ? staffRequest.getEndDate() : (Date) benchRecord.get("availableFrom");
                        benchRecord.put("availableFrom", availableFrom);
                        projectNames.add(projectkey + " (" + staffRequest.getBillableType().getBillableType() + ")");
                    } else {
                        if (durationEndDate.after(staffRequest.getEndDate()) && staffRequest.getEndDate().after(new Date(new Date().getTime() - 1000 * 60 * 60 * 24)))
                            benchRecord.put("availableFrom", java.sql.Date.valueOf(staffRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1)));

                        projectNames.add(projectkey + " (" + staffRequest.getBillableType().getBillableType() + ")");
                    }
                });
                benchRecord.put("currentProjects", projectNames);

                bench.put(key, benchRecord);

                Map<String, Object> nominatedMap = bench.get(key);


//                staffings.forEach(eStaffing ->
//                {
//                    List<String> list = (List<String>) nominatedMap.get("employeeName");
//                    if (list.contains(eStaffing.getEmployeeName())) {
//
//                        staffings.forEach(nomProjectNames -> {
//                                    if (eStaffing.getEmployeeName().equals(nomProjectNames)) {
//                                        nominatedProjectNames.add(eStaffing.getProject().getProjectName());
//                                        System.out.println(" Employee Name "+nomProjectNames.getEmployeeName()+" Project Name "+nomProjectNames.getProject().getProjectName());
//
//                                    }
//                                }
//                        );
//
//                    }
//                });
            }


        });


        return bench;

    }

    @Override
    public void uploadReviewerData() {

    }
}
