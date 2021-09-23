package com.Microservices.SemanticConnector.controller;

import com.Microservices.SemanticConnector.domain.InsertQuery;
import com.Microservices.SemanticConnector.service.QueryExecution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Insert", description = "Insert triples into a repository")
public class InsertController {

  @Autowired
  QueryExecution builder;

  @Autowired
  InsertQuery insertQuery;

  @PostMapping("/semanticconnector/insertclassdefinition/repo/{repo}")
  public String insertClassDef(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the instance.") @RequestParam String instance,
      @Parameter(description = "The IRI of the ontology class.") @RequestParam String className) {
    String result = "";

    result = builder.insertQuery(repo, insertQuery.insertClassDefinition(instance, className));

    return result;
  }

  @PostMapping("/semanticconnector/insertttobjrelation/repo/{repo}")
  public String insertTTObjRel(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the relation class.") @RequestParam String relation,
      @Parameter(description = "The IRI of the first related instance.") @RequestParam String instance1, 
      @Parameter(description = "The IRI of the second related instance.") @RequestParam String instance2,
      @Parameter(description = "The IRI of the first relating predicate.") @RequestParam String predicate1,
      @Parameter(description = "The IRI of the second relating predicate.") @RequestParam String predicate2) {
    String result = "";

    result = builder.insertQuery(repo, insertQuery.insertTTObjRelation(relation, instance1, instance2, predicate1, predicate2));

    return result;
  }
}