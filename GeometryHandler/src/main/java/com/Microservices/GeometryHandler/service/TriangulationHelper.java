package com.Microservices.GeometryHandler.service;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.algorithm.locate.IndexedPointInAreaLocator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.ConstraintEnforcementException;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.quadedge.Vertex;
import org.springframework.stereotype.Service;

@Service
public class TriangulationHelper {

    public CoordinateList getPointsFromList(List<List<Double>> list) {
        CoordinateList points = new CoordinateList();
        for (List<Double> point : list) {
            Coordinate p;
            try {
                p = new Coordinate(point.get(0), point.get(1), point.get(2));
            } catch (Exception e) {
                p = new Coordinate(point.get(0), point.get(1));
            }
            points.add(p);
        }
        return points;
    }

    public CoordinateList getCoordInBoundary(GeometryCollection collection, Geometry boundary) {
        CoordinateList list = new CoordinateList();
        IndexedPointInAreaLocator locator = new IndexedPointInAreaLocator(boundary);

        for (Coordinate c : collection.getCoordinates()) {
            int o = locator.locate(c);
            if (o == 0 || o == 1) {
                list.add(c, false);
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
            builder.setTolerance(0.001);
            if (boundary != null)
                builder.setConstraints(boundary);
            triangulation = (GeometryCollection) builder.getTriangles(factory);
        } catch (ConstraintEnforcementException ce) {
            System.out.println(ce.getMessage());
            System.out.println("Constraints coudn't be used for triangulation. Trying it without constraints:");
            try {
                ConformingDelaunayTriangulationBuilder builder = new ConformingDelaunayTriangulationBuilder();
                builder.setSites(tinGeo);
                builder.setTolerance(0.001);
                triangulation = (GeometryCollection) builder.getTriangles(factory);
                System.out.println("Triangulation without constraints was successful.");
            } catch (Exception e) {
                System.out.println("Triangulation without constraints wasn't successful:");
                System.out.println(e.getMessage());
            }
        }

        return triangulation;
    }

    public GeometryCollection triangulateDT(CoordinateList cList, GeometryFactory factory) {

        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
        builder.setSites(cList);
        builder.setTolerance(0.001);
        GeometryCollection triangulation = (GeometryCollection) builder.getTriangles(factory);

        return triangulation;
    }

    public GeometryCollection cutCollectionByBoundary(GeometryCollection collection, LinearRing boundary,
            GeometryFactory factory) {
        IndexedPointInAreaLocator locator = new IndexedPointInAreaLocator(boundary);
        List<Geometry> geom = new ArrayList<Geometry>();
        for (int i = 0; i < collection.getNumGeometries(); i++) {
            Polygon x = (Polygon) collection.getGeometryN(i);
            int o = locator.locate(x.getCentroid().getCoordinate());
            if (o == 0) {
                geom.add(x);
            }
        }
        Geometry[] geometry = new Geometry[geom.size()];
        geom.toArray(geometry);
        GeometryCollection col = factory.createGeometryCollection(geometry);
        return col;
    }

    public Polygon[] interpolateNaNValues(GeometryCollection triangles,
            ArrayList<LineSegment> lineSegments,
            CoordinateList newPointList, boolean breaklines,
            GeometryFactory factory) {
        Polygon[] polygonList = new Polygon[triangles.getNumGeometries()];
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
                                z = Math.round(zValue * 1000.0) / 1000.0;
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
            Polygon zz = factory.createPolygon(xx);
            polygonList[i] = zz;
        }
        return polygonList;
    }

    public CoordinateList findPointsInOriginalTin(Coordinate[] xx, CoordinateList originalPoints, Tin tin) {

        CoordinateList list = new CoordinateList();
        for (Coordinate c : xx) {
            boolean found = false;
            for (int j = 0; j < originalPoints.size(); j++) {
                if (c.x == originalPoints.get(j).x && c.y == originalPoints.get(j).y) {
                    list.add(originalPoints.get(j));
                    found = true;
                    break;
                }
            }
            if (!found) {
                double z = tin.GetTriangle(new double[] { c.x, c.y }).DoubleValue;
                list.add(new Coordinate(c.x, c.y, z));
            }
        }

        return list;
    }

}

