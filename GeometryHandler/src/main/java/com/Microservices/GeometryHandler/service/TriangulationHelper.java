package com.Microservices.GeometryHandler.service;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.algorithm.locate.IndexedPointInAreaLocator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.ConstraintEnforcementException;
import org.locationtech.jts.triangulate.quadedge.Vertex;
import org.springframework.stereotype.Service;

@Service
public class TriangulationHelper {

    public CoordinateList getCoordInBoundary(GeometryCollection collection, Geometry boundary) {
        CoordinateList list = new CoordinateList();
        IndexedPointInAreaLocator locator = new IndexedPointInAreaLocator(boundary);

        for (Coordinate c : collection.getCoordinates()) {
            int o = locator.locate(c);
            if (o == 0 || o == 1) {
                list.add(c);
            }
        }
        return list;
    }

    public GeometryCollection triangulate(CoordinateList cList, Geometry boundary, GeometryFactory factory) {

        Geometry tinGeo = factory.createMultiPointFromCoords(cList.toCoordinateArray());
        GeometryCollection triangulation = new GeometryCollection(new Geometry[0], factory);

        try {
            ConformingDelaunayTriangulationBuilder builder = new ConformingDelaunayTriangulationBuilder();
            builder.setSites(tinGeo);
            builder.setTolerance(0.0001);
            builder.setConstraints(boundary);
            triangulation = (GeometryCollection) builder.getTriangles(factory);
        } catch (ConstraintEnforcementException ce) {
            System.out.println(ce.getMessage());
            System.out.println("Constraints coudn't be used for triangulation. Trying it without constraints:");
            try {
                ConformingDelaunayTriangulationBuilder builder = new ConformingDelaunayTriangulationBuilder();
                builder.setSites(tinGeo);
                builder.setTolerance(0.0001);
                triangulation = (GeometryCollection) builder.getTriangles(factory);
                System.out.println("Triangulation without constraints was successful.");
            } catch (Exception e) {
                System.out.println("Triangulation without constraints wasn't successful:");
                System.out.println(e.getMessage());
            }
        }

        return triangulation;
    }

    public List<Polygon> cutPolygonListByBoundary(GeometryCollection collection, LinearRing boundary, CoordinateList newPointList,
            GeometryFactory factory) {
        ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
        Coordinate[] cLine = boundary.getCoordinates();
        for (int i = 0; i < cLine.length - 1; i++) {
            LineSegment segment = new LineSegment(cLine[i], cLine[i + 1]);
            lineSegments.add(segment);
        }
        IndexedPointInAreaLocator locator = new IndexedPointInAreaLocator(boundary);

        List<Polygon> polygonList = new ArrayList<Polygon>();
        ArrayList<Coordinate> nanPoints = new ArrayList<Coordinate>();
        ArrayList<Double> newZ = new ArrayList<Double>();

        for (int i = 0; i < collection.getNumGeometries(); i++) {
            Polygon x = (Polygon) collection.getGeometryN(i);
            int o = locator.locate(x.getCentroid().getCoordinate());
            if (o == 0) {
                Coordinate[] xx = x.getCoordinates();
                for (Coordinate c : xx) {
                    if (newPointList.contains(c)) {
                        if (nanPoints.contains(c)) {
                            c.setZ(newZ.get(nanPoints.indexOf(c)));
                        } else {
                        double z = Double.NaN;
                        double d = 0.0001;
                        for (LineSegment line : lineSegments) {
                            double dist = line.distance(c);
                            if (dist < d) {
                                double zValue = Vertex.interpolateZ(c, line.p0, line.p1);
                                z = zValue;
                                d = dist;
                            }
                        }
                        if (!Double.isNaN(z)) {
                            c.setZ(z);
                        } else {
                            System.out.println("No z value for " + c.toString());
                        }
                        nanPoints.add(c);
                        newZ.add(z);
                    }
                }
                }
                CoordinateSequence cso = new CoordinateArraySequence(xx);
                LinearRing lxx = new LinearRing(cso, factory);
                Polygon zz = new Polygon(lxx, null, factory);
                polygonList.add(zz);
            }
        }

        return polygonList;
    }

    public List<Polygon> interpolateNaNValues(GeometryCollection triangles, ArrayList<LineSegment> lineSegments, CoordinateList newPointList, boolean breaklines,
            GeometryFactory factory) {
        List<Polygon> polygonList = new ArrayList<Polygon>();
        ArrayList<Coordinate> nanPoints = new ArrayList<Coordinate>();
        ArrayList<Double> newZ = new ArrayList<Double>();

        for (int i = 0; i < triangles.getNumGeometries(); i++) {
            Polygon x = (Polygon) triangles.getGeometryN(i);
            Coordinate[] xx = x.getCoordinates();
            for (Coordinate c : xx) {
                if (breaklines && newPointList.contains(c)) {
                    if (nanPoints.contains(c)) {
                        c.setZ(newZ.get(nanPoints.indexOf(c)));
                    } else {
                    double z = Double.NaN;
                    double d = 0.0001;
                    for (LineSegment line : lineSegments) {
                        double dist = line.distance(c);
                        if (dist < d) {
                            double zValue = Vertex.interpolateZ(c, line.p0, line.p1);
                            z = zValue;
                            d = dist;
                        }
                    }
                    if (!Double.isNaN(z)) {
                        c.setZ(z);
                    } else {
                        System.out.println("No z value for " + c.toString());
                    }
                    nanPoints.add(c);
                    newZ.add(z);
                }
            }
            }
            
            CoordinateSequence cso = new CoordinateArraySequence(xx);
            LinearRing lxx = new LinearRing(cso, factory);
            Polygon zz = new Polygon(lxx, null, factory);
            polygonList.add(zz);
            }
        return polygonList;
     }
    
}
