package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.staffing.EProjectStaffingRequest;
import com.ttn.bluebell.domain.staffing.EStaffing;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.staffing.Staffing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by praveshsaini on 7/10/16.
 */
@Repository
public interface StaffingRepository extends JpaRepository<EStaffing,Long>{
    EStaffing findByStaffingRequestAndEmail(EProjectStaffingRequest staffingRequest, String email);
    EStaffing findTopByStaffingRequestAndStateOrderByIdDesc(EProjectStaffingRequest eProjectStaffingRequest, Staffing.State state);
    EStaffing findTopByStaffingRequestAndStateAndEmailOrderByIdDesc(EProjectStaffingRequest eProjectStaffingRequest, Staffing.State state, String email);
    EStaffing findTopByStaffingRequestAndEmailOrderByIdDesc(EProjectStaffingRequest eProjectStaffingRequest, String email);
    EStaffing findTopByEmailOrderByIdDesc(String email);
    List<EStaffing> findByStaffingRequestAndState(EProjectStaffingRequest eProjectStaffingRequest, Staffing.State state);
    List<EStaffing> findByEmailAndStateAndStaffingRequestActiveIsTrue(String email, Staffing.State state);
    List<EStaffing> findByStaffingRequestAndProjectAndState(EProjectStaffingRequest eProjectStaffingRequest, EProject eProject, Staffing.State state);
    List<EStaffing> findByStaffingRequestInAndState(List<EProjectStaffingRequest> eProjectStaffingRequests, Staffing.State state);
    @Query(value = "select es from EStaffing es where es.state = ?1 and es.staffingRequest.endDate > ?2 and es.staffingRequest.endDate < ?3 and es.staffingRequest.active = true")
    List<EStaffing> findByStateAndDateAfterAndDateBefore(Staffing.State state,Date date1, Date date2);
    long countByStaffingRequestAndProjectAndStateAndEmail(EProjectStaffingRequest eProjectStaffingRequest, EProject eProject, Staffing.State state, String email);
    List<EStaffing> findByState(Staffing.State state);
    List<EStaffing> findByProjectAndStateAndStaffingRequestActiveIsTrue(EProject project, Staffing.State state);
    List<EStaffing> findByEmail(String email);
    List<EStaffing> findAllByEmailIn(List<String> emails);
    List<EStaffing> findByProjectAndEmail(EProject project,String email);
    long countAllByStaffingRequestAndState(EProjectStaffingRequest eProjectStaffingRequest, Staffing.State state);
    @Query(value = "select count(es) from EStaffing es  where es.project = ?1 and es.state = ?2 and es.email=?3 and es.staffingRequest.startDate <= ?4 and es.staffingRequest.endDate >= ?5")
    long countByProjectAndStateAndEmailBetweenStartDateAndEndDate( EProject eProject, Staffing.State state, String email,Date date1, Date date2);
    List<EStaffing> findEStaffingByStaffingRequestStateAndState(StaffRequest.State state,Staffing.State state1);
}