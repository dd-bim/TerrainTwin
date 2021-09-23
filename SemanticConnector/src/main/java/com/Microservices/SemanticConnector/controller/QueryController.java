package com.Microservices.SemanticConnector.controller;

import java.util.ArrayList;

import com.Microservices.SemanticConnector.domain.FlexibleQuery;
import com.Microservices.SemanticConnector.domain.StaticQuery;
import com.Microservices.SemanticConnector.service.QueryExecution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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

  @GetMapping("/semanticconnector/features/repo/{repo}")
  public String getFeatures(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, StaticQuery.FEATURES.toString());

    for (int z = 0; z < list.size(); z++) {
      result += list.get(z) + "\n";
    }
    return result;
  }

  @GetMapping("/semanticconnector/ttobjclasses/repo/{repo}")
  public String getTTObjClasses(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, StaticQuery.TTOBJCLASSES.toString());

    for (int z = 0; z < list.size(); z++) {
      result += list.get(z) + "\n";
    }
    return result;
  }

  @GetMapping("/semanticconnector/ttobjrelations/repo/{repo}")
  public String getTTObjRelations(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, StaticQuery.TTOBJRELATIONS.toString());

    for (int z = 0; z < list.size(); z++) {
      result += list.get(z) + "\n";
    }
    return result;
  }

  @GetMapping("/semanticconnector/extttobjrelations/repo/{repo}")
  public String getExtTTObjRelations(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, StaticQuery.EXTENDED_TTOBJRELATIONS.toString());

    for (int z = 0; z < list.size(); z = z + 3) {
      result += "Relation: " + list.get(z) + ", Predicate: " + list.get(z + 1) + ", Range: " + list.get(z + 2) + "\n";
    }
    return result;
  }

  @GetMapping("/semanticconnector/fachmodelle/repo/{repo}")
  public String getFachmodelle(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, StaticQuery.SUBJECT_MODELS.toString());

    for (int z = 0; z < list.size(); z++) {
      result += list.get(z) + "\n";
    }
    return result;
  }

  @GetMapping("/semanticconnector/gruppen/repo/{repo}")
  public String getGruppen(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, StaticQuery.GROUPS.toString());

    for (int z = 0; z < list.size(); z++) {
      result += list.get(z) + "\n";
    }
    return result;
  }

  @GetMapping("/semanticconnector/klassen/repo/{repo}")
  public String getKlassen(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, StaticQuery.CLASSES.toString());

    for (int z = 0; z < list.size(); z++) {
      result += list.get(z) + "\n";
    }
    return result;
  }

  @PostMapping("/semanticconnector/gruppenimfachmodell/repo/{repo}")
  public String getGruppenImFachmodell(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the fachmodell in datacat ontology.") @RequestParam String subjectModel) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, flexibleQuery.findGroupsInSubjectModel(subjectModel));

    for (int z = 0; z < list.size(); z++) {
      result += list.get(z) + "\n";
    }
    return result;
  }

  @PostMapping("/semanticconnector/klasseningruppe/repo/{repo}")
  public String getKlassenInGruppe(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the gruppe in datacat ontology.") @RequestParam String group) {
    ArrayList<String> list;
    String result = "";

    list = builder.execQuery(repo, flexibleQuery.findClassesInGroup(group));

    for (int z = 0; z < list.size(); z++) {
      result += list.get(z) + "\n";
    }
    return result;
  }

}