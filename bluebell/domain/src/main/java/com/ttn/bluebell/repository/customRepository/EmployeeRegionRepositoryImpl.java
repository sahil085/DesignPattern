package com.ttn.bluebell.repository.customRepository;

import com.ttn.bluebell.domain.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ttnd on 26/10/16.
 */
@Repository
public class EmployeeRegionRepositoryImpl implements RegionRepositoryCustom {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<String> getRegionHeadForRegion(Region region) {

        Map<String, Long> param = new HashMap<>();
        param.put("region", region.getId());
        String sql = "select r.email from EMPLOYEE_REGIONS r, EMPLOYEE_ROLES er where r.email=er.email and er.roles='REGION HEAD' and r.region_id=:region";
        return namedParameterJdbcTemplate.queryForList(sql, param, String.class);
    }

    @Override
    public List<String> getRegionHeadForRegionId(Long regionId) {
        Map<String, Long> param = new HashMap<>();
        param.put("region", regionId);
        String sql = "select r.email from EMPLOYEE_REGIONS r, EMPLOYEE_ROLES er where r.email=er.email and er.roles='REGION HEAD' and r.region_id=:region";
        return namedParameterJdbcTemplate.queryForList(sql, param, String.class);
    }
}
