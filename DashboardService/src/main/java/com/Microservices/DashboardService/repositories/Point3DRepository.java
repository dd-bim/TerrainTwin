package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Point3D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Point3DRepository extends CrudRepository<Point3D, Integer> {

    @Query(value = "select point_id, count(*) from terraintwinv2.point_3d group by point_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPnt3DDuplicates();
}
