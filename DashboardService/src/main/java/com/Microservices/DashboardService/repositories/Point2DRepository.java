package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Point2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Point2DRepository extends CrudRepository<Point2D, Integer> {

    @Query(value = "select point_id, count(*) from terraintwinv2.point_2d group by point_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPnt2DDuplicates();
}
