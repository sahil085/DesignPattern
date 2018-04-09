package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.staffing.EProjectStaffingRequest;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by ttnd on 5/10/16.
 */
@Repository
public interface StaffRequestRepository extends JpaRepository<EProjectStaffingRequest, Long> {

    EProjectStaffingRequest findByIdAndActiveIsTrue(Long id);

    EProjectStaffingRequest findByIdAndProjectAndActiveIsTrue(Long id, EProject project);

    List<EProjectStaffingRequest> findByProjectAndTitleAndState(EProject project, String title, StaffRequest.State state);

    List<EProjectStaffingRequest> findByStateAndProject(StaffRequest.State state, EProject project);

    List<EProjectStaffingRequest> findByStateAndActiveIsTrue(StaffRequest.State state);

    List<EProjectStaffingRequest> findAllByProjectAndIsReallocatedAndEndDateBetween(EProject project,Boolean isReallocated, Date date1, Date date2);
    List<EProjectStaffingRequest> findByStateAndProjectAndActiveIsTrue(StaffRequest.State state, EProject project);


}
