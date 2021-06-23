package com.Microservices.TopologyService.repositories;

import java.util.UUID;

import com.Microservices.TopologyService.schemas.Point2D;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Point2DRepository extends CrudRepository<Point2D, UUID> {

    // @Query(value = "select cast(a.id as varchar) as pointida, cast(b.id as varchar) as pointidb, ST_AsEWKT(b.geometry) from #{#entityName} a,  #{#entityName} b where ST_Intersects(a.geometry, b.geometry)", nativeQuery = true)
    // String [][] getIntersectWithSelf();

    // @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_AsEWKT(b.geometry) from #{#entityName} a, terraintwinv2.polygon_2d b where ST_Intersects(a.geometry,b.geometry)", nativeQuery = true)
    // String [][] getIntersects();

    // @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_AsEWKT(b.geometry) from #{#entityName} a, terraintwinv2.polygon_2d b where ST_Intersects(a.geometry,b.geometry)", nativeQuery = true)
    // String [][] getIntersectWithPoly2D();

    // @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_AsEWKT(b.geometry) from #{#entityName} a, terraintwinv2.polygon_3d b where ST_Intersects(a.geometry,b.geometry)", nativeQuery = true)
    // String [][] getIntersectWithPoly3D();

    // @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_AsEWKT(b.geometry) from #{#entityName} a, terraintwinv2.point_3d b where ST_Intersects(a.geometry,b.geometry)", nativeQuery = true)
    // String [][] getIntersectWithPoint3D();

    // @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_AsEWKT(b.geometry) from #{#entityName} a, terraintwinv2.line_2d b where ST_Intersects(a.geometry,b.geometry)", nativeQuery = true)
    // String [][] getIntersectWithLine2D();

    // @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_AsEWKT(b.geometry) from #{#entityName} a, terraintwinv2.line_3d b where ST_Intersects(a.geometry,b.geometry)", nativeQuery = true)
    // String [][] getIntersectWithLine3D();

    // @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_AsEWKT(b.geometry) from #{#entityName} a, terraintwinv2.solid b where ST_Intersects(a.geometry,b.geometry)", nativeQuery = true)
    // String [][] getIntersectWithSolid();




    // @Query(value = "select count(*) from #{#entityName} where id = cast(?1 as uuid) and ST_CoveredBy(geometry, ?2)", nativeQuery = true)
    // Integer coveredBy(String aid, String geom);

    // @Query(value = "select count(*) from #{#entityName} where id = cast(?1 as uuid) and ST_Covers(geometry, ?2)", nativeQuery = true)
    // Integer covers(String aid, String geom);

    // @Query(value = "select count(*) from #{#entityName} where id = cast(?1 as uuid) and ST_Overlaps(geometry, ?2)", nativeQuery = true)
    // Integer overlaps(String aid, String geom);

    // @Query(value = "select count(*) from #{#entityName} where id = cast(?1 as uuid) and ST_Crosses(geometry, ?2)", nativeQuery = true)
    // Integer crosses(String aid, String geom);

    // @Query(value = "select count(*) from #{#entityName} where id = cast(?1 as uuid) and ST_Touches(geometry, ?2)", nativeQuery = true)
    // Integer touches(String aid, String geom);
    
    // @Query(value = "select count(*) from #{#entityName} where id = cast(?1 as uuid) and ST_Equals(geometry, ?2)", nativeQuery = true)
    // Integer equals(String aid, String geom);

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, #{#entityName} b", nativeQuery = true)
    String [][] relatePoint2d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.point_3d b", nativeQuery = true)
    String [][] relatePoint3d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.line_2d b", nativeQuery = true)
    String [][] relateLine2d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.line_3d b", nativeQuery = true)
    String [][] relateLine3d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.polygon_2d b", nativeQuery = true)
    String [][] relatePolygon2d();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.polygon_3d b", nativeQuery = true)
    String [][] relatePolygon3d();

    // @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.solid b", nativeQuery = true)
    // String [][] relateSolid();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.tin_id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.dtm_tin b", nativeQuery = true)
    String [][] relateTIN();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.bl_id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.dtm_breaklines b", nativeQuery = true)
    String [][] relateBreaklines();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.embarkment_id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.dtm_embarkment b", nativeQuery = true)
    String [][] relateEbmarkment();

    @Query(value = "select cast(a.id as varchar) as pointid, cast(b.id as varchar) as polyid, ST_Dimension(a.geometry) as dimA, ST_Dimension(b.geometry) as dimB, ST_Relate(a.geometry, b.geometry) from #{#entityName} a, terraintwinv2.dtm_specialpoints b", nativeQuery = true)
    String [][] relateSpecialPoints();
}
