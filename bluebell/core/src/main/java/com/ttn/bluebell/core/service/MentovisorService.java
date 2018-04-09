package com.ttn.bluebell.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.domain.employee.EEmployeeRegions;
import com.ttn.bluebell.domain.employee.EMentovisor;
import com.ttn.bluebell.durable.model.constant.RabbitMqQueueNameConstant;
import com.ttn.bluebell.durable.model.employee.ChangeMentovisorDTO;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.employee.MentovisorDTO;
import com.ttn.bluebell.repository.EmployeeMentovisorRepository;
import com.ttn.bluebell.repository.EmployeeRegionRepository;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@Transactional
public class MentovisorService {
    @Autowired
    private AmqpTemplate template;
    @Autowired
    private EmployeeMentovisorRepository employeeMentovisorRepository;
    @Autowired
    private EmployeeOperations employeeOperations;
    @Autowired
    private EmployeeRegionRepository employeeRegionRepository;

    public MentovisorDTO getMentovisor(Long empCode) {
        MentovisorDTO mentovisorDTO = null;
        EMentovisor mentovisor = employeeMentovisorRepository.findOne(empCode);
        if (mentovisor != null) {
            Employee employee = employeeOperations.findEmployeeByEmail(mentovisor.getMentovisorEmail());
            Employee mentee = employeeOperations.findEmployeeByEmail(mentovisor.getEmployeeEmail());
            List<EEmployeeRegions> mentovisorRegion = employeeRegionRepository.findByEmail(employee.getEmailAddress());
            List<EEmployeeRegions> menteeRegion = employeeRegionRepository.findByEmail(mentee.getEmailAddress());
            mentovisorDTO = new MentovisorDTO(employee, mentee, mentovisor.getPerformanceReviewerEmail());
            if (mentovisorRegion.size() > 0) {

                mentovisorDTO.setRegion(mentovisorRegion.stream().findFirst().get().getRegionvalue().getRegionName());
            }
            if (menteeRegion.size() > 0)
                mentovisorDTO.setMenteeRegion(menteeRegion.stream().findFirst().get().getRegionvalue().getRegionName());
        }
        return mentovisorDTO;


    }

    private MentovisorDTO getMentovisorAndRegion(Employee employee, List<EMentovisor> eMentovisors, List<EEmployeeRegions> eEmployeeRegions) {
        String mentovisorEmail = eMentovisors.stream().parallel().filter(eMentovisor -> eMentovisor.getEmployeeEmail().equals(employee.getEmailAddress())
        ).map(EMentovisor::getMentovisorEmail).findFirst().get();
        String performanceReviewerEmail = eMentovisors.stream().parallel().filter(eMentovisor -> eMentovisor.getEmployeeEmail().equals(employee.getEmailAddress())
        ).map(EMentovisor::getPerformanceReviewerEmail).findFirst().get();
        String menteeRegion = eEmployeeRegions.stream().parallel().filter(employeeRegion -> employeeRegion.getEmail().equals(employee.getEmailAddress())
        ).map(EEmployeeRegions::getRegionvalue).findFirst().get().getRegionName();
        return new MentovisorDTO(employee, menteeRegion, mentovisorEmail, performanceReviewerEmail);
    }

    public Employee changeMentovisor(ChangeMentovisorDTO changeMentovisorDTO) {
        EMentovisor mentovisor = employeeMentovisorRepository.findById(changeMentovisorDTO.getEmpCode());
        if (mentovisor != null) {
            mentovisor.setMentovisorEmail(changeMentovisorDTO.getMentovisorEmail());
            if (changeMentovisorDTO.getPerformanceReviewerEmail() != null) {
                mentovisor.setPerformanceReviewerEmail(changeMentovisorDTO.getPerformanceReviewerEmail());
            }
            employeeMentovisorRepository.save(mentovisor);
            employeeOperations.updateEmployeeRegion(mentovisor.getEmployeeEmail(), changeMentovisorDTO.getNewRegion());
        }
        assert mentovisor != null;
        return employeeOperations.findEmployeeByEmail(mentovisor.getEmployeeEmail());
    }


    public void publishEvent(ChangeMentovisorDTO changeMentovisorDTO) throws IOException, TimeoutException {
        ObjectMapper mapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        changeMentovisorDTO.setModifiedByEmployeeCode((String) authentication.getPrincipal());
        changeMentovisorDTO.setModifiedOn(DateTime.now().toDate());
        String jsonInString = mapper.writeValueAsString(changeMentovisorDTO);
        template.convertAndSend(RabbitMqQueueNameConstant.changeMentovisorQueue, jsonInString);
    }

    public List<MentovisorDTO> getAllEmployeesMentovisor() {
        Set<Employee> employees = employeeOperations.findAllEmployeeByCompetency(null, false, false);
        ;
        List<EMentovisor> eMentovisors = employeeMentovisorRepository.getAll();
        List<EEmployeeRegions> mentovisorRegion = employeeRegionRepository.getAll();
        return employees.stream().parallel().map(employee -> getMentovisorAndRegion(employee, eMentovisors, mentovisorRegion)).
                collect(Collectors.toList());
    }
}
