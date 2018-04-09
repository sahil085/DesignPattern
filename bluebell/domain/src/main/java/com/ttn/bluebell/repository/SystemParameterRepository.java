package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.common.ESystemParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ttnd on 25/10/16.
 */
public interface SystemParameterRepository extends JpaRepository<ESystemParameter,Long> {
    List<ESystemParameter> findByType(String type);
}
