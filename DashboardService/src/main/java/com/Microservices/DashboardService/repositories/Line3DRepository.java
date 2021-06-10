package com.Microservices.DashboardService.repositories;

import java.util.UUID;

import com.Microservices.DashboardService.schemas.Line3D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Line3DRepository extends CrudRepository<Line3D, UUID> {

    @Query(value = "select line_id, count(*) from terraintwinv2.line_3d group by line_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getLine3DDuplicates();
}

