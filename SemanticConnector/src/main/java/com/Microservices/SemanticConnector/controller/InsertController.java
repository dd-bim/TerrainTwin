package com.Microservices.SemanticConnector.controller;

import com.Microservices.SemanticConnector.domain.InsertQuery;
import com.Microservices.SemanticConnector.domain.model.Instance;
import com.Microservices.SemanticConnector.domain.model.Relation;
import com.Microservices.SemanticConnector.service.QueryExecution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
      @Parameter(description = "The IRI of the instance and the ontology class.") @RequestBody Instance instance) {
    String result = "";
    result = builder.insertQuery(repo, insertQuery.insertClassDefinition(instance));

    return result;
  }

  @PostMapping("/semanticconnector/insertttobjrelation/repo/{repo}")
  public String insertTTObjRel(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The IRI of the relation objects.") @RequestBody Relation relation) {
    String result = "";

    result = builder.insertQuery(repo, insertQuery.insertTTObjRelation(relation));

    return result;
  }
}