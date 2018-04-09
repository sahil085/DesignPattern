package com.ttn.bluebell.core.scheduler;

import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.api.StaffingOperations;
import com.ttn.bluebell.core.service.MentovisorService;
import com.ttn.bluebell.domain.region.Region;
import com.ttn.bluebell.domain.staffing.EStaffing;
import com.ttn.bluebell.durable.model.employee.ChangeMentovisorDTO;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.core.emailModel.DeallocationIntimationModel;
import com.ttn.bluebell.durable.model.event.notification.DeallocationIntimationRequest;
import com.ttn.bluebell.durable.model.staffing.Staffing;
import com.ttn.bluebell.repository.EmployeeRegionRepository;
import com.ttn.bluebell.repository.RegionRepository;
import com.ttn.bluebell.repository.StaffingRepository;
import com.ttn.bluebell.repository.SystemParameterRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by ttnd on 20/10/16.
 */
@Service
public class AutoDeallocationService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private StaffingRepository staffingRepository;
    @Autowired
    private StaffingOperations staffingOperations;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private EmployeeOperations employeeOperations;
    @Autowired
    private EmployeeRegionRepository employeeRegionRepository;
    @Autowired
    private SystemParameterRepository systemParameterRepository;
    @Autowired
    MentovisorService mentovisorService;
    @Value("${employee.bench.mentovisor.email}")
    String mentovisorEmail;
    @Value("${employee.bench.region}")
    String employeeRegion;

    @Autowired
    private RegionRepository regionRepository;

    public void DeallocateStaff() {
        List<EStaffing> eStaffingList = staffingRepository.findByState(Staffing.State.Allocated);
        Date currDate = new Date();
        eStaffingList.stream().forEach(eStaffing -> {
                    if (eStaffing.getStaffingRequest().getEndDate().before(currDate) && (eStaffing.getStaffingRequest().getActive()))
                        try {
                            staffingOperations.deAllocate(eStaffing.getProject().getProjectId(), eStaffing.getStaffingRequest().getId(), eStaffing.getEmail(), null, new Date());
                            Employee employee = employeeOperations.findEmployeeByEmail(eStaffing.getEmail());
                            if (!(employee.getAdditionalInfo().getTotalBillableAllocation() == 0 &&
                                    employee.getAdditionalInfo().getTotalNonBillableAllocation() == 0)) {
                                ChangeMentovisorDTO changeMentovisorDTO = new ChangeMentovisorDTO();
                                changeMentovisorDTO.setEmpCode(Long.parseLong(employee.getCode()));
                                changeMentovisorDTO.setEmployeeName(employee.getName());
                                changeMentovisorDTO.setMentovisorEmail(mentovisorEmail);
                                changeMentovisorDTO.setNewRegion(employeeRegion);
                                mentovisorService.changeMentovisor(changeMentovisorDTO);
                                mentovisorService.publishEvent(changeMentovisorDTO);
                            }

                        } catch (Exception e) {
                        }
                }

        );
    }

    public void sendFutureDeallocationIntimation(Integer intimationDays) {
        List<EStaffing> eStaffingList = staffingRepository.findByState(Staffing.State.Allocated);
        Map<Long, List<EStaffing>> eStaffingByRegionMap = new HashMap<>();
        eStaffingList.stream().forEach(
                eStaff -> {
                    Date currDate = new Date();
                    Long dateDiff = eStaff.getStaffingRequest().getEndDate().getTime() - currDate.getTime();
                    Long daysDiff = TimeUnit.MILLISECONDS.toDays(dateDiff);
                    if (daysDiff.longValue() >= 0 && daysDiff.longValue() <= intimationDays.longValue()) {
                        if (eStaffingByRegionMap.containsKey(eStaff.getProject().getRegionvalue().getId())) {
                            eStaffingByRegionMap.get(eStaff.getProject().getRegionvalue().getId()).add(eStaff);
                        } else {
                            List eStaffingListOfRegion = new ArrayList<>();
                            eStaffingListOfRegion.add(eStaff);
                            eStaffingByRegionMap.put(eStaff.getProject().getRegionvalue().getId(),eStaffingListOfRegion);
                        }
                    }
                }
        );

        if (eStaffingByRegionMap.keySet() != null && eStaffingByRegionMap.keySet().size() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = new Date();
            for (Long regionId : eStaffingByRegionMap.keySet()) {
                List<Employee> employees = new ArrayList<>(1);
                List<String> emailList = employeeRegionRepository.getRegionHeadForRegionId(regionId);
                for (String email : emailList) {
                    employees.add(employeeOperations.findEmployeeByEmail(email));
                }
                if(employees.size()==0){
                    LOGGER.info("No region head for region {}", regionId);
                    continue;
                }
                String receiverName = employees.get(0).getName();
                String receiverEmail = employees.get(0).getEmailAddress();
                Region region = regionRepository.findById(regionId);
                List<String> ccEmails = employees.stream().map(e -> e.getEmailAddress()).collect(Collectors.toList());
                DeallocationIntimationModel deallocationIntimationModel = new DeallocationIntimationModel(
                        eStaffingByRegionMap.get(regionId) ,region.getRegionName(),dateFormat.format(date)
                );
                String subject = "Auto Deallocation Intimation | "+region.getRegionName() +" | "+dateFormat.format(date)+" ";

                DeallocationIntimationRequest request = new DeallocationIntimationRequest(receiverName,
                        receiverEmail,subject,ccEmails, deallocationIntimationModel,intimationDays);
                publisher.publishEvent(request);
            }
        }
    }
}
