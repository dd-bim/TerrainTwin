package com.Microservices.DashboardService.repositories;

import java.util.UUID;

import com.Microservices.DashboardService.schemas.Polygon3D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Polygon3DRepository extends CrudRepository<Polygon3D, UUID> {

    @Query(value = "select polygon_id, count(*) from terraintwinv2.polygon_3d group by polygon_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPoly3DDuplicates();
}
