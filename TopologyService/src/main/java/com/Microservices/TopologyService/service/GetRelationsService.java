package com.Microservices.TopologyService.service;

import java.util.ArrayList;
import java.util.List;

import com.Microservices.TopologyService.domain.model.Triple;

import ij.util.WildcardMatch;

public class GetRelationsService {
    
    public static List<Triple> matchRelations(String[][] rel) {

        List<Triple> relations = new ArrayList<Triple>();
    
        for (int i = 0; i < rel.length; i++) {
    
          String a = rel[i][0];
          String b = rel[i][1];
          int dimA = Integer.parseInt(rel[i][2]);
          int dimB = Integer.parseInt(rel[i][3]);
          
          String mask = rel[i][4];
          String bool = mask.replaceAll("[0-2]", "T");
          WildcardMatch wm = new WildcardMatch();
    
          if (a.equals(b)) {
    
          } else if (wm.match(bool, "FF?FF????")) {
    
          } else if (wm.match(bool, "T?F??FFF?")) {
            relations.add(new Triple(a, "equals", b));

          } else {
    
            if (wm.match(bool, "T?F??F???")) {
              relations.add(new Triple(a, "within", b));
              relations.add(new Triple(b, "contains", a));
            }
            if (wm.match(bool, "?TF??F???") || wm.match(bool, "??FT?F???") || wm.match(bool, "??F?TF???")) {
              relations.add(new Triple(a, "coveredBy", b));
              relations.add(new Triple(b, "covers", a));
            }
            if (wm.match(bool, "FT???????") || wm.match(bool, " 	F??T?????") || wm.match(bool, "F???T????")) {
              relations.add(new Triple(a, "touches", b));
            }
            if (wm.match(bool, "T?????FF?")) {
              relations.add(new Triple(a, "contains", b));
              relations.add(new Triple(b, "within", a));
            }
            if (wm.match(bool, "?T????FF?") || wm.match(bool, "???T??FF?") || wm.match(bool, "????T?FF?")) {
              relations.add(new Triple(a, "covers", b));
              relations.add(new Triple(b, "coveredBy", a));
            }
            if (dimA < dimB && wm.match(bool, "T?T??????") || dimA > dimB && wm.match(bool, "T?????T??")
                || dimA == 1 && dimB == 1 && mask.startsWith("0")) {
              relations.add(new Triple(a, "crosses", b));
            }
            if (dimA == dimB && (dimA == 0 || dimA == 2) && wm.match(bool, "T?T???T??")
                || dimA == dimB && (dimA == 1 && mask.startsWith("1") && wm.match(bool, "T?T???T??"))) {
              relations.add(new Triple(a, "overlaps", b));
            }
          }
        }
        return relations;
      }
}
