package com.Microservices.GraphDBImportService.controller;

import com.Microservices.GraphDBImportService.service.ImportService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin(origins = "http://localhost:8084")
public class ImportController {

  @Value("${minio.url}")
  private String url;
  @Value("${minio.port}")
  private String port;
  @Value("${minio.access_key}")
  private String access_key;
  @Value("${minio.secret_key}")
  private String secret_key;

  @Value("${graphdb.url}")
  private String graphdb_url;
  @Value("${graphdb.username}")
  private String graphdb_username;
  @Value("${graphdb.password}")
  private String graphdb_password;

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

    ImportService minio = new ImportService(url, port, access_key, secret_key, graphdb_url, graphdb_username, graphdb_password);
    results += minio.getFiles(folder, repo, url);

    model.addAttribute("erg", results);

    return "index";
  }
}