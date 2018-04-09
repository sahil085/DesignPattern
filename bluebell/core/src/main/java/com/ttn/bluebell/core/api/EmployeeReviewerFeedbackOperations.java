package com.ttn.bluebell.core.api;

import com.ttn.bluebell.domain.employee.EEmployeeReviewerFeedback;
import com.ttn.bluebell.durable.model.employee.ReviewerFeedback;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ttn on 2/11/16.
 */
public interface EmployeeReviewerFeedbackOperations {

    Map<String, List<ReviewerFeedback>> findReviewersRating(Set<String> emailAddresses);
    EEmployeeReviewerFeedback save(EEmployeeReviewerFeedback eEmployeeReviewerFeedback);
}
