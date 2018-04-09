package com.ttn.bluebell.integration.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttn.bluebell.durable.model.common.BBResponse;
import com.ttn.bluebell.durable.model.employee.ProjectDetailsDTO;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by praveshsaini on 22/9/16.
 */
public interface NewersWorldOperations {


    public static String LEAVES_COUNT_SYNC_API = "/util/getNextnDaysLeavesCount?n=90";
    public static String LEAVES_SUMMARY_API = "/util/getNextnDaysAttendance?n={n}&&username={username}";

    Map<String,Integer> refreshEmployeesLeaveCounts();

    String getLeaveSummary(int n,String username);

    void updateLeavesCount();
   }
