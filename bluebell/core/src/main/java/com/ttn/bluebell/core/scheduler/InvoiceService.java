package com.ttn.bluebell.core.scheduler;

import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.service.StaffingTemplate;
import com.ttn.bluebell.core.service.TimeAndMoneyProjectTemplate;
import com.ttn.bluebell.domain.project.ETimeAndMoneyProject;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.event.notification.InvoiceNotificationRequest;
import com.ttn.bluebell.durable.model.project.TimeAndMoneyProject;
import com.ttn.bluebell.durable.model.project.billing.InvoiceInfo;
import com.ttn.bluebell.repository.EmployeeRegionRepository;
import com.ttn.bluebell.repository.RegionRepository;
import org.dozer.DozerBeanMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by ttnd on 19/10/16.
 */
@Service
public class InvoiceService {

    @Autowired
    private TimeAndMoneyProjectTemplate timeAndMoneyProjectTemplate;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private EmployeeOperations employeeOperations;
    @Autowired
    private EmployeeRegionRepository employeeRegionRepository;
    @Autowired
    private StaffingTemplate staffingTemplate;
    @Autowired
    private DozerBeanMapper beanMapper;
    @Autowired
    private RegionRepository regionRepository;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);

    @Transactional
    public void sendInvoice() {
        LOGGER.info(">>>>>>>>>>>>>>> Invice scheduler start");
        Set<TimeAndMoneyProject> tnmProject = timeAndMoneyProjectTemplate.findAll();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 6);

        Calendar fortnightDate = Calendar.getInstance();
        fortnightDate.set(Calendar.DATE, 15);
        int lastDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DATE);
        Calendar monthLastDate = Calendar.getInstance();
        monthLastDate.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);

        for (TimeAndMoneyProject project : tnmProject) {
            try {
                boolean flag = false;
                Date invoiceDate = project.getBillingInfo().getInvoiceInfo().getInvoiceStartDate();
                if (invoiceDate.compareTo(startDate.getTime()) <= 0) {
                    InvoiceInfo.InvoiceCycle invoiceCycle = project.getBillingInfo().getInvoiceInfo().getInvoiceCycle();

                    if (invoiceCycle == InvoiceInfo.InvoiceCycle.FortNight &&
                            startDate.compareTo(fortnightDate) <= 0 && endDate.compareTo(fortnightDate) >= 0) {

                        flag = true;

                    } else if (invoiceCycle == InvoiceInfo.InvoiceCycle.Monthly &&
                            startDate.compareTo(monthLastDate) <= 0 && endDate.compareTo(monthLastDate) >= 0) {
                        flag = true;

                    } else if (invoiceCycle == InvoiceInfo.InvoiceCycle.Weekly) {

                        flag = true;
                    }
                }

                if (flag) {
                    List<Employee> employees = new ArrayList<>();
                    List<String> emails = employeeRegionRepository.getRegionHeadForRegion(regionRepository.findByExternalId(project.getRegionvalue().getExternalId()));
                    emails.stream().forEach(email -> employees.add(employeeOperations.findEmployeeByEmail(email)));


                    ETimeAndMoneyProject eProject = beanMapper.map(project, ETimeAndMoneyProject.class);
                    List<Employee> projectManager = staffingTemplate.findManagers(eProject, "");
                    String receiverName = "", receiverEmail = "";

                    if (projectManager != null && !projectManager.isEmpty()) {
                        receiverName = projectManager.get(0).getName();
                        receiverEmail = projectManager.get(0).getEmailAddress();
                        employees.addAll(projectManager);
                    }
                    InvoiceNotificationRequest invoiceNotificationRequest = new InvoiceNotificationRequest(receiverName,
                            receiverEmail, project.getProjectName(), employees);
//                    publisher.publishEvent(invoiceNotificationRequest);
                }
            } catch (Exception e) {
                LOGGER.error(e.getStackTrace().toString());
            }


        }
    }

}
