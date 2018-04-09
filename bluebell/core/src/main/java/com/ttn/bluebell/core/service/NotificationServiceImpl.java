package com.ttn.bluebell.core.service;

/**
 * Created by ttn on 30/9/16.
 */

import com.ttn.bluebell.core.api.NotificationService;
import com.ttn.bluebell.core.exception.EmailNotificationSenderFailureException;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.employee.UpcomingDeallocationDTO;
import com.ttn.bluebell.durable.model.event.notification.DeallocationIntimationRequest;
import com.ttn.bluebell.durable.model.event.notification.NotificationRequest;
import com.ttn.bluebell.durable.model.event.notification.NotificationRequestWithPayload;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by ttn on 29/9/16.
 */

@SuppressWarnings("deprecation")
@Service
@PropertySource("classpath:application.properties")
@EnableScheduling
public class NotificationServiceImpl implements NotificationService {

    @Resource
    Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private StaffingTemplate staffingTemplate;

    public void sendEmail(MimeMessagePreparator preparator) {
        mailSender.send(preparator);
    }

    public String getVelocityTemplateContent(Map<String, Object> request, String template) {
        StringBuffer content = new StringBuffer();
        request.put("date", new DateTool());
        content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, request));
        if (StringUtils.isEmpty(content))
            throw new EmailNotificationSenderFailureException("exception.email.template.notfound");
        return content.toString();
    }

    public void handler(NotificationRequest notificationRequest) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setSubject(notificationRequest.getSubject());
                helper.setTo(notificationRequest.getReceiverEmail());
                helper.setFrom(env.getProperty("email.default.from"));
                Map<String, Object> model = new HashMap<>();
                List<Employee> employees = notificationRequest.getManagers();
                if (employees != null && employees.size() > 0) {
                    String[] emailIds = new String[employees.size()];
                    StringBuffer spoc = new StringBuffer("");
                    spoc.append(employees.get(0).getName());
                    for (int i = 0; i < employees.size(); i++) {
                        Employee employee = employees.get(i);
                        emailIds[i] = employee.getEmailAddress();
                    }
                    helper.setCc(emailIds);
                    model.put("spoc", spoc);
                }
                initializeStaffinRequestDetails(model, notificationRequest);
                model.put("receiver", notificationRequest.getReceiverName());
                if(env.getProperty("email.service.sendmail.override").equalsIgnoreCase("true")){
                    helper.setCc(env.getProperty("email.cc.constants").split(","));
                    helper.setTo(env.getProperty("email.service.sendmail.override.address"));
                }
                if (env.getProperty("email.cc.add").equals("true")) {
                    helper.addCc(env.getProperty("email.cc.add.default"));
                }
                System.out.println("sending mail with to all users : ");
                Arrays.asList(helper.getMimeMessage().getAllRecipients()).forEach(add ->
                    System.out.print(((InternetAddress)add).getAddress()));
                model.put("project", notificationRequest.getProjectName());
                model.put("startStaffingDate", notificationRequest.getStartStaffingDate());
                model.put("lastStaffingDate", notificationRequest.getLastStaffingDate());
                String text = getVelocityTemplateContent(model, "notification/template/" + notificationRequest.getTemplateName());//Use Freemarker or Velocity
                helper.setText(text, true);

            }
        };
        if (env.getProperty("email.service.sendmail").equalsIgnoreCase("true")){
            sendEmail(preparator);
        }
    }

    @Override
    public void handler(NotificationRequestWithPayload notificationRequest) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setSubject(notificationRequest.getSubject());
                helper.setTo(notificationRequest.getReceiverEmail());
                String[] cc = (String[])notificationRequest.getCcEmails().toArray(new String[0]);
                helper.setCc(cc);
                helper.setFrom(env.getProperty("email.default.from"));
                Map<String, Object> model = new HashMap<>();
                model.put("receiver", notificationRequest.getReceiverName());
                if(env.getProperty("email.service.sendmail.override").equalsIgnoreCase("true")){
                    helper.setCc(env.getProperty("email.cc.constants").split(","));
                    helper.setTo(env.getProperty("email.service.sendmail.override.address"));
                }
                if (env.getProperty("email.cc.add").equals("true")) {
                    helper.addCc(env.getProperty("email.cc.add.default"));
                }
                model.put("payload",notificationRequest.getPayload());
                if(notificationRequest instanceof DeallocationIntimationRequest){
                    model.put("days",((DeallocationIntimationRequest)notificationRequest).getDays());
                }
                String text = getVelocityTemplateContent(model, "notification/template/" + notificationRequest.getTemplateName());//Use Freemarker or Velocity
                helper.setText(text, true);
            }
        };
        if (env.getProperty("email.service.sendmail").equalsIgnoreCase("true")){
            sendEmail(preparator);
        }
    }

//    @Override
//    @Scheduled(cron = "0 0/2 * 1/1 * ?")
//    public void sendDeallocationWarningEmails() {
//        Set<UpcomingDeallocationDTO> upcomingDeallocationDTOS = staffingTemplate.getUpcomingDeallocations(7, null, null);
//        Map<Long,UpcomingDeallocationDTO> upcomingDeallocationDTOsByRegionMap =  upcomingDeallocationDTOS.stream().collect(Collectors.toMap(UpcomingDeallocationDTO::getRegionId, dto -> dto));
//        if(upcomingDeallocationDTOsByRegionMap.entrySet() != null && upcomingDeallocationDTOsByRegionMap.entrySet().size() >=0){
//            for(Long regionId : upcomingDeallocationDTOsByRegionMap.keySet()){
//
//            }
//        }
//    }

    private void initializeStaffinRequestDetails(Map<String, Object> model, NotificationRequest notificationRequest) {
        StaffRequest staffRequest = notificationRequest.getStaffRequest();
        if (staffRequest != null) {
            model.put("title", staffRequest.getTitle());
            model.put("competency", staffRequest.getCompetency());
            model.put("billable", (staffRequest.getBillableType().toString()));
            model.put("allocationPercentage", staffRequest.getAllocationPercentage());
            model.put("startDate", staffRequest.getStartDate());
            model.put("endDate", staffRequest.getEndDate());
            model.put("details", staffRequest.getDetails() != null ? staffRequest.getDetails() : " ");
        }
    }


}