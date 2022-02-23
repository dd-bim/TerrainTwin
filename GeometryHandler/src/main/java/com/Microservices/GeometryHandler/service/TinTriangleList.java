package com.Microservices.GeometryHandler.service;

import com.Microservices.GeometryHandler.domain.model.IndexedTin;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.Polygon;

public class TinTriangleList {

    public static IndexedTin createIndexedTriangles(GeometryCollection collection) {
        
        CoordinateList usedCoord = new CoordinateList();
        int[] triangles = new int[collection.getNumGeometries() * 3];
        for (int i = 0; i < collection.getNumGeometries(); i++) {
            Polygon polygon = (Polygon) collection.getGeometryN(i);
            Coordinate[] cList = polygon.getCoordinates();
            for (int j = 0; j < 3; j++) {
                Coordinate c = cList[j];
                if (!usedCoord.contains(c)) {
                    usedCoord.add(c);
                }
                int index = usedCoord.indexOf(c);
                triangles[i * 3 + j] = index;
            }

        }

        double[][] points = new double[usedCoord.size()][3];
        for (int k = 0; k < usedCoord.size(); k++) {
            Coordinate c = usedCoord.get(k);
            points[k] = new double[] { c.x, c.y, c.z };
        }

        return new IndexedTin(points, triangles);
    }
}
