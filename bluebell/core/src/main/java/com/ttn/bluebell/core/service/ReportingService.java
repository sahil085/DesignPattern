package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.CommonDataOperations;
import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.api.ReportingOperations;
import com.ttn.bluebell.core.api.StaffingOperations;
import com.ttn.bluebell.core.util.DateUtil;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.staffing.EProjectStaffingRequest;
import com.ttn.bluebell.domain.staffing.EStaffing;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.report.AllocationReportDTO;
import com.ttn.bluebell.durable.model.report.ReportData;
import com.ttn.bluebell.durable.model.staffing.AllocationDetails;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.staffing.Staffing;
import com.ttn.bluebell.durable.enums.BillableType;
import com.ttn.bluebell.repository.ProjectRepository;
import com.ttn.bluebell.repository.StaffRequestRepository;
import com.ttn.bluebell.repository.StaffingRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by ttnd on 2/11/16.
 */
@Service
public class ReportingService implements ReportingOperations {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StaffRequestRepository staffRequestRepository;

    @Autowired
    private StaffingRepository staffingRepository;

    @Autowired
    private DozerBeanMapper beanMapper;

    @Autowired
    private StaffingOperations staffingOperations;
    @Autowired
    private EmployeeOperations employeeOperations;
    @Autowired
    private ExcelUtility excelUtility;
    @Autowired
    private CommonDataOperations commonDataOperations;

    @Override
    public Map<String, Map<String, Map<String, Integer>>> regionWiseCompetencyDetail() {

        List<EProject> projectSet = projectRepository.findAllByActiveIsTrueOrderByProjectNameAsc();
        Map<String, Map<String, Map<String, Integer>>> regionCompMap = new HashMap<>();
        for (EProject project : projectSet) {
            String region = project.getRegion();
            List<EStaffing> staffingList = staffingRepository.findByProjectAndStateAndStaffingRequestActiveIsTrue(project, Staffing.State.Allocated);
            Map<String, Map<String, Integer>> compMap = null;
            if (regionCompMap.containsKey(region))
                compMap = regionCompMap.get(region);
            else {
                compMap = new HashMap<>();
                regionCompMap.put(region, compMap);
            }

            for (EStaffing staffing : staffingList) {
                String competency = staffing.getStaffingRequest().getCompetency();
                Map<String, Integer> billableMap = null;
                if (compMap.containsKey(competency))
                    billableMap = compMap.get(competency);
                else {
                    billableMap = new HashMap<>();
                    billableMap.put("billable", 0);
                    billableMap.put("nonBillable", 0);
                    compMap.put(competency, billableMap);
                }

                if (staffing.getStaffingRequest().getBillableType().equals(BillableType.BILLABLE))
                    billableMap.put("billable", billableMap.get("billable") + 1);
                else {
                    billableMap.put("nonBillable", billableMap.get("nonBillable") + 1);
                }
            }
        }
        return regionCompMap;
    }

    @Override
    public Map<String, Map<String, Map<String, List<StaffRequest>>>> regionAndCompetencyWiseOpenNeedsDetail() {
        List<EProject> projectSet = projectRepository.findAllByActiveIsTrueOrderByProjectNameAsc();
        Map<String, Map<String, Map<String, List<StaffRequest>>>> regionProjectCompetency = new HashMap<>();
        for (EProject project : projectSet) {
            String region = project.getRegionvalue().getRegionName();
            List<EProjectStaffingRequest> staffingList = staffRequestRepository.findByStateAndProjectAndActiveIsTrue(StaffRequest.State.Open, project);
            Map<String, Map<String, List<StaffRequest>>> projectCompetency = regionProjectCompetency.get(region);
            if (projectCompetency == null) {
                projectCompetency = new HashMap<>();
                regionProjectCompetency.put(region, projectCompetency);
            }
            Map<String, List<StaffRequest>> competency = projectCompetency.get(project.getProjectName());
            if (competency == null) {
                competency = new HashMap<>();
                projectCompetency.put(project.getProjectName() + ":" + project.getProjectId(), competency);
            }
            for (EProjectStaffingRequest staffing : staffingList) {
                StaffRequest request = beanMapper.map(staffing, StaffRequest.class);
                request.setProjectId(staffing.getProject().getProjectId());
                request.setNominatedStaffings(staffingOperations.validStaffing(request, Staffing.State.Nominated));
                String comp = staffing.getCompetency();
                List<StaffRequest> staffRequestList = competency.get(comp);
                if (staffRequestList == null)
                    staffRequestList = new ArrayList<>();
                staffRequestList.add(request);
                competency.put(comp, staffRequestList);
            }
            projectCompetency.put(project.getProjectName() + ":" + project.getProjectId(), competency);
            regionProjectCompetency.put(region, projectCompetency);
        }
        return regionProjectCompetency;
    }

    @Override
    public List<Object> allocationSummary(LocalDate startDate, LocalDate endDate) {
        List employeeData = allocationReport(startDate, endDate);
        Map<String, Map<String, Set<AllocationDetails>>> allEmployeeAllocationDetailsMap = (Map<String, Map<String, Set<AllocationDetails>>>) employeeData.get(0);
        Map<String, Set<AllocationDetails>> allocationDetailsMap = allEmployeeAllocationDetailsMap.get("allocationDetails");
        Map<String, Set<AllocationDetails>> employeeOnBench = allEmployeeAllocationDetailsMap.get("onBench");
        Map<String, Map<String, Double>> regionWiseAllocationPercentage = new HashMap<>();
        Map<String, Double> calculateTotal = new HashMap<>();
        Set<String> allRegions = commonDataOperations.findAllActiveRegions();
        allocationDetailsMap.putAll(employeeOnBench);
        allRegions.forEach(region -> {
                    calculateTotal.put(region, 0.0);
                }
        );
        calculateTotal.put("Total", 0.0);
        calculateTotal.put("Bench", 0.0);
        allocationDetailsMap.forEach((key, value) -> {
            Map<String, Double> allocationData = new HashMap<>();
            allocationData.put("Total", 0.0);
            value.forEach(allocationDetail -> {
                if (allocationData.containsKey(allocationDetail.getRegion())) {
                    Double allocationPercentage = ((double) allocationDetail.getAllocationPercentage()) / 100 + allocationData.get(allocationDetail.getRegion());
                    allocationData.put(allocationDetail.getRegion(), allocationPercentage);
                } else {
                    allocationData.put(allocationDetail.getRegion(), (double) allocationDetail.getAllocationPercentage() / 100);
                }
                Double total = allocationData.get("Total") + (double) allocationDetail.getAllocationPercentage() / 100;
                allocationData.put("Total", total);
                total = calculateTotal.get(allocationDetail.getRegion()) + (double) allocationDetail.getAllocationPercentage() / 100;
                calculateTotal.put(allocationDetail.getRegion(), total);
            });
            calculateTotal.put("Total", calculateTotal.get("Total") + allocationData.get("Total"));
            double partiallyOnBench = (1 - allocationData.get("Total")) > 0 ? (1 - allocationData.get("Total")) : 0;
            allocationData.put("Bench", partiallyOnBench);
            calculateTotal.put("Bench", calculateTotal.get("Bench") + partiallyOnBench);
            regionWiseAllocationPercentage.put(key, allocationData);
        });
        calculateTotal.put("Total", calculateTotal.get("Total") + calculateTotal.get("Bench"));
        regionWiseAllocationPercentage.put("Total", calculateTotal);
        employeeData.remove(0);
        employeeData.add(0, regionWiseAllocationPercentage);
        return employeeData;
    }

    private List allocationReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Map<String, Set<AllocationDetails>>> allEmployeesAllocationDetails = new HashMap<>();
        Set<Employee> employees = employeeOperations.findAllEmployeeByCompetency(null, false, false);
        Map<String, Set<AllocationDetails>> employeeAllocationDetailMap = new HashMap<>();
        Map<String, Set<AllocationDetails>> employeeOnBenchMap = new HashMap<>();
        Map<String, Employee> employeeInfo = new HashMap<>();
        employees.forEach(employee -> {
            if(employee.getEmailAddress().equals("aakash.agarwal@tothenew.com")){
                System.out.print("");
            }
            Set<AllocationDetails> allocationDetails = null;
            allocationDetails = staffingOperations.getAllocationForEmployeeOnBench(employee);
            employeeInfo.put(employee.getEmailAddress(), employee);
            boolean onBench = true;
            for (AllocationDetails allocationDetail : allocationDetails) {
                int allocationPercentage = (int) allocationPercentageNumberOfDays(startDate, endDate, allocationDetail);
                if(allocationDetail.getBillableType().equals(BillableType.SHADOW.getBillableType())){
                    allocationPercentage = 0;
                }
                allocationDetail.setAllocationPercentage(allocationPercentage > 0 ? allocationPercentage : 0);
                if (onBench)
                    onBench = !(allocationDetail.getAllocationPercentage() > 0);
            }
            if (onBench)
                employeeOnBenchMap.put(employee.getEmailAddress(), allocationDetails);
            else
                employeeAllocationDetailMap.put(employee.getEmailAddress(), allocationDetails);
        });
        allEmployeesAllocationDetails.put("allocationDetails", employeeAllocationDetailMap);
        allEmployeesAllocationDetails.put("onBench", employeeOnBenchMap);
        List<Object> employeedata = new ArrayList<>();
        employeedata.add(allEmployeesAllocationDetails);
        employeedata.add(employeeInfo);
        return employeedata;
    }

    private float allocationPercentageNumberOfDays(LocalDate startDate, LocalDate endDate, AllocationDetails allocationDetail) {
        LocalDate actualStartDate = allocationDetail.getStartDate();
        LocalDate actualEndDate = allocationDetail.getEndDate().plusDays(1);
        LocalDate tempStartDate = actualStartDate.isBefore(startDate) ? startDate : actualStartDate;
        LocalDate tempEndDate = actualEndDate != null && actualEndDate.isBefore(endDate) ? actualEndDate : endDate;
        float duration = (float) DAYS.between(startDate, endDate);
        float empDuration = (float) DAYS.between(tempStartDate, tempEndDate);
        float ratio = (empDuration / duration);
        return (ratio * allocationDetail.getAllocationPercentage());
    }

    @Override
    public List<AllocationReportDTO> allocationReportSummary(LocalDate startDate, LocalDate endDate) {
        Set<Employee> employees = employeeOperations.findAllEmployeeByCompetency(null, false, false);
        Set<Employee> deactivatedEmployees = employeeOperations.findAllDeactiveEmployee();
        employees.addAll(deactivatedEmployees);
        List<AllocationReportDTO> reportDTO = new ArrayList<>();
        employees.forEach(employee -> {
            boolean isAllocated = false;
            Set<AllocationDetails> allocationDetails = staffingOperations.getAllocationForEmployeeReport(employee);
            for (AllocationDetails allocationDetail : allocationDetails) {
                AllocationReportDTO reportData = allocationDataSetter(startDate, endDate, allocationDetail, employee);
                if (reportData != null) {
                    isAllocated = true;
                    reportDTO.add(reportData);
                }
            }
            if (!isAllocated) {
                if (!deactivatedEmployees.contains(employee)) {
                    AllocationReportDTO reportData = new AllocationReportDTO();
                    reportData.setEmployee(employee);
                    reportData.setRegion(employee.getDepartmentOfEmployee());
                    reportData.setCompetency(employee.getCompetency().getName());
                    reportDTO.add(reportData);
                }
            }
        });
        return reportDTO;
    }

    private AllocationReportDTO allocationDataSetter(LocalDate startDate, LocalDate endDate, AllocationDetails allocationDetail, Employee employee) {
        List<ReportData> dataList = new ArrayList<>();
        boolean isAllocated = false;
        for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
            ReportData data = new ReportData();
            data.setDate(date);
            if (DateUtil.dateInBetweenOrEqualTo(allocationDetail.getStartDate(), allocationDetail.getEndDate(), date)) {
                data.setAllocationPercentage(allocationDetail.getAllocationPercentage());
                data.setBillableType(allocationDetail.getBillableType());
                isAllocated = true;
            }
            dataList.add(data);
        }
        AllocationReportDTO reportDTO = new AllocationReportDTO(employee, allocationDetail, dataList);
        reportDTO.setClientName(allocationDetail.getClientName());
        return isAllocated ? reportDTO : null;
    }

}