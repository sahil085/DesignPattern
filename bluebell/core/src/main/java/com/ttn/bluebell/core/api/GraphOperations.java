package com.ttn.bluebell.core.api;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ttn on 8/11/16.
 */
public interface GraphOperations {
    Map<String, Integer> upcomingDeallocations(String days, ArrayList<String> regions);

    Map<String, Map<String, Map<String, Integer>>> regionCompetencyBillability(String projectType, String graphType, ArrayList<String> filterList, ArrayList<String> regions);
}
