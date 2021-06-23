package com.Microservices.TopologyService.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.Microservices.TopologyService.repositories.Line2DRepository;
import com.Microservices.TopologyService.repositories.Line3DRepository;
import com.Microservices.TopologyService.repositories.Point2DRepository;
import com.Microservices.TopologyService.repositories.Point3DRepository;
import com.Microservices.TopologyService.repositories.Polygon2DRepository;
import com.Microservices.TopologyService.repositories.Polygon3DRepository;
import com.Microservices.TopologyService.repositories.TINRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ij.util.WildcardMatch;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Topology Service", description = "Compute relationships between geometries from Postgres")
public class ImportRestController {

  // @Autowired
  // CheckFiles minio;

  @Autowired
  Point2DRepository point2dRepo;

  @Autowired
  Point3DRepository point3dRepo;

  @Autowired
  Line2DRepository line2dRepo;

  @Autowired
  Line3DRepository line3dRepo;

  @Autowired
  Polygon2DRepository poly2dRepo;

  @Autowired
  Polygon3DRepository poly3dRepo;

  @Autowired
  TINRepository tinRepository;

  Logger log = LoggerFactory.getLogger(ImportRestController.class);

  // @GetMapping("/topology/intersect")
  // public String intersect() {
  // String result = "";
  // // HashMap<UUID,UUID> intersects = point2dRepo.getIntersects();

  // // for(UUID key : intersects.keySet()) {
  // // result += key + ", " + intersects.get(key) + "\n";
  // // }

  // // String[][] i0 = point2dRepo.getIntersectWithSelf();
  // // String[][] i1 = point2dRepo.getIntersectWithPoly2D();
  // // String[][] i2 = point2dRepo.getIntersectWithPoint3D();
  // // String[][] i3 = point2dRepo.getIntersectWithPoly3D();
  // // String[][] i4 = point2dRepo.getIntersectWithLine2D();
  // // String[][] i5 = point2dRepo.getIntersectWithLine3D();
  // // String[][] i6 = point2dRepo.getIntersectWithSolid();

  // // int c = i0.length + i1.length + i2.length + i3.length + i4.length +
  // i5.length
  // // + i6.length;

  // // String[][] intersects = new String[c][3];
  // // System.arraycopy(i0, 0, intersects, 0, i0.length);
  // // System.arraycopy(i1, 0, intersects, i0.length, i1.length);
  // // System.arraycopy(i2, 0, intersects, i0.length + i1.length, i2.length);
  // // System.arraycopy(i3, 0, intersects, i0.length + i1.length + i2.length,
  // // i3.length);
  // // System.arraycopy(i4, 0, intersects, i0.length + i1.length + i2.length +
  // // i3.length, i4.length);
  // // System.arraycopy(i5, 0, intersects, i0.length + i1.length + i2.length +
  // // i3.length + i4.length, i5.length);
  // // System.arraycopy(i6, 0, intersects, i0.length + i1.length + i2.length +
  // // i3.length + i4.length + i5.length,
  // // i6.length);

  // result += getTopo2(point2dRepo.getIntersectWithSelf());
  // result += getTopo2(point2dRepo.getIntersectWithPoly2D());
  // result += getTopo2(point2dRepo.getIntersectWithPoint3D());
  // result += getTopo2(point2dRepo.getIntersectWithPoly3D());
  // result += getTopo2(point2dRepo.getIntersectWithLine2D());
  // result += getTopo2(point2dRepo.getIntersectWithLine3D());
  // result += getTopo2(point2dRepo.getIntersectWithSolid());

  // // List<Polygon2D> poly = (List<Polygon2D>) polygon2dRepo.findAll();
  // // for (int i = 0; i < poly.size(); i++) {
  // // String [] intersects =
  // point2dRepo.getIntersects(poly.get(i).getGeometry());
  // // for (int j = 0; j < intersects.length; j++) {

  // // result += intersects[j] + ", " + poly.get(i).getId() + "\n";

  // // }
  // // }

  // // UUID [] intersects = point2dRepo.getIntersects();
  // // for (int j = 0; j < intersects.length; j++) {

  // // result += intersects[j] + "\n";

  // // }

  // return result;
  // }

  @GetMapping("/topology/relations")
  public String relations() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String result = "";
    int disjoint = 0;
    long start = System.currentTimeMillis();

    ArrayList<String []> relations = new ArrayList<String []>();

    Method [] methods = Point2DRepository.class.getMethods();
    for( int index =0; index < methods.length; index++){

      // Object res = methods[index].invoke(point2dRepo);
      Object s =  methods[index].invoke(point2dRepo);

  
      // relations.addAll(matchRelations(a));
    }

    // relations.addAll(matchRelations(point2dRepo.relatePoint2d(), 0, 0));
    // relations.addAll(matchRelations(point2dRepo.relatePoint3d(), 0, 0));
    // relations.addAll(matchRelations(point2dRepo.relateLine2d(), 0, 1));
    // relations.addAll(matchRelations(point2dRepo.relateLine3d(), 0, 1));
    // relations.addAll(matchRelations(point2dRepo.relatePolygon2d(), 0, 2));
    // relations.addAll(matchRelations(point2dRepo.relatePolygon3d(), 0, 2));
    // // relations.addAll(matchRelations(point2dRepo.relateSolid(), 0, 2));
    // relations.addAll(matchRelations(point2dRepo.relateTIN(), 0, 2));
    // relations.addAll(matchRelations(point2dRepo.relateBreaklines(), 0, 1));
    // relations.addAll(matchRelations(point2dRepo.relateEbmarkment(), 0, 2));
    // relations.addAll(matchRelations(point2dRepo.relateSpecialPoints(), 0, 0));

    // relations.addAll(matchRelations(point3dRepo.relatePoint3d(), 0, 0));
    // relations.addAll(matchRelations(point3dRepo.relateLine2d(), 0, 1));
    // relations.addAll(matchRelations(point3dRepo.relateLine3d(), 0, 1));
    // relations.addAll(matchRelations(point3dRepo.relatePolygon2d(), 0, 2));
    // relations.addAll(matchRelations(point3dRepo.relatePolygon3d(), 0, 2));
    // // relations.addAll(matchRelations(point3dRepo.relateSolid(), 0, 2));
    // relations.addAll(matchRelations(point3dRepo.relateTIN(), 0, 2));
    // relations.addAll(matchRelations(point3dRepo.relateBreaklines(), 0, 1));
    // relations.addAll(matchRelations(point3dRepo.relateEbmarkment(), 0, 2));
    // relations.addAll(matchRelations(point3dRepo.relateSpecialPoints(), 0, 0));

    // relations.addAll(matchRelations(line2dRepo.relateLine2d(), 1, 1));
    // relations.addAll(matchRelations(line2dRepo.relateLine3d(), 1, 1));
    // relations.addAll(matchRelations(line2dRepo.relatePolygon2d(), 1, 2));
    // relations.addAll(matchRelations(line2dRepo.relatePolygon3d(), 1, 2));
    // // relations.addAll(matchRelations(line2dRepo.relateSolid(), 1, 2));
    // relations.addAll(matchRelations(line2dRepo.relateTIN(), 1, 2));
    // relations.addAll(matchRelations(line2dRepo.relateBreaklines(), 1, 1));
    // relations.addAll(matchRelations(line2dRepo.relateEbmarkment(), 1, 2));
    // relations.addAll(matchRelations(line2dRepo.relateSpecialPoints(), 1, 0));

    // relations.addAll(matchRelations(line3dRepo.relateLine3d(), 1, 1));
    // relations.addAll(matchRelations(line3dRepo.relatePolygon2d(), 1, 2));
    // relations.addAll(matchRelations(line3dRepo.relatePolygon3d(), 1, 2));
    // // relations.addAll(matchRelations(line3dRepo.relateSolid(), 1, 2));
    // relations.addAll(matchRelations(line3dRepo.relateTIN(), 1, 2));
    // relations.addAll(matchRelations(line3dRepo.relateBreaklines(), 1, 1));
    // relations.addAll(matchRelations(line3dRepo.relateEbmarkment(), 1, 2));
    // relations.addAll(matchRelations(line3dRepo.relateSpecialPoints(), 1, 0));

    // relations.addAll(matchRelations(poly2dRepo.relatePolygon2d(), 2, 2));
    // relations.addAll(matchRelations(poly2dRepo.relatePolygon3d(), 2, 2));
    // // relations.addAll(matchRelations(poly2dRepo.relateSolid(), 2, 2);
    // relations.addAll(matchRelations(poly2dRepo.relateTIN(), 2, 2));
    // relations.addAll(matchRelations(poly2dRepo.relateBreaklines(), 2, 1));
    // relations.addAll(matchRelations(poly2dRepo.relateEbmarkment(), 2, 2));
    // relations.addAll(matchRelations(poly2dRepo.relateSpecialPoints(), 2, 0));

    // relations.addAll(matchRelations(poly3dRepo.relatePolygon3d(), 2, 2));
    // // relations.addAll(matchRelations(poly3dRepo.relateSolid(), 2, 2));
    // relations.addAll(matchRelations(poly3dRepo.relateTIN(), 2, 2));
    // relations.addAll(matchRelations(poly3dRepo.relateBreaklines(), 2, 1));
    // relations.addAll(matchRelations(poly3dRepo.relateEbmarkment(), 2, 2));
    // relations.addAll(matchRelations(poly3dRepo.relateSpecialPoints(), 2, 0));

    long end = System.currentTimeMillis();
    String diff = (end - start) / 1000 + "s";

    return "Anzahl Relationen: " + relations.size() + "\n" + "Dauer: " + diff;
  }

  public ArrayList<String[]> matchRelations(String[][] rel) {

    // String result = "";
    ArrayList<String[]> relations = new ArrayList<String[]>();

    for (int i = 0; i < rel.length; i++) {

      String a = rel[i][0];
      String b = rel[i][1];
      int dimA = Integer.parseInt(rel[i][2]);
      int dimB = Integer.parseInt(rel[i][3]);;
      String mask = rel[i][4];
      String bool = mask.replaceAll("[0-2]", "T");
      WildcardMatch wm = new WildcardMatch();

      if (a.equals(b)) {

      } else if (wm.match(bool, "FF?FF????")) {

      } else if (wm.match(bool, "T?F??FFF?")) {
        String[] arr = { a, "equals", b };
        relations.add(arr);
      } else {

        if (wm.match(bool, "T?F??F???")) {
          String[] arr = { a, "within", b };
          relations.add(arr);
          String[] arr1 = { b, "contains", a };
          relations.add(arr1);
        }
        if (wm.match(bool, "?TF??F???") || wm.match(bool, "??FT?F???") || wm.match(bool, "??F?TF???")) {
          String[] arr = { a, "coveredBy", b };
          relations.add(arr);
          String[] arr1 = { b, "covers", a };
          relations.add(arr1);
        }
        if (wm.match(bool, "FT???????") || wm.match(bool, " 	F??T?????") || wm.match(bool, "F???T????")) {
          String[] arr = { a, "touches", b };
          relations.add(arr);
        }
        if (wm.match(bool, "T?????FF?")) {
          String[] arr = { a, "contains", b };
          relations.add(arr);
          String[] arr1 = { b, "within", a };
          relations.add(arr1);
        }
        if (wm.match(bool, "?T????FF?") || wm.match(bool, "???T??FF?") || wm.match(bool, "????T?FF?")) {
          String[] arr = { a, "covers", b };
          relations.add(arr);
          String[] arr1 = { b, "coveredBy", a };
          relations.add(arr1);
        }
        if (dimA < dimB && wm.match(bool, "T?T??????") || dimA > dimB && wm.match(bool, "T?????T??")
            || dimA == 1 && dimB == 1 && mask.startsWith("0")) {
          String[] arr = { a, "crosses", b };
          relations.add(arr);
        }
        if (dimA == dimB && (dimA == 0 || dimA == 2 && wm.match(bool, "T?T???T??"))
            || (dimA == 1 && mask.startsWith("1") && wm.match(bool, "T?T???T??"))) {
          String[] arr = { a, "overlaps", b };
          relations.add(arr);
        }
      }
    }
    return relations;
  }
  // public String getTopo2(String[][] intersects) {

  // String result = "";

  // for (int i = 0; i < intersects.length; i++) {

  // String a = intersects[i][0];
  // String b = intersects[i][1];
  // String geom = intersects[i][2];

  // if (a.equals(b)) {
  // } else {
  // if (point2dRepo.coveredBy(a, geom) == 1) {
  // result += a + " coveredBy " + b + "\n";
  // }
  // if (point2dRepo.covers(a, geom) == 1) {
  // result += a + " covers " + b + "\n";
  // }
  // if (point2dRepo.overlaps(a, geom) == 1) {
  // result += a + " overlaps " + b + "\n";
  // }
  // if (point2dRepo.crosses(a, geom) == 1) {
  // result += a + " crosses " + b + "\n";
  // }
  // if (point2dRepo.touches(a, geom) == 1) {
  // result += a + " touches " + b + "\n";
  // }
  // if (point2dRepo.equals(a, geom) == 1) {
  // result += a + " equals " + b + "\n";
  // }
  // }
  // }
  // return result;
  // }

  // if (a.equals(b)) {
  // }
  // else if (point2dRepo.coveredBy(a, geom) == 1) {
  // result += a + " coveredBy " + b + "\n";
  // }
  // else if (point2dRepo.covers(a, geom) == 1) {
  // result += a + " covers " + b + "\n";
  // }
  // else if (point2dRepo.overlaps(a, geom) == 1) {
  // result += a + " overlaps " + b + "\n";
  // }
  // else if (point2dRepo.crosses(a, geom) == 1) {
  // result += a + " crosses " + b + "\n";
  // }
  // else if (point2dRepo.touches(a, geom) == 1) {
  // result += a + " touches " + b + "\n";
  // }
  // else if (point2dRepo.equals(a, geom) == 1) {
  // result += a + " equals " + b + "\n";
  // }
  // else {
  // result += a + " unknown relationship " + b + "\n";
  // }

  // @GetMapping("/topology/intersect2")
  // public String intersect2() {
  // String result = "";
  // String[][] intersects = point2dRepo.getIntersects2();
  // for (int i = 0; i < intersects.length; i++) {

  // String a = intersects[i][0];
  // String b = intersects[i][1];

  // if (point2dRepo.coveredBy(a, b) == 1) {
  // result += a + " coveredBy " + b + "\n";
  // }
  // else if (point2dRepo.covers(a, b) == 1) {
  // result += a + " covers " + b + "\n";
  // }
  // else if (point2dRepo.overlaps(a, b) == 1) {
  // result += a + " overlaps " + b + "\n";
  // }
  // else if (point2dRepo.crosses(a, b) == 1) {
  // result += a + " crosses " + b + "\n";
  // }
  // else if (point2dRepo.touches(a, b) == 1) {
  // result += a + " touches " + b + "\n";
  // }
  // else if (point2dRepo.equals(a, b) == 1) {
  // result += a + " equals " + b + "\n";
  // }
  // else {
  // result += a + " unknown relationship " + b + "\n";
  // }
  // }
  // return result;
  // }
}