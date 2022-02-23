package com.Microservices.GeometryHandler.service;

import java.util.ArrayList;
import java.util.List;

import com.Microservices.GeometryHandler.domain.model.IndexedTin;
import com.Microservices.GeometryHandler.domain.model.Volume;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class VolumeCalculation {

    public Volume calculateVolume(GeometryCollection firstTin, GeometryCollection secondTin, LinearRing boundary,
            int epsg) {

        TriangulationHelper helper = new TriangulationHelper();
        VolumeDifferenzHelper volHelper = new VolumeDifferenzHelper();
        double scale = 1000.0;
        PrecisionModel precisionModel = new PrecisionModel(scale);
        GeometryFactory factory = new GeometryFactory(precisionModel, epsg);

        // recalculate TINs to avoid false triangles and duplicate points, which affect the volume 
        firstTin = helper.triangulateDT(new CoordinateList(firstTin.getCoordinates()), factory);
        secondTin = helper.triangulateDT(new CoordinateList(secondTin.getCoordinates()), factory);

        // create indexed TINs
        IndexedTin fTinIndexed = TinTriangleList.createIndexedTriangles(firstTin);
        Tin fTin = Tin.CreateTin(fTinIndexed.Points, fTinIndexed.Triangles, 0);

        IndexedTin sTinIndexed = TinTriangleList.createIndexedTriangles(secondTin);
        Tin sTin = Tin.CreateTin(sTinIndexed.Points, sTinIndexed.Triangles, 0);

        // cut collections by boundary, if given
        if (boundary != null) {
            CoordinateList cuttedfTinList = helper.getCoordInBoundary(firstTin, boundary);
            CoordinateList cuttedsTinList = helper.getCoordInBoundary(secondTin, boundary);

            firstTin = helper.triangulate(cuttedfTinList, boundary, factory);
            secondTin = helper.triangulate(cuttedsTinList, boundary, factory);

            firstTin = helper.cutCollectionByBoundary(firstTin, boundary, factory);
            secondTin = helper.cutCollectionByBoundary(secondTin, boundary, factory);
        }

        // get polygon lists from collections
        List<Polygon> fTinPolygons = new ArrayList<Polygon>(), sTinPolygons = new ArrayList<Polygon>();

        for (int f = 0; f < firstTin.getNumGeometries(); f++) {
            fTinPolygons.add((Polygon) firstTin.getGeometryN(f));
        }
        for (int s = 0; s < secondTin.getNumGeometries(); s++) {
            sTinPolygons.add((Polygon) secondTin.getGeometryN(s));
        }

        // initialize volume counter
        double positiveVolume = 0, negativeVolume = 0;

        // check each polygon pair, if they overlap
        for (int a = 0; a < fTinPolygons.size(); a++) {
            Polygon first = fTinPolygons.get(a);

            for (int b = 0; b < sTinPolygons.size(); b++) {
                Polygon second = sTinPolygons.get(b);

                // get intersection, if exists
                if (first.intersects(second) && !first.touches(second)) {
                    Geometry inter = first.intersection(second);

                    // result is a polygon
                    if (inter instanceof Polygon) {
                        Polygon m = (Polygon) inter;

                        // split polygon into triangles
                        List<CoordinateList> pMList = volHelper.create3EdgePolygons(m.getCoordinates());

                        for (CoordinateList pM1 : pMList) {
                            Coordinate[] xx = pM1.toCoordinateArray();

                            // project cutted polygons on indext TINs to get z values from input TINs
                            CoordinateList listP1 = helper.findPointsInOriginalTin(xx,
                                    new CoordinateList(), fTin);
                            Polygon p1 = factory.createPolygon(listP1.toCoordinateArray());

                            CoordinateList listP2 = helper.findPointsInOriginalTin(xx,
                                    new CoordinateList(), sTin);
                            // Polygon p2 = factory.createPolygon(listP2.toCoordinateArray());

                            // check, how many points from first TIN lie over same points in second TIN
                            // next steps depend on results
                            // Addition of all z-distances between xy-identical points for calculation of
                            // the volume of unique polygons (only positive or negative volume)
                            boolean positive = false, negative = false;
                            double delta = 0;
                            for (int c = 0; c < listP1.size() - 1; c++) {
                                double dist = listP1.get(c).z - listP2.get(c).z;
                                if (dist > 0.0001) {
                                    positive = true;
                                } else if (dist < -0.0001) {
                                    negative = true;
                                }

                                delta += Math.abs(dist);
                            }

                            // calculate volume
                            double vol = p1.getArea() * delta / 3;

                            // 1. if first polygon over second, negtive volume
                            if (positive && !negative) {
                                if (!Double.isNaN(vol)) {
                                    negativeVolume += vol;
                                }
                                // 2. if second polygon over first, positive volume
                            } else if (negative && !positive) {
                                if (!Double.isNaN(vol)) {
                                    positiveVolume += vol;
                                }
                                // 3. polygons cut each other, positive and negative volume
                            } else if (positive && negative) {

                                // find cutting line, should always be two points
                                CoordinateList swapPoints = new CoordinateList();
                                for (int g = 0; g < listP1.size() - 1; g++) {
                                    Coordinate swPoint = null;
                                    Coordinate pO1 = listP1.get(g);
                                    Coordinate pO2 = listP1.get(g + 1);
                                    Coordinate pU1 = listP2.get(g);
                                    Coordinate pU2 = listP2.get(g + 1);

                                    // from the top boundaries of the polygones are exatly the same, cutting line is not visible
                                    // look from the side, to finde crossing lines
                                    // while x values of line are different, change y and z
                                    if (pO1.x != pO2.x) {
                                        pO1 = new Coordinate(pO1.x, pO1.z, pO1.y);
                                        pO2 = new Coordinate(pO2.x, pO2.z, pO2.y);
                                        LineSegment lineSegmentO = new LineSegment(pO1, pO2);

                                        pU1 = new Coordinate(pU1.x, pU1.z, pU1.y);
                                        pU2 = new Coordinate(pU2.x, pU2.z, pU2.y);
                                        LineSegment lineSegmentU = new LineSegment(pU1, pU2);

                                        Coordinate is = lineSegmentO.intersection(lineSegmentU);
                                        if (is != null) {
                                            swPoint = new Coordinate(is.x, is.z, is.y);
                                        }
                                        // if x values are the same, change x and y
                                    } else {
                                        pO1 = new Coordinate(pO1.z, pO1.y, pO1.x);
                                        pO2 = new Coordinate(pO2.z, pO2.y, pO2.x);
                                        LineSegment lineSegmentO = new LineSegment(pO1, pO2);

                                        pU1 = new Coordinate(pU1.z, pU1.y, pU1.x);
                                        pU2 = new Coordinate(pU2.z, pU2.y, pU2.x);
                                        LineSegment lineSegmentU = new LineSegment(pU1, pU2);

                                        Coordinate is = lineSegmentO.intersection(lineSegmentU);

                                        if (is != null) {
                                            swPoint = new Coordinate(is.z, is.y, is.x);
                                        }
                                    }

                                    // if polygon points are part of cutting line, use only one time 
                                    if (swPoint != null && !swapPoints.contains(swPoint)) {
                                        swapPoints.add(swPoint);
                                    }

                                }

                                // create new polygons with cutting line
                                Polygon[] fCutPolygone = volHelper.createCuttedPolygons(listP1.toCoordinateArray(),
                                        swapPoints, factory);
                                Polygon[] sCutPolygone = volHelper.createCuttedPolygons(listP2.toCoordinateArray(),
                                        swapPoints, factory);

                                // splitt new polygons into triangles
                                List<CoordinateList> coListF = new ArrayList<CoordinateList>(),
                                        coListS = new ArrayList<CoordinateList>();
                                for (Polygon fCut : fCutPolygone) {
                                    coListF.addAll(volHelper.create3EdgePolygons(fCut.getCoordinates()));
                                }
                                for (Polygon sCut : sCutPolygone) {
                                    coListS.addAll(volHelper.create3EdgePolygons(sCut.getCoordinates()));
                                }

                                // for each polygone calculate volume
                                for (int i = 0; i < coListF.size(); i++) {
                                    CoordinateList oCutCoord = coListF.get(i);
                                    CoordinateList uCutCoord = coListS.get(i);
                                    Polygon pF = factory.createPolygon(oCutCoord.toCoordinateArray());

                                    // get centroid to check, which polygon is higher
                                    Point centroid = pF.getCentroid();
                                    double centroidFZ = fTin.GetTriangle(
                                            new double[] { centroid.getX(), centroid.getY() }).DoubleValue;

                                    double centroidSZ = sTin.GetTriangle(
                                            new double[] { centroid.getX(), centroid.getY() }).DoubleValue;

                                    // calculate volume
                                    double deltaS = 0;
                                    for (int c = 0; c < 3; c++) {
                                        deltaS += Math.abs(oCutCoord.get(c).z - uCutCoord.get(c).z);
                                    }

                                    double volume = pF.getArea() * deltaS / 3;

                                    // decide, if positive or negative volume
                                    if (!Double.isNaN(volume)) {
                                        if (centroidFZ > centroidSZ) {
                                            negativeVolume += volume;
                                        } else {
                                            positiveVolume += volume;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Volume volume = new Volume(Math.round(positiveVolume * scale) / scale, Math.round(negativeVolume * scale) / scale);
        return volume;

    }
}