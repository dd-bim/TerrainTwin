package com.Microservices.GraphDBImportService.service;

import java.util.HashMap;
import java.util.List;

import com.Microservices.GraphDBImportService.connection.GraphDBConnection;
import com.Microservices.GraphDBImportService.domain.model.Queries;
import com.Microservices.GraphDBImportService.domain.model.Triple;

import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeleteTriples {

    @Autowired
    GraphDBConnection dbconnection;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(DeleteTriples.class);

    String exception = "Could not connect to GraphDB. Possibly the database is not available. \n Message: ";

    // delete triples containing resource with id
    public String delete(String repo, String id) throws Exception {

        String results = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            try {
                String query = "PREFIX postgres: <" + domain + "/postgres/> DELETE WHERE { postgres:" + id
                        + " ?p ?o . ?s ?d postgres:" + id + "}";
                db.prepareUpdate(query).execute();
                log.info("Triples removed");
                results += "Triples removed";
            } catch (Exception e) {

                results += "Couldn't remove triples.\n Message: " + e.getMessage();
            }

        } catch (Exception e) {
            results += exception + e.getMessage();
        }
        return results;
    }

    // delete all triples containing topological predicates
    public String deleteTopology(String repo) throws Exception {

        String results = "";
        String[] predicates = { "sfEquals", "sfTouches", "sfWithin", "sfContains", "sfOverlaps", "sfCrosses",
                "ehCovers", "ehCoveredBy" };

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            try {
                for (String predicate : predicates) {
                    String query = "PREFIX geo: <http://www.opengis.net/ont/geosparql#> DELETE WHERE { ?s geo:"
                            + predicate + " ?o . }";
                    db.prepareUpdate(query).execute();
                }
                log.info("Triples removed");
                results += "Triples removed";
            } catch (Exception e) {

                results += "Couldn't remove triples.\n Message: " + e.getMessage();
            }

        } catch (Exception e) {
            results += exception + e.getMessage();
        }
        return results;
    }

    // delete triples
    public String deleteQuery(String repo, Queries queries) throws Exception {

        String results = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            try {
                String query = "";
                HashMap<String, String> ns = queries.getNamespaces();
                for (String key : ns.keySet()) {
                    query +="PREFIX " + key + ": " + "<" +  ns.get(key) + ">\n";
                };
                query += "DELETE WHERE {\n";
                List<Triple> triples = queries.getTriples();
                for (Triple triple : triples) {
                    query += triple.getSubject() + " " + triple.getPredicate() + " " + triple.getObject() + " .\n";
                }
                query += "}";
                log.info("Delete query: " + query);
                db.prepareUpdate(query).execute();

                log.info("Query executed");
                results += "Query executed";
            } catch (Exception e) {

                results += "Couldn't execute query.\n Message: " + e.getMessage();
            }

        } catch (Exception e) {
            results += exception + e.getMessage();
        }
        return results;
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
            results += exception + e.getMessage();
        }
        return results;
    }
}