package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.mapper.project.FixedPriceProjectMapper;
import com.ttn.bluebell.domain.project.billing.EFixedPriceBillingInfo;
import com.ttn.bluebell.domain.project.EFixedPriceProject;
import com.ttn.bluebell.durable.model.project.billing.BillingInfo;
import com.ttn.bluebell.durable.model.project.billing.FixedPriceBillingInfo;
import com.ttn.bluebell.durable.model.project.FixedPriceProject;
import com.ttn.bluebell.repository.FixedPriceProjectRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by praveshsaini on 22/9/16.
 */
@Service
@Named("fixedPriceProjectTemplate")
public class FixedPriceProjectTemplate extends BillableProjectTemplate<FixedPriceProject,EFixedPriceProject,FixedPriceBillingInfo> {

    private FixedPriceProjectRepository fixedPriceProjectRepository;

    @Autowired
    private DozerBeanMapper beanMapper;

    @Inject
    public FixedPriceProjectTemplate(FixedPriceProjectRepository fixedPriceProjectRepository) {
        super(fixedPriceProjectRepository, FixedPriceProjectMapper.entity, FixedPriceProjectMapper.durable);
        this.fixedPriceProjectRepository = fixedPriceProjectRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public FixedPriceBillingInfo updateBillingInfo(Long projectId, FixedPriceBillingInfo billingInfo) {
        EFixedPriceProject fixedPriceProject = super.findEntityById(projectId);

        EFixedPriceBillingInfo fixedPriceBillingInfo = beanMapper.map(billingInfo,EFixedPriceBillingInfo.class);
        fixedPriceProject.setBillingInfo(fixedPriceBillingInfo);
        fixedPriceProject = fixedPriceProjectRepository.saveAndFlush(fixedPriceProject);

        return ( (FixedPriceProject)ProjectBasicTemplate.map(fixedPriceProject) ).getBillingInfo();
    }

    @Override
    public BillingInfo getBillingInfo(Long projectId) {

        EFixedPriceProject eFixedPriceProject = super.findEntityById(projectId);
        return ((FixedPriceProject)ProjectBasicTemplate.map(eFixedPriceProject) ).getBillingInfo();

    }
}
