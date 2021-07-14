package com.Microservices.FileInputHandler.service;

import java.util.HashMap;
import java.util.List;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.domain.model.Triple;
import com.Microservices.FileInputHandler.domain.model.TriplePair;
import com.Microservices.FileInputHandler.domain.model.UpdateQuery;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UpdateTriples {

    @Autowired
    GraphDBConnection dbconnection;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(UpdateTriples.class);

    String exception = "Could not connect to GraphDB. Possibly the database is not available. \n Message: ";

    // update triples
    public String update(String repo, UpdateQuery update) throws Exception {

        String results = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            String namespace = "";
            HashMap<String, String> ns = update.getNamespaces();
            List<TriplePair> pairs = update.getPairs();

            for (String key : ns.keySet()) {
                namespace += "PREFIX " + key + ": " + "<" + ns.get(key) + ">\n";
            }

            try {

                // iterate over all triple pairs
                for (TriplePair pair : pairs) {

                    // delete the old triple
                    Triple oldTriple = pair.getOldTriple();
                    String delete = namespace + "DELETE WHERE {\n";
                    delete += oldTriple.getSubject() + " " + oldTriple.getPredicate() + " " + oldTriple.getObject()
                            + " .\n}";
                    db.prepareUpdate(delete).execute();

                    // insert the new triple
                    Triple newTriple = pair.getNewTriple();
                    String insert = namespace + "INSERT DATA {\n";
                    insert += newTriple.getSubject() + " " + newTriple.getPredicate() + " " + newTriple.getObject()
                            + " .\n}";
                    db.prepareUpdate(insert).execute();
                }

                results += "Updated all triples";

            } catch (Exception e) {

                results += "Couldn't update triples.\n Message: " + e.getMessage();
            }

        } catch (Exception e) {
            results += exception + e.getMessage();
        }
        return results;
    }
}