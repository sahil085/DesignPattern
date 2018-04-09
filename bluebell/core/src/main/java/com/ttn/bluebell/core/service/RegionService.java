package com.ttn.bluebell.core.service;


import com.ttn.bluebell.core.api.RegionOperations;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.region.Region;
import com.ttn.bluebell.durable.model.common.RegionDTO;
import com.ttn.bluebell.durable.model.employee.CoreEntityDTO;
import com.ttn.bluebell.integration.api.SuccessFactorOperations;
import com.ttn.bluebell.repository.ProjectRepository;
import com.ttn.bluebell.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class RegionService implements RegionOperations {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private SuccessFactorOperations successFactorOperations;

    private Map<String, String> regionMap() {
        Map<String, String> regionMap = new HashMap<>();
        regionMap.put("US Central + ANZ", "Spartans (Australia-New Zealand,US Central)");
        regionMap.put("ME", "Cyborgs (Middle East and Africa)");
        regionMap.put("Video", "Titans (SmartTV,Video Ready,HDFC)");
        regionMap.put("EU", "Gladiators (European Union)");
        regionMap.put("SEA", "South East Asia ");
        regionMap.put("Corp", "Ninjas (Global Innovation Hub)");
        regionMap.put("Corporate Marketing", "Ninjas (Global Innovation Hub)");
        regionMap.put("GIH", "Ninjas (Global Innovation Hub)");
        regionMap.put("US East", "Vikings (US East)");
        regionMap.put("US West", "Vanguardians (US West)");
        regionMap.put("US Central", "Spartans (Australia-New Zealand,US Central)");
        regionMap.put("Middle East and Africa", "Cyborgs (Middle East and Africa)");
        regionMap.put("India", "Aryans (India)");
        regionMap.put("VIDEO READY", "Titans (SmartTV,Video Ready,HDFC)");
        regionMap.put("European Union", "Gladiators (European Union)");
        regionMap.put("SmartTV", "Titans (SmartTV,Video Ready,HDFC)");
        regionMap.put("Smart TV", "Titans (SmartTV,Video Ready,HDFC)");
        regionMap.put("HDFC", "Titans (SmartTV,Video Ready,HDFC)");
        regionMap.put("Global Innovation Hub", "Ninjas (Global Innovation Hub)");

        return regionMap;

    }

    private void syncRegionData() {
        List<EProject> projects = projectRepository.findAll();
        List<EProject> projectsList = new ArrayList<>();
        Map<String, String> region = regionMap();
        projects.forEach(project -> {
            if (region.containsKey(project.getRegion())) {
                String projectRegion = region.get(project.getRegion());
                project.setRegionvalue(findRegion(projectRegion));
                projectsList.add(project);
            }
        });
        projectRepository.save(projectsList);
    }

    @Override
    public void refreshRegionData() {
        List<RegionDTO> origRegions = fetchAllRegions();
        List<Region> updateRegionList = new ArrayList<>();
        origRegions.forEach(region -> {
            Region currentRegion = regionRepository.findByExternalId(region.getCode());
            if (currentRegion != null) {
                currentRegion.setRegionName(region.getName());
            } else {
                currentRegion = new Region(region.getName(), region.getCode(), region.getStatus());
            }
            updateRegionList.add(currentRegion);
        });
        regionRepository.save(updateRegionList);
//        syncRegionData();
    }

    @Override
    public List<RegionDTO> fetchAllRegions() {
        return successFactorOperations.fetchAllRegions();
    }

    public Region findRegion(String name) {
        Region region = null;
        region = regionRepository.findByRegionNameAndStatus(name, true);
        if (region == null)
            region = regionRepository.findByRegionName(name);
        return region;
    }


    public Region updateRegion(CoreEntityDTO coreEntityDTO) {
        Region region = null;
        region = regionRepository.findByExternalId(coreEntityDTO.getSfCode());
        if (region == null) {
            region = new Region();
            region.setExternalId(coreEntityDTO.getSfCode());
            region.setRegionName(coreEntityDTO.getName());
            region.setStatus(true);
        }
        else
            region.setRegionName(coreEntityDTO.getName());
            region=regionRepository.save(region);

        return region;
    }
}

