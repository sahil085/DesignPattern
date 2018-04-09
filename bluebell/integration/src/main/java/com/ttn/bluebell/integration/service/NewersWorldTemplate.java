package com.ttn.bluebell.integration.service;

import com.ttn.bluebell.durable.model.common.BBResponse;
import com.ttn.bluebell.durable.model.employee.*;
import com.ttn.bluebell.integration.api.NewersWorldOperations;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * Created by praveshsaini on 22/9/16.
 */
@Service
public class NewersWorldTemplate implements NewersWorldOperations{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NewersWorldTemplate.class);
    private static Cache employeeCache = CacheManager.getInstance().getCache("employees");

    @Resource
    private Environment env;

    @Override
    @PostConstruct
    public Map<String,Integer> refreshEmployeesLeaveCounts() {
        String uri = env.getProperty("newers.world.baseURL")+NewersWorldOperations.LEAVES_COUNT_SYNC_API;
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> object = (LinkedHashMap<String, Object>) restTemplate.getForObject(uri,Object.class);
        Map<String,Integer> map = new HashMap<>();
        map = (Map)object.get("data");
        map.forEach((k,v) -> {
                    Employee employee =  (Employee) employeeCache.get(k).getObjectValue();
                    employee.setLeavesCount(v);
                    employeeCache.put(new Element(employee.getEmailAddress(),employee));
                });
        return map;
    }

    @Override
    public String getLeaveSummary(int n,String username) {
        String bbResponse = new String();
        String uri = env.getProperty("newers.world.baseURL")+NewersWorldOperations.LEAVES_SUMMARY_API ;
        Map<String,Object> map = new HashMap<>();
        map.put("n",n);
        map.put("username",username);
        RestTemplate restTemplate = new RestTemplate();
        bbResponse = restTemplate.getForObject(uri,String.class,map);
        return bbResponse;
    }

    @Override
    @PostConstruct
    public void updateLeavesCount() {
        refreshEmployeesLeaveCounts();
    }

}
