package com.ttn.bluebell.domain.project.billing;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ttnd on 23/9/16.
 */
@Embeddable
public class EFixedPriceBillingInfo extends EBillingInfo {

    @Embedded
    @NotNull
    private EPrice projectValue;


    @ElementCollection
    @CollectionTable(name = "BILLING_MILESTONE", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "billing_milestone_id")
    private List<EBillingMilestone> billingMilestones;

    public EPrice getProjectValue() {
        return projectValue;
    }

    public void setProjectValue(EPrice projectValue) {
        this.projectValue = projectValue;
    }

    public List<EBillingMilestone> getBillingMilestones() {
        return billingMilestones;
    }

    public void setBillingMilestones(List<EBillingMilestone> billingMilestones) {
        this.billingMilestones = billingMilestones;
    }
}
