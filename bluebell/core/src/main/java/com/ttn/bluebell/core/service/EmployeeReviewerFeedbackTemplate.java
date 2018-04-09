package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.EmployeeReviewerFeedbackOperations;
import com.ttn.bluebell.core.mapper.project.EmployeeReviewerFeedbackMapper;
import com.ttn.bluebell.domain.employee.EEmployeeReviewerFeedback;
import com.ttn.bluebell.durable.model.employee.ReviewerFeedback;
import com.ttn.bluebell.repository.EmployeeReviewerFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ttn on 2/11/16.
 */

@Service
public class EmployeeReviewerFeedbackTemplate implements EmployeeReviewerFeedbackOperations{

    @Autowired
    private EmployeeReviewerFeedbackRepository employeeReviewerFeedbackRepository;

    @Override
    public Map<String, List<ReviewerFeedback>> findReviewersRating(Set<String> emailAddresses) {
        Map<String,List<ReviewerFeedback>> reviewerRatings = new HashMap<>();
        for(String email : emailAddresses) {
            List<ReviewerFeedback> reviewerFeedbacks = employeeReviewerFeedbackRepository.findByEmailOrderByRatingCycleDesc(email).stream().map(eEmployeeReviewerFeedback -> EmployeeReviewerFeedbackMapper.durable.apply(eEmployeeReviewerFeedback)).collect(Collectors.toList());
            reviewerRatings.put(email,reviewerFeedbacks);
        }
        return reviewerRatings;
    }

    @Override
    public EEmployeeReviewerFeedback save(EEmployeeReviewerFeedback eEmployeeReviewerFeedback){
        return employeeReviewerFeedbackRepository.saveAndFlush(eEmployeeReviewerFeedback);
    }

}
