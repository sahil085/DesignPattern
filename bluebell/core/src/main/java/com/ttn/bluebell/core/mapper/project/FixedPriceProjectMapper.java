package com.ttn.bluebell.core.mapper.project;

import com.ttn.bluebell.domain.project.EFixedPriceProject;
import com.ttn.bluebell.durable.model.project.FixedPriceProject;
import org.dozer.DozerBeanMapper;

import java.util.function.Function;

/**
 * Created by praveshsaini on 22/9/16.
 */
public class FixedPriceProjectMapper {
    public static Function<EFixedPriceProject,FixedPriceProject> durable = new Function<EFixedPriceProject, FixedPriceProject>() {
        @Override
        public FixedPriceProject apply(EFixedPriceProject eFixedPriceProject) {
            return new DozerBeanMapper().map(eFixedPriceProject,FixedPriceProject.class);
        }
    };

    public static Function<FixedPriceProject,EFixedPriceProject> entity = new Function<FixedPriceProject, EFixedPriceProject>() {
        @Override
        public EFixedPriceProject apply(FixedPriceProject fixedPriceProject) {
            return new DozerBeanMapper().map(fixedPriceProject,EFixedPriceProject.class);
        }
    };

}
