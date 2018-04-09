package com.ttn.bluebell.repository;


import com.ttn.bluebell.domain.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findByRegionName(String name);

    Region findByRegionNameAndStatus(String name, Boolean isActive);

    @Query(value = "select region from Region region ")
    List<Region> getAll();

    Region findByExternalId(String externalId);
    Region findById(Long id);
}