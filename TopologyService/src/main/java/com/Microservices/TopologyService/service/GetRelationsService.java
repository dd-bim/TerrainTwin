package com.Microservices.TopologyService.service;

import java.util.ArrayList;

import ij.util.WildcardMatch;

public class GetRelationsService {
    
    public static ArrayList<String[]> matchRelations(String[][] rel) {

        ArrayList<String[]> relations = new ArrayList<String[]>();
    
        for (int i = 0; i < rel.length; i++) {
    
          String a = rel[i][0];
          String b = rel[i][1];
          int dimA = Integer.parseInt(rel[i][2]);
          int dimB = Integer.parseInt(rel[i][3]);
          ;
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
}
