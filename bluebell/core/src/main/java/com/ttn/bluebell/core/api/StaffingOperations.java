package com.ttn.bluebell.core.api;

import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.staffing.EProjectStaffingRequest;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.employee.UpcomingDeallocationDTO;
import com.ttn.bluebell.durable.model.staffing.AllocationDetails;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.staffing.Staffing;

import java.util.*;

public interface StaffingOperations {

    StaffRequest updateStaffingRequest( Long projectId, StaffRequest staffRequest, String loggedInUser);
    void delete(Long projectId, Long staffId);
    void allocate(Long projectId, Long staffingRequestId, String email, String loggedInUser);
    void deAllocate(Long projectId, Long staffingRequestId, String email, String loggedInUser,Date lastWorkingDate);
    void nominate(Long projectId, Long staffingRequestId, String email, String loggedInUser);
    void rejectNomination(Long projectId, Long staffingRequestId, String email, String loggedInUser);
    void reAllocate(StaffRequest staffRequest,String loggedInUser);
    Map<String,Integer> countUpcomingDeallocations(int days, String competency, ArrayList<String> regions);
    Set<UpcomingDeallocationDTO> getUpcomingDeallocations(int days, ArrayList<String> regions,String competency);
    List<Staffing> validStaffing(StaffRequest staffingRequest, Staffing.State state);
    List<Staffing> findByStaffRequestAndProjectAndState(EProjectStaffingRequest staffingRequest, EProject project, Staffing.State state);
    Staffing findLatestByStaffRequestAndState(StaffRequest staffRequest, Staffing.State state);
    Set<AllocationDetails> getAllocationForEmail(String email);
    Set<AllocationDetails> getAllocationForEmployeeOnBench(Employee employee);
    Set<AllocationDetails> getAllocationForEmployeeReport(Employee employee);
    List<StaffRequest> getDeallocateSummary(long projectId);
}