package com.ttn.bluebell.core.service;

import com.ttn.bluebell.domain.project.EBillableProject;
import com.ttn.bluebell.durable.model.project.billing.BillingInfo;
import com.ttn.bluebell.durable.model.project.BillableProject;
import com.ttn.bluebell.repository.BaseProjectRepository;

import java.util.function.Function;

/**
 * Created by praveshsaini on 22/9/16.
 */
public abstract class BillableProjectTemplate <D extends BillableProject, E extends EBillableProject, T extends BillingInfo> extends ProjectTemplate <D,E,T> {

    public BillableProjectTemplate(BaseProjectRepository<E> baseProjectRepository, Function<D,E> entity, Function<E, D> durable) {
        super(baseProjectRepository, entity, durable);
    }


}
