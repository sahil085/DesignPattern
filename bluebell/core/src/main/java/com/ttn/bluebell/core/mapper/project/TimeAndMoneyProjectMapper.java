package com.ttn.bluebell.core.mapper.project;

import com.ttn.bluebell.domain.project.*;
import com.ttn.bluebell.durable.model.project.TimeAndMoneyProject;
import org.dozer.DozerBeanMapper;

import java.util.function.Function;

/**
 * Created by praveshsaini on 22/9/16.
 */
public class TimeAndMoneyProjectMapper {

    public static Function<ETimeAndMoneyProject,TimeAndMoneyProject> durable = new Function<ETimeAndMoneyProject, TimeAndMoneyProject>() {
        @Override
        public TimeAndMoneyProject apply(ETimeAndMoneyProject eTimeAndMoneyProject) {
            return new DozerBeanMapper().map(eTimeAndMoneyProject,TimeAndMoneyProject.class);
        }
    };

    public static Function<TimeAndMoneyProject,ETimeAndMoneyProject> entity = new Function<TimeAndMoneyProject, ETimeAndMoneyProject>() {
        @Override
        public ETimeAndMoneyProject apply(TimeAndMoneyProject timeAndMoneyProject) {
            return new DozerBeanMapper().map(timeAndMoneyProject,ETimeAndMoneyProject.class);
        }
    };

}
