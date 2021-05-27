package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Polygon2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Polygon2DRepository extends CrudRepository<Polygon2D, Integer> {

    @Query(value = "select polygon_id, count(*) from terraintwinv2.polygon_2d group by polygon_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPoly2DDuplicates();
}
