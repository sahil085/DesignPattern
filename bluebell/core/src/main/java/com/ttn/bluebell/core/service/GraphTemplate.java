package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.CommonDataOperations;
import com.ttn.bluebell.core.api.GraphOperations;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.region.Region;
import com.ttn.bluebell.domain.staffing.EProjectStaffingRequest;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.event.notification.NotificationRequest;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.repository.ProjectRepository;
import com.ttn.bluebell.repository.StaffRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ttn on 8/11/16.
 */

@Service
public class GraphTemplate implements GraphOperations {
    private static final Logger logger = LoggerFactory.getLogger(GraphTemplate.class);

    @Autowired
    private StaffingTemplate staffingTemplate;

    @Autowired
    private EmployeeTemplate employeeTemplate;

    @Autowired
    private CommonDataOperations commonDataOperations;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StaffRequestRepository staffRequestRepository;

    private Set<String> allRegions = null;

    private void updateAllRegions() {
        allRegions = commonDataOperations.findAllActiveRegions();

    }

    public Map<String, Integer> upcomingDeallocations(String days, ArrayList<String> regions) {
        updateAllRegions();
        int noOfDays = 30;
        switch (days) {
            case "Weekly":
                noOfDays = 7;
                break;
            case "FortNight":
                noOfDays = 15;
                break;
        }
        return staffingTemplate.countUpcomingDeallocations(noOfDays, null, regions);
    }

    private Map<String, Map<String, Map<String, Integer>>> getGraphDetailsForRegionHead(String projectType, String graphType, List<String> filterList, List<String> regions) {
        updateAllRegions();
        Map<String, Map<String, Map<String, Integer>>> regionCompetencyBillable = new HashMap<>();
        if (graphType.equalsIgnoreCase("Open Requests")) {
            List<EProjectStaffingRequest> staffingRequests = staffRequestRepository.findByStateAndActiveIsTrue(StaffRequest.State.Open);
            if (projectType.equalsIgnoreCase("Billable")) {
                staffingRequests.forEach(request -> {
                    String region = request.getProject().getRegionvalue().getRegionName();
                    if (regions == null || regions.size() == 0 || regions.contains(region)) {
                        String competency = request.getCompetency();
                        String projectName = request.getProject().getProjectName();
                        if (filterList == null || filterList.size() == 0 || filterList.contains(projectName)) {
                            Map<String, Map<String, Integer>> competencyBillable = regionCompetencyBillable.get(competency);
                            if (competencyBillable == null)
                                competencyBillable = createProjectMap(regions);
                            Map<String, Integer> billable = competencyBillable.get(projectName);
                            if (billable == null)
                                billable = new HashMap<>();
                            Integer count;
                            if (request.getProject().getProjectType().name().equalsIgnoreCase("Billable")) {
                                count = billable.get("count");
                                if (count == null)
                                    count = 0;
                                billable.put("count", ++count);
                                competencyBillable.put(projectName, billable);
                                regionCompetencyBillable.put(competency, competencyBillable);
                            }
                        }
                    }
                });
            } else if (projectType.equalsIgnoreCase("Non-Billable")) {
                staffingRequests.forEach(request -> {
                    String competency = request.getCompetency();
                    String region = request.getProject().getRegionvalue().getRegionName();
                    String projectName = request.getProject().getProjectName();
                    if (regions == null || regions.size() == 0 || regions.contains(region)) {
                        if (filterList == null || filterList.size() == 0 || filterList.contains(projectName)) {
                            Map<String, Map<String, Integer>> competencyBillable = regionCompetencyBillable.get(competency);
                            if (competencyBillable == null)
                                competencyBillable = createProjectMap(regions);
                            Map<String, Integer> billable = competencyBillable.get(projectName);
                            if (billable == null)
                                billable = new HashMap<>();
                            Integer count;
                            if (request.getProject().getProjectType().name().equalsIgnoreCase("NonBillable")) {
                                count = billable.get("count");
                                if (count == null)
                                    count = 0;
                                billable.put("count", ++count);
                                competencyBillable.put(projectName, billable);
                                regionCompetencyBillable.put(competency, competencyBillable);
                            }
                        }
                    }
                });
            }
        } else if (graphType.equalsIgnoreCase("Allocations")) {
            Set<Employee> employees = employeeTemplate.findAllEmployeeByCompetency(null, false, false);
            if (projectType.equalsIgnoreCase("Billable")) {
                employees.forEach(employee -> {
                    if (employee.getAdditionalInfo() != null && employee.getAdditionalInfo().getCurrentBillableProjects() != null) {
                        employee.getAdditionalInfo().getCurrentBillableProjects().entrySet().forEach(entry -> {
                            String competency = entry.getValue().getCompetency();
                            String projectName = entry.getKey();
                            String region = projectRepository.findOneByProjectNameAndActive(entry.getKey(), true).getRegionvalue().getRegionName();
                            if (regions.contains(region)) {
                                if (filterList == null || filterList.size() == 0 || filterList.contains(projectName)) {
                                    Map<String, Map<String, Integer>> regionMap = regionCompetencyBillable.get(competency);
                                    if (regionMap == null)
                                        regionMap = createProjectMap(regions);
                                    Map<String, Integer> billable = regionMap.get(projectName);
                                    if (billable == null)
                                        billable = new HashMap<>();
                                    Integer count;
                                    count = billable.get("count");
                                    if (count == null)
                                        count = 0;
                                    billable.put("count", ++count);
                                    regionMap.put(projectName, billable);
                                    regionCompetencyBillable.put(competency, regionMap);
                                }
                            }
                        });
                    }
                });
            } else if (projectType.equalsIgnoreCase("Non-Billable")) {
                employees.forEach(employee -> {
                    if (employee.getAdditionalInfo() != null && employee.getAdditionalInfo().getCurrentNonBillableProjects() != null) {
                        employee.getAdditionalInfo().getCurrentNonBillableProjects().entrySet().forEach(entry -> {
                            String competency = entry.getValue().getCompetency();
                            String projectName = entry.getKey();
                            String region = projectRepository.findOneByProjectNameAndActive(entry.getKey(), true).getRegionvalue().getRegionName();
                            if (regions.contains(region)) {
                                if (filterList == null || filterList.size() == 0 || filterList.contains(projectName)) {
                                    Map<String, Map<String, Integer>> competencyBillable = regionCompetencyBillable.get(competency);
                                    if (competencyBillable == null)
                                        competencyBillable = createProjectMap(regions);
                                    Map<String, Integer> billable = competencyBillable.get(projectName);
                                    if (billable == null)
                                        billable = new HashMap<>();
                                    Integer count;
                                    count = billable.get("count");
                                    if (count == null)
                                        count = 0;
                                    billable.put("count", ++count);
                                    competencyBillable.put(projectName, billable);
                                    regionCompetencyBillable.put(competency, competencyBillable);
                                }
                            }
                        });
                    }
                });
            }
        }
        return regionCompetencyBillable;
    }

    public Map<String, Map<String, Map<String, Integer>>> regionCompetencyBillability(String projectType, String graphType, ArrayList<String> filterList, ArrayList<String> regions) {
        if (filterList != null && filterList.size() > 0) {
            filterList.remove("");
        }
        if (regions.size() > 0) {
            return getGraphDetailsForRegionHead(projectType, graphType, filterList, regions);
        } else {
            Map<String, Map<String, Map<String, Integer>>> regionCompetencyBillable = new HashMap<>();
            if (graphType.equalsIgnoreCase("Open Requests")) {
                List<EProjectStaffingRequest> staffingRequests = staffRequestRepository.findByStateAndActiveIsTrue(StaffRequest.State.Open);
                if (projectType.equalsIgnoreCase("Billable")) {
                    logger.debug("Generating Data for Open Requests :: Billable");
                    staffingRequests.forEach(request -> {

                        Region origRegion = request.getProject().getRegionvalue();
                        String region = origRegion.getRegionName();

                        if ((regions == null || regions.size() == 0 || regions.contains(region)) && origRegion.getStatus()) {
                            if (filterList == null || filterList.size() == 0 || filterList.contains(region)) {
                                String competency = request.getCompetency();
                                Map<String, Map<String, Integer>> competencyBillable = regionCompetencyBillable.get(competency);
                                if (competencyBillable == null)
                                    competencyBillable = createRegionMap();
                                Map<String, Integer> billable = competencyBillable.get(region);
                                if (billable == null)
                                    billable = new HashMap<>();
                                Integer count;
                                if (request.getProject().getProjectType().name().equalsIgnoreCase("Billable")) {
                                    count = billable.get("count");
                                    if (count == null)
                                        count = 0;
                                    billable.put("count", ++count);
                                    competencyBillable.put(region, billable);
                                    regionCompetencyBillable.put(competency, competencyBillable);
                                }
                            }
                        }
                    });
                } else if (projectType.equalsIgnoreCase("Non-Billable")) {
                    logger.debug("Generating Data for Open Requests :: Non-Billable");
                    staffingRequests.forEach(request -> {
                        String competency = request.getCompetency();

                        Region origRegion = request.getProject().getRegionvalue();
                        String region = origRegion.getRegionName();
                        if ((regions == null || regions.size() == 0 || regions.contains(region)) && origRegion.getStatus()) {
                            if (filterList == null || filterList.size() == 0 || filterList.contains(region)) {
                                Map<String, Map<String, Integer>> competencyBillable = regionCompetencyBillable.get(competency);
                                if (competencyBillable == null)
                                    competencyBillable = createRegionMap();
                                Map<String, Integer> billable = competencyBillable.get(region);
                                if (billable == null)
                                    billable = new HashMap<>();
                                Integer count;
                                if (request.getProject().getProjectType().name().equalsIgnoreCase("NonBillable")) {
                                    count = billable.get("count");
                                    if (count == null)
                                        count = 0;
                                    billable.put("count", ++count);
                                    competencyBillable.put(region, billable);
                                    regionCompetencyBillable.put(competency, competencyBillable);
                                }
                            }
                        }
                    });
                }
            } else if (graphType.equalsIgnoreCase("Allocations")) {
                Set<Employee> employees = employeeTemplate.findAllEmployeeByCompetency(null, false, false);
                if (projectType.equalsIgnoreCase("Billable")) {
                    logger.debug("Generating Data for Allocations :: Billable");
                    employees.forEach(employee -> {

                        if (employee.getAdditionalInfo() != null && employee.getAdditionalInfo().getCurrentBillableProjects() != null) {
                            employee.getAdditionalInfo().getCurrentBillableProjects().entrySet().forEach(entry -> {
                                String competency = entry.getValue().getCompetency();
                                String region = projectRepository.findOneByProjectNameAndActive(entry.getKey(), true).getRegionvalue().getRegionName();
                                if (regions == null || regions.size() == 0 || regions.contains(region)) {
                                    if (filterList == null || filterList.size() == 0 || filterList.contains(region)) {
                                        Map<String, Map<String, Integer>> regionMap = regionCompetencyBillable.get(competency);
                                        if (regionMap == null)
                                            regionMap = createRegionMap();
                                        Map<String, Integer> billable = regionMap.get(region);
                                        if (billable == null)
                                            billable = new HashMap<>();
                                        Integer count;
                                        count = billable.get("count");
                                        if (count == null)
                                            count = 0;
                                        billable.put("count", ++count);
                                        regionMap.put(region, billable);
                                        regionCompetencyBillable.put(competency, regionMap);
                                    }
                                }
                            });
                        }
                    });
                } else if (projectType.equalsIgnoreCase("Non-Billable")) {
                    logger.debug("Generating Data for Open Requests :: Non-Billable");
                    employees.forEach(employee -> {
                        if (employee.getAdditionalInfo() != null && employee.getAdditionalInfo().getCurrentNonBillableProjects() != null) {
                            employee.getAdditionalInfo().getCurrentNonBillableProjects().entrySet().forEach(entry -> {
                                String competency = entry.getValue().getCompetency();
                                String region = projectRepository.findOneByProjectNameAndActive(entry.getKey(), true).getRegionvalue().getRegionName();
                                if (regions == null || regions.size() == 0 || regions.contains(region)) {
                                    if (filterList == null || filterList.size() == 0 || filterList.contains(region)) {
                                        Map<String, Map<String, Integer>> competencyBillable = regionCompetencyBillable.get(competency);
                                        if (competencyBillable == null)
                                            competencyBillable = createRegionMap();
                                        Map<String, Integer> billable = competencyBillable.get(region);
                                        if (billable == null)
                                            billable = new HashMap<>();
                                        Integer count;
                                        count = billable.get("count");
                                        if (count == null)
                                            count = 0;
                                        billable.put("count", ++count);

                                        competencyBillable.put(region, billable);
                                        regionCompetencyBillable.put(competency, competencyBillable);
                                    }
                                }
                            });
                        }
                    });
                }
            }

            return regionCompetencyBillable;
        }
    }

    private Map<String, Map<String, Integer>> createRegionMap() {
        Set<String> regions = allRegions;
        Map<String, Map<String, Integer>> regionMap = new HashMap<>();
        for (String region : regions) {
            Map<String, Integer> billable = new HashMap<>();
            billable.put("count", 0);
            regionMap.put(region, billable);

        }
        return regionMap;
    }

    private Map<String, Map<String, Integer>> createProjectMap(List<String> regions) {
        Map<String, Map<String, Integer>> projectMap = new HashMap<>();
        regions.forEach(region -> {
            List<EProject> projects = projectRepository.findByRegion(region);
            for (EProject project : projects) {
                Map<String, Integer> billable = new HashMap<>();
                billable.put("count", 0);
                if (project.getActive())
                    projectMap.put(project.getProjectName(), billable);
            }
        });

        return projectMap;
    }

}
