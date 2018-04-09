package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.employee.EEmployeeRegions;
import com.ttn.bluebell.repository.customRepository.RegionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ttnd on 21/10/16.
 */
public interface EmployeeRegionRepository extends JpaRepository<EEmployeeRegions, Long>, RegionRepositoryCustom {

    /*@Query(value = "select EMAIL, group_concat('REGION') as REGION from EMPLOYEE_REGIONS group by email", nativeQuery = true)
    List<EEmployeeRegions> findRegionsGroupByEmail();
    */
    List<EEmployeeRegions> findAllByEmail(String email);

    List<EEmployeeRegions> findByEmail(String email);

    void deleteByEmail(String email);
    @Query(value = "select er from EEmployeeRegions er")
    List<EEmployeeRegions> getAll();
/*
    @Query(value = "select r.email from EMPLOYEE_REGIONS r, EMPLOYEE_ROLES er where r.email=er.email and er.roles='REGION HEAD' and r.region=?1", nativeQuery = true)
    List<String> getRegionHeadByEmail(String rregion);
*/

}
