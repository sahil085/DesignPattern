package com.ttn.bluebell.core.api;


import com.ttn.bluebell.durable.model.common.RegionDTO;

import java.util.List;

public interface RegionOperations {
    List<RegionDTO> fetchAllRegions();

    void refreshRegionData();
}
