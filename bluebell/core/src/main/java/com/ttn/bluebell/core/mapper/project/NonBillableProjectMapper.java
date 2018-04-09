package com.ttn.bluebell.core.mapper.project;

import com.ttn.bluebell.domain.project.ENonBillableProject;
import com.ttn.bluebell.durable.model.project.NonBillableProject;
import org.dozer.DozerBeanMapper;

import java.util.function.Function;

/**
 * Created by praveshsaini on 22/9/16.
 */
public class NonBillableProjectMapper {

    public static Function<ENonBillableProject,NonBillableProject> durable = new Function<ENonBillableProject, NonBillableProject>() {
        @Override
        public NonBillableProject apply(ENonBillableProject ENonBillableProject) {
            return new DozerBeanMapper().map(ENonBillableProject,NonBillableProject.class);
        }
    };

    public static Function<NonBillableProject,ENonBillableProject> entity = new Function<NonBillableProject, ENonBillableProject>() {
        @Override
        public ENonBillableProject apply(NonBillableProject NonBillableProject) {
            return new DozerBeanMapper().map(NonBillableProject,ENonBillableProject.class);
        }
    };

}
