package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.employee.EEmployeeReviewerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ttn on 2/11/16.
 */
public interface EmployeeReviewerFeedbackRepository extends JpaRepository<EEmployeeReviewerFeedback,Long>{

    List<EEmployeeReviewerFeedback> findByEmailOrderByRatingCycleDesc(String email);

}
