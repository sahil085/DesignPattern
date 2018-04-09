package com.ttn.bluebell.core.listners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ttn.bluebell.core.service.EmployeeTemplate;
import com.ttn.bluebell.core.service.RegionService;
import com.ttn.bluebell.durable.model.constant.RabbitMqQueueNameConstant;
import com.ttn.bluebell.durable.model.employee.CoreEntityDTO;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.employee.EmployeeUpdateDTO;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.naming.Binding;
import java.io.IOException;
import java.text.ParseException;

@EnableRabbit
@Component
public class QueueListener {
    @Autowired
    private EmployeeTemplate employeeTemplate;
    @Autowired
    private RegionService regionService;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(QueueListener.class);
    @RabbitListener(queues = RabbitMqQueueNameConstant.updateEmployeeQueue)
    void synEmployee(String employeeJson) throws ParseException {
        try {
            LOGGER.debug("synEmployee method started");
            LOGGER.debug(employeeJson);
            System.out.println(employeeJson);
            Gson mapper = new Gson();
            EmployeeUpdateDTO employeeUpdateDTO=mapper.fromJson(employeeJson,EmployeeUpdateDTO.class);
            employeeTemplate.updateEmployee(employeeUpdateDTO);
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
        }


    }


    @RabbitListener(queues = RabbitMqQueueNameConstant.updateCoreEntityQueue)
    void synRegion(Object coreJson) throws ParseException {
        try {
            LOGGER.debug("synRegion method started");
            String coreJsonString=new String(((Message) coreJson).getBody());
            LOGGER.debug(coreJsonString);
            System.out.println(coreJsonString);
            Gson mapper = new Gson();
             CoreEntityDTO coreEntityDTO=mapper.fromJson(coreJsonString,CoreEntityDTO.class);
            if(coreEntityDTO.getCoreEntityType().equalsIgnoreCase("Region"))
             regionService.updateRegion(coreEntityDTO);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }


    }


}
