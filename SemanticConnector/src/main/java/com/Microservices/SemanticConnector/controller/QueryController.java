package com.Microservices.SemanticConnector.controller;

import com.Microservices.SemanticConnector.domain.FlexibleQuery;
import com.Microservices.SemanticConnector.domain.StaticQuery;
import com.Microservices.SemanticConnector.service.QueryExecution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Query", description = "A number of queries to find specific objects")
public class QueryController {

  @Autowired
  QueryExecution builder;

  @Autowired
  FlexibleQuery flexibleQuery;

  @GetMapping(value="/semanticconnector/features/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getFeatures(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.FEATURES.toString());

    return result;
  }

  @GetMapping(value="/semanticconnector/ttobjclasses/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getTTObjClasses(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.TTOBJCLASSES.toString());

    return result;
  }

  @GetMapping(value="/semanticconnector/ttobjrelations/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getTTObjRelations(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.TTOBJRELATIONS.toString());

    return result;
  }

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

  @GetMapping(value="/semanticconnector/fachmodelle/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getFachmodelle(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.SUBJECT_MODELS.toString());

    return result;
  }

  @GetMapping(value="/semanticconnector/gruppen/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getGruppen(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.GROUPS.toString());

    return result;
  }

  @GetMapping(value="/semanticconnector/klassen/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getKlassen(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";

    result = builder.execQuery(repo, StaticQuery.CLASSES.toString());

    return result;
  }

  @PostMapping(value="/semanticconnector/gruppenimfachmodell/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getGruppenImFachmodell(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the fachmodell in datacat ontology.") @RequestParam String subjectModel) {
    String result = "";

    result = builder.execQuery(repo, flexibleQuery.findGroupsInSubjectModel(subjectModel));

    return result;
  }

  @PostMapping(value="/semanticconnector/klasseningruppe/repo/{repo}", produces=MediaType.APPLICATION_JSON_VALUE)
  public String getKlassenInGruppe(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the gruppe in datacat ontology.") @RequestParam String group) {
    String result = "";

    result = builder.execQuery(repo, flexibleQuery.findClassesInGroup(group));

    return result;
  }

}