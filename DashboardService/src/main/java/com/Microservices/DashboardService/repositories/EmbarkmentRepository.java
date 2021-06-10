package com.Microservices.DashboardService.repositories;

import java.util.UUID;

import com.Microservices.DashboardService.schemas.Embarkment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbarkmentRepository extends CrudRepository<Embarkment, UUID> {

    // @Query(value = "select embarkment_id, count(*) from terraintwinv2.dtm_embarkment group by embarkment_id, tin_id, geometry having count(*) > 1", nativeQuery = true)
    // Integer[][] getEmbDuplicates();
}
