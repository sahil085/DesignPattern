package com.ttn.bluebell.rest.graph;

import com.ttn.bluebell.core.api.GraphOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ttn on 3/11/16.
 */

@RestController
@RequestMapping("/reports")
public class GraphResource {

    @Autowired
    private GraphOperations graphOperations;

    @RequestMapping(value = "/upcomingDe-allocation",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Integer> upcomingDeallocations(@RequestBody LinkedHashMap<String,Object> upcomingDeallocation) {
        return graphOperations.upcomingDeallocations((String) upcomingDeallocation.get("days"),(ArrayList<String>) upcomingDeallocation.get("regions"));
    }

    @RequestMapping(value = "/region-competency-billable",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String,Map<String,Map<String, Integer>>> regionCompetencyBillability(@RequestBody LinkedHashMap<String,Object> upcomingDeallocation) {
        return graphOperations.regionCompetencyBillability((String)upcomingDeallocation.get("projectType"),(String)upcomingDeallocation.get("graphType"),(ArrayList<String>)
                upcomingDeallocation.get("filterList"),(ArrayList<String>) upcomingDeallocation.get("regions"));
    }
}
