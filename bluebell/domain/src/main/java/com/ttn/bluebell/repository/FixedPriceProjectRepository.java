package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.project.EFixedPriceProject;
import com.ttn.bluebell.domain.project.ETimeAndMoneyProject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveshsaini on 22/9/16.
 */
@Repository
public interface FixedPriceProjectRepository extends BillableProjectRepository<EFixedPriceProject> {
    @Query(value = "select etmp from EFixedPriceProject etmp where etmp.region in (?1) and etmp.active = true")
    List<EFixedPriceProject> findProjectByRegionAndActive(ArrayList<String> region);
}
