package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Point;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends CrudRepository<Point, Integer> {

    @Query(value = "select point_id, count(*) from terraintwinv2.points group by point_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPntDuplicates();
}
