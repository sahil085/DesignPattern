package com.ttn.bluebell.repository;
import com.ttn.bluebell.domain.employee.EmployeeResignation;
import com.ttn.bluebell.domain.staffing.EStaffing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeResignationRepository extends JpaRepository<EmployeeResignation,String> {
    @Override
    List<EmployeeResignation> findAll();
    EmployeeResignation findByEmployeeCode(String employeeCode);
}
