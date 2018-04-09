package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.employee.EMentovisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeMentovisorRepository extends JpaRepository<EMentovisor, Long> {
    EMentovisor findById(Long empCode);

    EMentovisor findByEmployeeEmail(String email);

    @Query(value = "select er from EMentovisor er")
    List<EMentovisor> getAll();
}

