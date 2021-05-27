package com.Microservices.DashboardService.controller;

import com.Microservices.DashboardService.repositories.BreaklinesRepository;
import com.Microservices.DashboardService.repositories.EmbarkmentRepository;
import com.Microservices.DashboardService.repositories.LineRepository;
import com.Microservices.DashboardService.repositories.PointRepository;
import com.Microservices.DashboardService.repositories.PolygonRepository;
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
  PointRepository pntRepository;

  @Autowired
  LineRepository lineRepository;

  @Autowired
  PolygonRepository polyRepository;

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

    model.addAttribute("pntCount", pntRepository.count());

    model.addAttribute("lineCount", lineRepository.count());

    model.addAttribute("polyCount", polyRepository.count());

    model.addAttribute("solidCount", solidRepository.count());

    model.addAttribute("tinCount", tinRepository.count());

    model.addAttribute("blCount", blRepository.count());

    model.addAttribute("embCount", embRepository.count());

    model.addAttribute("sPntCount", sPntRepository.count());

    // model.addAttribute("sPntCount", Math.random());

    model.addAttribute("pntDups", pntRepository.getPntDuplicates());

    model.addAttribute("lineDups", lineRepository.getLineDuplicates());

    model.addAttribute("polyDups", polyRepository.getPolyDuplicates());

    model.addAttribute("solidDups", solidRepository.getSolidDuplicates());

    model.addAttribute("tinDups", tinRepository.getTINDuplicates());

    model.addAttribute("blDups", blRepository.getBlDuplicates());

    model.addAttribute("embDups", embRepository.getEmbDuplicates());

    model.addAttribute("sPntDups", sPntRepository.getSPntDuplicates());

    return "index";
  }

}