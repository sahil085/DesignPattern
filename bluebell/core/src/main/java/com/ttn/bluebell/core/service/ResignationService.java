package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.ResignationOperations;
import com.ttn.bluebell.domain.employee.EmployeeResignation;
import com.ttn.bluebell.durable.model.employee.ResignationDTO;
import com.ttn.bluebell.repository.EmployeeResignationRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

@Service
public class ResignationService implements ResignationOperations{
    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ResignationService.class);
    @Transactional
    @Override
    public void saveEmployeeResignation(ResignationDTO resignation) {
            EmployeeResignation employeeResignation = new EmployeeResignation();
            employeeResignation.setEmployeeCode(resignation.getEmployeeCode());
            employeeResignation.setExitNote(resignation.getExitNote());
            employeeResignation.setNoticePeriod(resignation.getNoticePeriod());
            employeeResignation.setInitiationDate(resignation.getInitiationDate());
            employeeResignation.setLastWorkingDate(resignation.getLastWorkingDate());
            employeeResignationRepository.save(employeeResignation);
    }

    @Override
    public void deleteEmployeeResignation(String empCode) {
        employeeResignationRepository.delete(empCode);
    }
    //    @Override
//    @Transactional
//    public void findAndSaveEmployeesResignations(){
//        List<ResignationDTO> resignations= successFactor.findAllResignations();
//        resignations.forEach(resignation-> {
//            EmployeeResignation employeeResignation = new EmployeeResignation();
//            employeeResignation.setEmployeeCode(resignation.getEmployeeCode());
//            employeeResignation.setExitNote(resignation.getExitNote());
//            employeeResignation.setNoticePeriod(resignation.getNoticePeriod());
//            employeeResignation.setInitiationDate(resignation.getInitiationDate());
//            employeeResignation.setLastWorkingDate(resignation.getLastWorkingDate());
//            employeeResignationRepository.saveAndFlush(employeeResignation);
//
//        });
//
//    }

}
