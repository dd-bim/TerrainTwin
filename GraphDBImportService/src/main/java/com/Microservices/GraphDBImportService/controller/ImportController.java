package com.Microservices.GraphDBImportService.controller;

import com.Microservices.GraphDBImportService.service.ImportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin(origins = "http://localhost:8084")
public class ImportController {

  @Autowired
  ImportService minio;

  // start page
  @GetMapping("/graphdbimport/home")
  public String index(Model model) throws Exception {
    model.addAttribute("erg", "");
    return "index";
  }

  // get bucket and use them
  @GetMapping("/graphdbimport/send")
  public String send(@RequestParam(name = "MinIO-Bucket", defaultValue = "kein Ordner gewählt") String bucket,
      @RequestParam(name = "GraphDB-Repository", defaultValue = "kein Repository gewählt") String repo, Model model)
      throws Exception {

    String results = minio.getFiles(bucket, repo);

    model.addAttribute("erg", results);

    return "index";
  }
}