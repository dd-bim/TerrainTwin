package com.Microservices.FileInputHandler.service;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;

import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QueryExecution {

    @Autowired
    GraphDBConnection dbconnection;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(QueryExecution.class);

    // execute a query with max. 1 result on GraphDB
    public String executeQuery(String repo, String query) {

        String feature = "";

        // connect to a repository
        RepositoryConnection db = dbconnection.connection(repo);

        try {
            // prepare the query
            TupleQuery tupleQuery = db.prepareTupleQuery(QueryLanguage.SPARQL, query);

            // execute the query
            TupleQueryResult tupleQueryResult = tupleQuery.evaluate();

            // read query results
            while (tupleQueryResult.hasNext()) {
                // Each result is represented by a BindingSet, which corresponds to a result row
                BindingSet bindingSet = tupleQueryResult.next();

                // Each BindingSet contains one or more Bindings
                for (Binding binding : bindingSet) {
                    // Each Binding contains the variable name and the value for this result row
                    feature = binding.getValue().stringValue();
                }
            }

            // Once we are done with a particular result we need to close it
            tupleQueryResult.close();

        } finally {
            // It is best to close the connection in a finally block
            db.close();
        }

        return feature;
    }


    // get all namespaces of a repository
    public String getNamespace(String repo) throws Exception {

        String results = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            RepositoryResult<Namespace> ns = db.getNamespaces();

            for (Namespace namespace : ns) {
                results += "\"" + namespace.getPrefix() + "\"" + ": " + "\"" + namespace.getName() + "\"\n";
            }

        } catch (Exception e) {
            results += "Could not connect to GraphDB. Possibly the database is not available. \n Message: " + e.getMessage();
        }
        return results;
    }
}
