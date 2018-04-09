package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.employee.EEmployeCompetencyRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Created by ttnd on 5/10/16.
 */
public interface EmployeeCompetencyRatingRepository extends JpaRepository<EEmployeCompetencyRating,String> {

   Set<EEmployeCompetencyRating> findByCodeIn(Set<String> employeeCodes);
}
