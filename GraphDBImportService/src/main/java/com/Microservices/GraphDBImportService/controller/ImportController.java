package com.Microservices.GraphDBImportService.controller;

import com.Microservices.GraphDBImportService.service.MinIOConnection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/terraintwin")
@CrossOrigin(origins = "http://localhost:8084")
public class ImportController {

  // start page
  @GetMapping("/graphdbimport/home")
  public String index(Model model) throws Exception {
    model.addAttribute("erg", "");
    return "index";
  }

  // get folder and use them
  @GetMapping("/graphdbimport/send")
  public String send(@RequestParam(name = "MinIO-Bucket", defaultValue = "kein Ordner gewählt") String folder,
      @RequestParam(name = "GraphDB-Repository", defaultValue = "kein Repository gewählt") String repo, Model model)
      throws Exception {
    String results = "";

    MinIOConnection minio = new MinIOConnection();
    results += minio.getFiles(folder, repo);

    model.addAttribute("erg", results);

    return "index";
  }
}