package com.ttn.bluebell.core.mapper.project;

import com.ttn.bluebell.domain.employee.EEmployeeReviewerFeedback;
import com.ttn.bluebell.durable.model.employee.ReviewerFeedback;
import org.dozer.DozerBeanMapper;

import java.util.function.Function;

/**
 * Created by ttn on 2/11/16.
 */
public class EmployeeReviewerFeedbackMapper {

    public static Function<EEmployeeReviewerFeedback,ReviewerFeedback> durable = new Function<EEmployeeReviewerFeedback,ReviewerFeedback>() {
        @Override
        public ReviewerFeedback apply(EEmployeeReviewerFeedback eEmployeeReviewerFeedback) {
            return new DozerBeanMapper().map(eEmployeeReviewerFeedback,ReviewerFeedback.class);
        }
    };

    public static Function<ReviewerFeedback,EEmployeeReviewerFeedback> entity = new Function<ReviewerFeedback,EEmployeeReviewerFeedback>() {
        @Override
        public EEmployeeReviewerFeedback apply(ReviewerFeedback reviewerFeedback) {
            return new DozerBeanMapper().map(reviewerFeedback,EEmployeeReviewerFeedback.class);
        }
    };

}
