package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.client.EClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ttnd on 29/9/16.
 */
@Repository
public interface ClientRepository  extends JpaRepository<EClient, Long>{

    EClient findByClientIdAndActiveIsTrue( Long clientId);
    List<EClient> findAllByActiveIsTrueOrderByClientNameAsc();
}
