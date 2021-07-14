package com.Microservices.FileInputHandler.service;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;

import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetData {

    @Autowired
    GraphDBConnection dbconnection;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(GetData.class);

    String exception = "Could not connect to GraphDB. Possibly the database is not available. \n Message: ";

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
            results += exception + e.getMessage();
        }
        return results;
    }
}