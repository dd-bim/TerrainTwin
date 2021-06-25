package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Polygon3D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Polygon3DRepository extends PostgresTableRepository<Polygon3D> {

    @Query(value = "select origin_id, count(*) from terraintwinv2.polygon_3d group by origin_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPoly3DDuplicates();
}
