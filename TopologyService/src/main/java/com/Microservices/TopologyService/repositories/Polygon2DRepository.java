package com.Microservices.TopologyService.repositories;

import java.util.UUID;

import com.Microservices.TopologyService.schemas.Polygon2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Polygon2DRepository extends CrudRepository<Polygon2D, UUID> {

    // @Query("select id, geometry from terraintwinv2.polygon_2d")
    // String [][] getPolygon2D();

    // @Query("select polygon_id from terraintwinv2.polygon_2d where id = '2330645d-9dde-44d8-adcd-ce43b4573a3f'")
    // Integer getT();
}
