package com.Microservices.SemanticConnector.service;

import java.util.ArrayList;

import com.Microservices.SemanticConnector.connection.GraphDBConnection;
import com.google.gson.Gson;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryExecution {

    @Autowired
    GraphDBConnection dbconnection;

    Logger log = LoggerFactory.getLogger(QueryExecution.class);
    
    // execute a query on a GraphDB repository
    public String execQuery(String repo, String query) {
        ArrayList<String> list = new ArrayList<String>();

        // connect to a repository
        RepositoryConnection db = dbconnection.connection(repo);
 
        try {
            // prepare the query
            TupleQuery tupleQuery = db.prepareTupleQuery(QueryLanguage.SPARQL, query );

            // execute the query
            TupleQueryResult tupleQueryResult = tupleQuery.evaluate();

            // read query results
            while (tupleQueryResult.hasNext()) {
                // Each result is represented by a BindingSet, which corresponds to a result row
                BindingSet bindingSet = tupleQueryResult.next();

                // Each BindingSet contains one or more Bindings
                for (Binding binding : bindingSet) {
                    // Each Binding contains the variable name and the value for this result row
                    String name = binding.getName();
                    Value value = binding.getValue();

                    log.info(name + " = " + value);

                    // add result values to a list
                    list.add(value.toString());
                }
            }

            // Once we are done with a particular result we need to close it
            tupleQueryResult.close();

        } finally {
            // It is best to close the connection in a finally block
            db.close();
        }

        // return the result list
        String json = new Gson().toJson(list);
        return json;
    }


    // add triple to a repository over a insert query 
    public String insertQuery(String repo, String query) {
        String result = "";

        // connect to a GraphDB repository
        RepositoryConnection db = dbconnection.connection(repo);

        try {
            // prepare query and execute it
            db.prepareUpdate(QueryLanguage.SPARQL, query).execute();
            result = "Successfully inserted.";

        } catch (Exception e) {
            // if insert query fails, return error message
            result = e.getMessage();
        } finally {
            // It is best to close the connection in a finally block
            db.close();
        }

        return result;
    }
}
