package com.Microservices.PostgresImportService.controller;

import com.Microservices.PostgresImportService.service.CheckFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  CheckFiles minio;

  Logger log = LoggerFactory.getLogger(ImportController.class);

  // start page
  @GetMapping("/postgresimport")
  public String index(Model model) throws Exception {
    model.addAttribute("erg", "");
    return "index";
  }

  // get folder and use them
  @PostMapping("/postgresimport")
  public String send(
      @RequestParam(name = "folder", required = false, defaultValue = "kein Ordner gewählt") String folder, String repo, Model model)
      throws Exception {

    log.info("Start import of geometries into postgres database");
    String results = minio.getFiles(folder, repo);
    model.addAttribute("erg", results);

    return "index";
  }
}