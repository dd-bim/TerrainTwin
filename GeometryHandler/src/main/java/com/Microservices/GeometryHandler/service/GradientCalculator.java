package com.Microservices.GeometryHandler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.Microservices.GeometryHandler.domain.model.GradientInfos;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class GradientCalculator {

    public GradientInfos getGradientInfos(GeometryCollection coll, double[] point, double radius) {

        double scale = 1000.0;
        PrecisionModel precisionModel = new PrecisionModel(scale);
        GeometryFactory factory = new GeometryFactory(precisionModel, coll.getSRID());

        // create polygon list from collection
        Polygon[] polyList = new Polygon[coll.getNumGeometries()];
        for (int a = 0; a < coll.getNumGeometries(); a++) {
            polyList[a] = (Polygon) coll.getGeometryN(a);
        }

        // create point and make buffer polygon by radius
        Point p = factory.createPoint(new Coordinate(point[0], point[1]));
        Polygon buffer = (Polygon) p.buffer(radius,16);

        // find all polygons intersecting with buffer and calculate gradient
        List<Double> gradientList = new ArrayList<Double>();
        double sum = 0.0;
        for (Polygon po : polyList) {
            if( po.intersects(buffer) && !po.touches(buffer) ) {
                double gradient = calcGradient(new CoordinateList(po.getCoordinates()));
                gradientList.add(gradient);
                sum += gradient;
            }
        }

        // sort gradient values by ascending order
        Collections.sort(gradientList);

        // calculate gradient infos
        GradientInfos gradientInfos = new GradientInfos();
        int c = gradientList.size();
        gradientInfos.setFaceNumber(c);
        gradientInfos.setMin(gradientList.get(0));
        gradientInfos.setMax(gradientList.get(c - 1));
        gradientInfos.setAverage(Math.round((sum / c) * 10.0) / 10.0);
        gradientInfos.setMedian(gradientList.get(Math.floorDiv(c, 2) - 1)); // bei ungerader Zahl Wert vor Hälfte

        return gradientInfos;
    }

    // calculate gradient
    public double calcGradient(CoordinateList list) {

        // calculate normal vector of surface
        double vec1x = list.get(1).x - list.get(0).x;
        double vec1y = list.get(1).y - list.get(0).y;
        double vec1z = list.get(1).z - list.get(0).z;

        double vec2x = list.get(2).x - list.get(1).x;
        double vec2y = list.get(2).y - list.get(1).y;
        double vec2z = list.get(2).z - list.get(1).z;

        double[] n = new double[3];
        n[0] = vec1y * vec2z - vec1z * vec2y;
        n[1] = vec1z * vec2x - vec1x * vec2z;
        n[2] = vec1x * vec2y - vec1y * vec2x;

        // calculate gradient as angle between normal and z-axis
        double scalar = n[0] * 0 + n[1] * 0 + n[2] * 1;
        double lnormal = Math.sqrt(Math.pow(n[0], 2) + Math.pow(n[1],2) + Math.pow(n[2],2));
        double quotient = scalar / (lnormal * 1);
        double gradient = Math.toDegrees(Math.acos(quotient));

        return Math.round(gradient * 10.0) / 10.0;
        
    }
}