package com.ttn.bluebell.core.api;

import com.ttn.bluebell.durable.model.common.ResponseBean;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.project.billing.BillingInfo;

import java.util.Set;

/**
 * Created by praveshsaini on 16/9/16.
 */
public interface ProjectOperations <D extends Project, T extends BillingInfo> {

    ResponseBean create(D project);
    Set<D> findAll();
    ResponseBean update(Long projectId, D project);
    BillingInfo updateBillingInfo(Long projectId, T billingInfo);
    BillingInfo getBillingInfo(Long projectId);
}
