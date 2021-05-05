package com.Microservices.GraphDBImportService.controller;

import com.Microservices.GraphDBImportService.connection.GraphDBConnection;
import com.Microservices.GraphDBImportService.connection.MinIOConnection;
import com.Microservices.GraphDBImportService.service.ImportService;

import org.eclipse.rdf4j.repository.RepositoryConnection;
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

  // get bucket and use them
  @GetMapping("/graphdbimport/send")
  public String send(@RequestParam(name = "MinIO-Bucket", defaultValue = "kein Ordner gewählt") String bucket,
      @RequestParam(name = "GraphDB-Repository", defaultValue = "kein Repository gewählt") String repo, Model model)
      throws Exception {
    String results = "";
    MinIOConnection connect = new MinIOConnection();
    GraphDBConnection dbConnect = new GraphDBConnection();

    try {
      // Connect to a repository
      RepositoryConnection graphdb = dbConnect.connection(graphdb_url, graphdb_username, graphdb_password, repo);

      // import files
      ImportService minio = new ImportService(connect.connection(url, port, access_key, secret_key), graphdb);
      results += minio.getFiles(bucket, url);
    } 
    catch (Exception e) {
      results += "Could not connect to GraphDB. Possibly the repository does not exist.";
    }
    model.addAttribute("erg", results);

    return "index";
  }
}