package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Point3D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Point3DRepository extends PostgresTableRepository<Point3D> {

    @Query(value = "select origin_id, count(*) from terraintwinv2.point_3d group by origin_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPnt3DDuplicates();
}
