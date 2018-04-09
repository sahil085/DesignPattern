package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.mapper.project.TimeAndMoneyProjectMapper;
import com.ttn.bluebell.domain.project.ETimeAndMoneyProject;
import com.ttn.bluebell.domain.project.billing.ETNMBillingInfo;
import com.ttn.bluebell.durable.model.project.TimeAndMoneyProject;
import com.ttn.bluebell.durable.model.project.billing.BillingInfo;
import com.ttn.bluebell.durable.model.project.billing.TNMBillingInfo;
import com.ttn.bluebell.repository.TimeAndMoneyProjectRepository;
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
@Named("timeAndMoneyProjectTemplate")
public class TimeAndMoneyProjectTemplate extends BillableProjectTemplate<TimeAndMoneyProject,ETimeAndMoneyProject, TNMBillingInfo> {

    private TimeAndMoneyProjectRepository timeAndMoneyProjectRepository;

    @Autowired
    private DozerBeanMapper beanMapper;

    @Inject
    public TimeAndMoneyProjectTemplate(TimeAndMoneyProjectRepository timeAndMoneyProjectRepository) {
        super(timeAndMoneyProjectRepository, TimeAndMoneyProjectMapper.entity, TimeAndMoneyProjectMapper.durable);
        this.timeAndMoneyProjectRepository = timeAndMoneyProjectRepository;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TNMBillingInfo updateBillingInfo(Long projectId, TNMBillingInfo tnmBillingInfo) {

        ETimeAndMoneyProject timeAndMoneyProject = super.findEntityById(projectId);
        ETNMBillingInfo billingInfo = beanMapper.map(tnmBillingInfo,ETNMBillingInfo.class);
        timeAndMoneyProject.setBillingInfo(billingInfo);
        timeAndMoneyProject = timeAndMoneyProjectRepository.saveAndFlush(timeAndMoneyProject);
        return ( (TimeAndMoneyProject)ProjectBasicTemplate.map(timeAndMoneyProject) ).getBillingInfo();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public BillingInfo getBillingInfo(Long projectId) {
        ETimeAndMoneyProject eTimeAndMoneyProject = super.findEntityById(projectId);
        return ((TimeAndMoneyProject)ProjectBasicTemplate.map(eTimeAndMoneyProject)) .getBillingInfo();
    }

}
