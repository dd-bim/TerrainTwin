package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Line3D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Line3DRepository extends PostgresTableRepository<Line3D> {

    @Query(value = "select origin_id, count(*) from terraintwinv2.line_3d group by origin_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getLine3DDuplicates();
}

