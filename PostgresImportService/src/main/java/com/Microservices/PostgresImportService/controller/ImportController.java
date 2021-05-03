package com.Microservices.PostgresImportService.controller;

import com.Microservices.PostgresImportService.repositories.BreaklinesRepository;
import com.Microservices.PostgresImportService.repositories.SurfaceRepository;
import com.Microservices.PostgresImportService.repositories.TINRepository;
import com.Microservices.PostgresImportService.service.MinIOConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin(origins = "http://localhost:8084")
public class ImportController {

  @Autowired
  SurfaceRepository repository;
  @Autowired
  TINRepository tinRepository;
  @Autowired
  BreaklinesRepository blRepository;

  Logger log = LoggerFactory.getLogger(ImportController.class);

  @Value("${minio.url}")
  private String url;
  @Value("${minio.port}")
  private String port;
  @Value("${minio.access_key}")
  private String access_key;
  @Value("${minio.secret_key}")
  private String secret_key;

  // start page
  @GetMapping("/postgresimport")
  public String index(Model model) throws Exception {
    model.addAttribute("erg", "");
    return "index";
  }

  // get folder and use them
  @PostMapping("/postgresimport")
  public String send(
      @RequestParam(name = "folder", required = false, defaultValue = "kein Ordner gewählt") String folder, Model model)
      throws Exception {
    String results = "";

    log.info("Start import of geometries into postgres database");

    MinIOConnection minio = new MinIOConnection(url, port, access_key, secret_key);
    results += minio.getFiles(folder, tinRepository, blRepository, repository);
    model.addAttribute("erg", results);

    return "index";
  }
}