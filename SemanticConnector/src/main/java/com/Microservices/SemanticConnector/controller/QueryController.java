package com.Microservices.SemanticConnector.controller;

import java.util.ArrayList;

import com.Microservices.SemanticConnector.connection.GraphDBRestConnection;
import com.Microservices.SemanticConnector.domain.FlexibleQuery;
import com.Microservices.SemanticConnector.domain.StaticQuery;
import com.Microservices.SemanticConnector.service.QueryExecution;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Query", description = "A number of queries to find specific objects")
public class QueryController {

  @Autowired
  QueryExecution builder;

  @Autowired
  FlexibleQuery flexibleQuery;

  @Autowired
  GraphDBRestConnection restConn;

  // get all repositories of GraphDB
  @GetMapping(path = "/semanticconnector/repositories")
  public String getRepos() {
    ArrayList<String> result = new ArrayList<String>();
    
    result = restConn.getRepositories();
    String json = new Gson().toJson(result);
    return json;
  }

  // get all feature instances in repository
  @GetMapping(value="/semanticconnector/features/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getFeatures(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.FEATURES.toString());

    return result;
  }

  // get all TTObject classes
  @GetMapping(value="/semanticconnector/ttobjclasses/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getTTObjClasses(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.TTOBJCLASSES.toString());

    return result;
  }

  // get all TTObject relation classes
  @GetMapping(value="/semanticconnector/ttobjrelations/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getTTObjRelations(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.TTOBJRELATIONS.toString());

    return result;
  }

  // get all TTObject relation classes with domain and range predicates 
  @GetMapping(value="/semanticconnector/extttobjrelations/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getExtTTObjRelations(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    // ArrayList<String> list;
    String result = "";

    result = builder.execQuery(repo, StaticQuery.EXTENDED_TTOBJRELATIONS.toString());

    // for (int z = 0; z < list.size(); z = z + 3) {
    //   result += "Relation: " + list.get(z) + ", Predicate: " + list.get(z + 1) + ", Range: " + list.get(z + 2) + "\n";
    // }
    return result;
  }

  // get all fachmodelle classes from datacat ontology in repository
  @GetMapping(value="/semanticconnector/fachmodelle/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getFachmodelle(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.SUBJECT_MODELS.toString());

    return result;
  }

  // get all gruppen classes from datacat ontology in repository
  @GetMapping(value="/semanticconnector/gruppen/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getGruppen(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.GROUPS.toString());

    return result;
  }

  // get all klassen classes from datacat ontology in repository
  @GetMapping(value="/semanticconnector/klassen/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getKlassen(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.CLASSES.toString());

    return result;
  }

  // get all gruppen classes under one fachmodell class from datacat ontology in repository
  @PostMapping(value="/semanticconnector/gruppenimfachmodell/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getGruppenImFachmodell(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the fachmodell in datacat ontology.") @RequestParam String subjectModel) {
    String result = "";

    result = builder.execQuery(repo, flexibleQuery.findGroupsInSubjectModel(subjectModel));

    return result;
  }

  // get all klassen classes under one gruppen class from datacat ontology in repository
  @PostMapping(value="/semanticconnector/klasseningruppe/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getKlassenInGruppe(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the gruppe in datacat ontology.") @RequestParam String group) {
    String result = "";

    result = builder.execQuery(repo, flexibleQuery.findClassesInGroup(group));

    return result;
  }

  // get all domain and range predicates from one relation class off tto ontology
  @PostMapping(value="/semanticconnector/predicatesfromrelation/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getPredicatesFromRel(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the relation in tto ontology.") @RequestParam String relation) {
    String result = "";

    result = builder.execQuery(repo, flexibleQuery.findPredicatesFromRelation(relation));

    return result;
  }
}