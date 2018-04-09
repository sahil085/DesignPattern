package com.ttn.bluebell.core.api;

import com.ttn.bluebell.durable.model.employee.ResignationDTO;

import java.text.ParseException;

public interface ResignationOperations {
//    void findAndSaveEmployeesResignations();
      void saveEmployeeResignation(ResignationDTO resignationDTO);
      void deleteEmployeeResignation(String empCode);
}
