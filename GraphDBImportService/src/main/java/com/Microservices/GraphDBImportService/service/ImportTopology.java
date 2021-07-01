package com.Microservices.GraphDBImportService.service;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.Microservices.GraphDBImportService.connection.GraphDBConnection;
import com.Microservices.GraphDBImportService.connection.MinIOConnection;
import com.Microservices.GraphDBImportService.domain.model.Triple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImportTopology {

    @Autowired
    MinIOConnection connection;

    @Autowired
    GraphDBConnection dbconnection;

    Logger log = LoggerFactory.getLogger(ImportTopology.class);

    @Value("${minio.url}")
    private String url;
    @Value("${domain.url}")
    private String domain;

    public String importTopo(String topo, String repo) throws Exception {

        String results = "";

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Triple>>(){}.getType();
        List<Triple> triples = gson.fromJson(topo, type);
        
        System.out.println("Anzahl Triples: " + triples.size()); // 782 stimmt
        
        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            String namespace = domain + "/postgres/";
            String prefix = "postgres:";

            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace("postgres", namespace).setNamespace("geo", "http://www.opengis.net/ont/geosparql#");
            
            // write triples 
            triples.forEach( (t) -> {
                builder.add(prefix + t.getSubject(), "geo:" + t.getPredicate(), prefix + t.getObject());
                log.info(t.getPredicate());
            });

            Model m = builder.build();

            // write model in turtle file and import into repository
            File tmp = File.createTempFile("turtle", "tmp");
            FileOutputStream out = new FileOutputStream(tmp);
            try {
                Rio.write(m, out, RDFFormat.TURTLE);
            } finally {
                out.close();
            }
            db.add(tmp, namespace, RDFFormat.TURTLE);
            results += "Topology import complete. \n";

    }catch(

    Exception e)
    {
        results += "Could not connect to GraphDB. Possibly the database is not available. Triples are not imported in GraphDB. \n Message: "
                + e.getMessage();
    }return results;
    }
}