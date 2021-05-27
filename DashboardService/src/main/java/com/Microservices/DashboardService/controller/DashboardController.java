package com.Microservices.DashboardService.controller;

import com.Microservices.DashboardService.repositories.BreaklinesRepository;
import com.Microservices.DashboardService.repositories.EmbarkmentRepository;
import com.Microservices.DashboardService.repositories.Line2DRepository;
import com.Microservices.DashboardService.repositories.Line3DRepository;
import com.Microservices.DashboardService.repositories.Point2DRepository;
import com.Microservices.DashboardService.repositories.Point3DRepository;
import com.Microservices.DashboardService.repositories.Polygon2DRepository;
import com.Microservices.DashboardService.repositories.SolidRepository;
import com.Microservices.DashboardService.repositories.SpecialPointsRepository;
import com.Microservices.DashboardService.repositories.TINRepository;
import com.Microservices.DashboardService.service.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RefreshScope
@Controller
@CrossOrigin(origins = "http://localhost:8084")
public class DashboardController {

  @Autowired
  Point2DRepository pnt2DRepository;

  @Autowired
  Point3DRepository pnt3DRepository;

  @Autowired
  Line2DRepository line2DRepository;

  @Autowired
  Line3DRepository line3DRepository;

  @Autowired
  Polygon2DRepository poly2DRepository;

  @Autowired
  SolidRepository solidRepository;

  @Autowired
  TINRepository tinRepository;

  @Autowired
  BreaklinesRepository blRepository;

  @Autowired
  EmbarkmentRepository embRepository;

  @Autowired
  SpecialPointsRepository sPntRepository;

  @Autowired
  DashboardService dashboard;

  @GetMapping("/postgresinfos")
  public String getPostgresInfos(Model model) {

    int pnt2d = (int) pnt2DRepository.count();
    int pnt3d = (int) pnt3DRepository.count();
    model.addAttribute("pntCount", pnt2d + pnt3d);
    model.addAttribute("pnt2d", pnt2d);
    model.addAttribute("pnt3d", pnt3d);

    int line2d = (int) line2DRepository.count();
    int line3d = (int) line3DRepository.count();
    model.addAttribute("lineCount", line2d + line3d);
    model.addAttribute("line2d", line2d);
    model.addAttribute("line3d", line3d);

    model.addAttribute("polyCount", poly2DRepository.count());

    model.addAttribute("solidCount", solidRepository.count());

    model.addAttribute("tinCount", tinRepository.count());

    model.addAttribute("blCount", blRepository.count());

    model.addAttribute("embCount", embRepository.count());

    model.addAttribute("sPntCount", sPntRepository.count());

    // model.addAttribute("sPntCount", Math.random());

    Integer[][] pntDups1 = pnt2DRepository.getPnt2DDuplicates();
    int pnt1 = pntDups1.length;
    Integer[][] pntDups2 = pnt3DRepository.getPnt3DDuplicates();
    int pnt2 = pntDups2.length;

    Integer[][] pntDups = new Integer[pnt1 + pnt2][2];
    for (int p1 = 0; p1 < pnt1; p1++) {
      pntDups[p1][0] = pntDups1[p1][0];
      pntDups[p1][1] = pntDups1[p1][1];
    }
    for (int p2 = 0; p2 < pnt2; p2++) {
      pntDups[pnt1 - 1 + p2][0] = pntDups2[p2][0];
      pntDups[pnt1 - 1 + p2][1] = pntDups2[p2][1];
    }

    model.addAttribute("pntDups", pntDups);

    Integer[][] lineDups1 = line2DRepository.getLine2DDuplicates();
    int line1 = lineDups1.length;
    Integer[][] lineDups2 = line3DRepository.getLine3DDuplicates();
    int line2 = lineDups2.length;

    Integer[][] lineDups = new Integer[line1 + line2][2];
    for (int l1 = 0; l1 < line1; l1++) {
      lineDups[l1][0] = lineDups1[l1][0];
      lineDups[l1][1] = lineDups1[l1][1];
    }
    for (int l2 = 0; l2 < line2; l2++) {
      lineDups[line1 - 1 + l2][0] = lineDups2[l2][0];
      lineDups[line1 - 1 + l2][1] = lineDups2[l2][1];
    }
    model.addAttribute("lineDups", lineDups);

    model.addAttribute("polyDups", poly2DRepository.getPoly2DDuplicates());

    model.addAttribute("solidDups", solidRepository.getSolidDuplicates());

    model.addAttribute("tinDups", tinRepository.getTINDuplicates());

    model.addAttribute("blDups", blRepository.getBlDuplicates());

    model.addAttribute("embDups", embRepository.getEmbDuplicates());

    model.addAttribute("sPntDups", sPntRepository.getSPntDuplicates());

    return "index";
  }

}