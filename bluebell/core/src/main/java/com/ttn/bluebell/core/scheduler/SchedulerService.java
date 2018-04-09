package com.ttn.bluebell.core.scheduler;

import com.ttn.bluebell.core.service.EmployeeTemplate;
import com.ttn.bluebell.core.service.ProjectBasicTemplate;
import com.ttn.bluebell.integration.service.NewersWorldTemplate;
import com.ttn.bluebell.domain.common.ESystemParameter;
import com.ttn.bluebell.durable.model.common.SystemParameter;
import com.ttn.bluebell.repository.SystemParameterRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by ttnd on 18/10/16.
 */
public class SchedulerService {

    @Autowired
    private EmployeeTemplate employeeTemplate;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private AutoDeallocationService deallocationService;
    @Autowired
    private ProjectBasicTemplate projectService;
    @Autowired
    private NewersWorldTemplate newersWorldTemplate;
    @Autowired
    private SystemParameterRepository systemParameterRepository;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshEmployeeCache(){
        deallocationService.DeallocateStaff();
        employeeTemplate.findAllEmployee();
        projectService.deactivateProjectOnEndDate();
        newersWorldTemplate.refreshEmployeesLeaveCounts();
       // successFactorTemplate.findAllEmployee("grails");
        LOGGER.info("scheduler");
    }

    //    @Scheduled(cron = "0 0/15 * * * ?")
    @Scheduled(cron = "0 0 2 ? * MON,THU,FRI")
    public void sendDeallocationEmails(){
        ESystemParameter param = systemParameterRepository.findByType(SystemParameter.ParameterType.DEALLOCATION_INTIMATION_DAYS.name()).get(0);
        //TODO : Handle all values of this system paramter, right now only one
        Long intimationDays = Long.valueOf(param.getValue());
        deallocationService.sendFutureDeallocationIntimation(intimationDays.intValue());
        LOGGER.info("scheduler send deallocation emails");
    }

    public void sendDeallocationEmailsNoSchedule(Integer days){
        deallocationService.sendFutureDeallocationIntimation(days);
        LOGGER.info("scheduler send deallocation emails");
    }

//#ToDo Commented Out invoice
//    @Scheduled(fixedDelay = 60000)
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void sendInvoice(){
//       invoiceService.sendInvoice();
//    }

//    @Scheduled(cron = "0 0 0 * * ?")
//    public void updateEmployeeResignationTable(){
//        employeeTemplate.findAndSaveEmployeesResignations();
//        LOGGER.info("scheduler");
//    }
}
