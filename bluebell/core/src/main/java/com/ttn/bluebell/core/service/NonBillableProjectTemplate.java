package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.mapper.project.NonBillableProjectMapper;
import com.ttn.bluebell.repository.NonBillableProjectRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by praveshsaini on 22/9/16.
 */
@Service
@Named("nonBillableProjectTemplate")
public class NonBillableProjectTemplate extends ProjectTemplate {
    private NonBillableProjectRepository nonBillableProjectRepository;

    @Inject
    public NonBillableProjectTemplate(NonBillableProjectRepository nonBillableProjectRepository) {
        super(nonBillableProjectRepository, NonBillableProjectMapper.entity, NonBillableProjectMapper.durable);
        this.nonBillableProjectRepository = nonBillableProjectRepository;
    }
}
