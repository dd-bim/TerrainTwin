package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Polygon;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolygonRepository extends CrudRepository<Polygon, Integer> {

    @Query(value = "select polygon_id, count(*) from terraintwinv2.polygon group by polygon_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPolyDuplicates();
}
