package com.Microservices.GeometryHandler.service;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.Polygon;

public class VolumeDifferenzHelper {
    
    // erstellt aus dem Schnittpunkten und Schnittpolygonen zwei neue Polygone
    public Polygon[] createCuttedPolygons(Coordinate[] pList, CoordinateList swapPoints, GeometryFactory factory) {
        // erstellt eine Liste mit der Reihenfolge aller Punkte
        CoordinateList swapPoints2 = new CoordinateList(swapPoints.toCoordinateArray());
        CoordinateList aa = new CoordinateList();
        for (int pc = 0; pc < pList.length - 1; pc++) {
            boolean found = false;
            LineSegment ls = new LineSegment(pList[pc], pList[pc + 1]);
           
            if (!swapPoints2.isEmpty()) {
            for (Coordinate c : swapPoints2) {
                    if (ls.p0.x == c.x && ls.p0.y == c.y) { 
                    aa.add(pList[pc]);
                    swapPoints2.remove(c);
                    found = true;
                    break;
                }else if (ls.p1.x == c.x && ls.p1.y == c.y) { 
                    aa.add(pList[pc]);
                    aa.add(pList[pc + 1]);
                    swapPoints2.remove(c);
                    found = true;
                    pc++;
                    break;
                } else if (ls.distance(c) < 0.0001) {
                    aa.add(pList[pc]);
                    aa.add(c);
                    swapPoints2.remove(c);
                    found = true;
                    break;
                } 
              }  
            }
              if (!found) {
                    aa.add(pList[pc]);
              }         
        }
        aa.add(aa.get(0));

        // teilt die Liste an der Schnittlinie in zwei Listen für neue Polygone
        CoordinateList a1 = new CoordinateList();
        CoordinateList a2 = new CoordinateList(); 
        boolean f = false;
        for (Coordinate c : aa) {
            if (swapPoints.contains(c)) {
                a1.add(c);
                a2.add(c);
                if (!f) {
                    f = true;
                } else {
                    f = false;
                }
            } else {
                if (!f) {
                    a1.add(c);
                } else {
                    a2.add(c);
                }
            }
        }
        if (a1.get(0) != a1.get(a1.size() - 1)) {
            a1.add(a1.get(0));
        }
        if (a2.get(0) != a2.get(a2.size() - 1)) {
            a2.add(a2.get(0));
        }

        Polygon pa1 = factory.createPolygon(a1.toCoordinateArray());
        Polygon pa2 = factory.createPolygon(a2.toCoordinateArray());
        Polygon[] cutPolygone = new Polygon[]{pa1,pa2};

        return cutPolygone;
    }

    // Zerlegt eine Poygonpunktliste in Dreieckespolygone
    public List<CoordinateList> create3EdgePolygons (Coordinate[] pM) {
        List<CoordinateList> pMList = new ArrayList<CoordinateList>();
        if (pM.length == 4) {
            CoordinateList l1 = new CoordinateList(pM);
            pMList.add(l1);
        } else if (pM.length < 4) {
            System.out.println(pM[0] + " " + pM[1] + " " + pM[2]);
            System.out.println("Invalid number of coordinates.");
        } else {
            int redSize = pM.length - 3;
            for (int i = 1; i <= redSize; i++) {
                CoordinateList l1 = new CoordinateList();

                l1.add(pM[0]);
                l1.add(pM[i]);
                l1.add(pM[i+1]);
                l1.add(pM[redSize + 2]);

                pMList.add(l1);
            }

        }

        return pMList;
    }
}
