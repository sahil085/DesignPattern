package com.ttn.bluebell.core.api;

import com.ttn.bluebell.durable.model.common.SystemParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by praveshsaini on 22/9/16.
 */
public interface CommonDataOperations {
    List<String> findAllBusinessUnits();

    Set<String> findAllRegions();

    Set<String> fetchAllRegions();

    Set<String> findAllActiveRegions();

    Set<String> findAllCompetencies();

    List<String> findAllEmployeeTitles();

    List<SystemParameter> findEmployeeExperience(String type);

    List<String> findProjectList(String type, ArrayList<String> regions);

    void uploadReviewerData();

    Map<String, Map<String, Object>> employeeOnBench(int durationInDays);
}
