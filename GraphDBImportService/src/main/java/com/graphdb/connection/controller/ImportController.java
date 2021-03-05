package com.graphdb.connection.controller;

import com.graphdb.connection.connectors.MinIOConnection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ImportController {

  // start page
  @RequestMapping("/graphdbimport/home")
  // @GetMapping("/graphdbimport/home")
  public String index(Model model) throws Exception {
    model.addAttribute("erg", "");
    return "index";
  }

  // get folder and use them
  @GetMapping("/graphdbimport/send")
  // @GetMapping("/graphdbimport/send")
  public String send(
      @RequestParam(name = "folder", required = false, defaultValue = "kein Ordner gewählt") String folder,
      @RequestParam(name = "repo", required = false, defaultValue = "kein Repository gewählt") String repo, Model model)
      throws Exception {
    String results = "";

    MinIOConnection minio = new MinIOConnection();
    results += minio.getFiles(folder, repo);

    model.addAttribute("erg", results);

    return "index";
  }
}