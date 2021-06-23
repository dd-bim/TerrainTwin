package com.Microservices.TopologyService.repositories;

import java.util.UUID;

import com.Microservices.TopologyService.schemas.Line2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Line2DRepository extends CrudRepository<Line2D, UUID> {

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.line_2d b", nativeQuery = true)
    String [][] relateLine2d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.line_3d b", nativeQuery = true)
    String [][] relateLine3d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.polygon_2d b", nativeQuery = true)
    String [][] relatePolygon2d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.polygon_3d b", nativeQuery = true)
    String [][] relatePolygon3d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.solid b", nativeQuery = true)
    String [][] relateSolid();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.tin_id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.dtm_tin b", nativeQuery = true)
    String [][] relateTIN();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.bl_id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.dtm_breaklines b", nativeQuery = true)
    String [][] relateBreaklines();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.embarkment_id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.dtm_embarkment b", nativeQuery = true)
    String [][] relateEbmarkment();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.dtm_specialpoints b", nativeQuery = true)
    String [][] relateSpecialPoints();
}

