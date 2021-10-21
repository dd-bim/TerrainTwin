package com.Microservices.FileInputHandler.service;

import java.io.File;
import java.io.FileOutputStream;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.domain.model.Triple;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportTriple {

    @Autowired
    GraphDBConnection dbconnection;

    Logger log = LoggerFactory.getLogger(ImportTriple.class);

    public String importTriple(String repo, Triple triple) throws Exception {

        String results = "";
        
        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);
            
            ModelBuilder builder = new ModelBuilder();

            String[] obj = triple.getObject().split("/");
            String object = obj[obj.length - 1];
            String namespace = triple.getObject().replace(object, "");
        
            // write triple
            builder.setNamespace("", namespace);
            builder.add(triple.getSubject(), triple.getPredicate(), ":" + object);

            Model m = builder.build();

            // write model in turtle file and import into repository
            File tmp = File.createTempFile("turtle", "tmp");
            FileOutputStream out = new FileOutputStream(tmp);
            try {
                Rio.write(m, out, RDFFormat.TURTLE);
            } finally {
                out.close();
            }
            db.add(tmp, "", RDFFormat.TURTLE);
            results += "Triple import complete. \n";

    }catch(

    Exception e)
    {
        results += "Could not connect to GraphDB. Possibly the database is not available. Triples are not imported in GraphDB. \n Message: "
                + e.getMessage();
    }return results;
    }
}