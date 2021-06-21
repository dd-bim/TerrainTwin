package com.Microservices.TopologyService.repositories;

import java.util.UUID;

import com.Microservices.TopologyService.schemas.Point2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Point2DRepository extends CrudRepository<Point2D, UUID> {

    // @Query("select a.id, b.id from terraintwinv2.point_2d a, terraintwinv2.polygon_2d b where ST_Intersects(a.geometry,b.geometry)")
    // // HashMap<UUID, UUID> getIntersects();
    // String [][] getIntersects();

    // @Query("select id from terraintwinv2.point_2d where ST_Intersects(geometry, :geom2)")
    // @Query("select id from terraintwinv2.point_2d where ST_Intersects(geometry, :geom2) = true")
    // String [] getIntersects(@Param("geom2") Geometry geom2);

    @Query(value = "select cast(id as varchar) from terraintwinv2.point_2d where ST_Intersects(geometry, geometry)", nativeQuery = true)
    UUID [] getIntersects();

    // @Query("select point_id from terraintwinv2.point_2d where id = 'ad6ad4ea-ddf8-4a08-80dd-f23bc101adc7'")
    // Integer getT();

}
