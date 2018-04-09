package com.ttn.bluebell.repository.customRepository;

import com.ttn.bluebell.domain.region.Region;

import java.util.List;

/**
 * Created by ttnd on 26/10/16.
 */
public interface RegionRepositoryCustom {

    List<String> getRegionHeadForRegion(Region region);
    List<String> getRegionHeadForRegionId(Long regionId);
}
