package com.Microservices.FileInputHandler.service;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.connection.MinIOConnection;
import com.Microservices.FileInputHandler.domain.model.Queries;
import com.Microservices.FileInputHandler.domain.model.Triple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
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

    @Autowired
    QueryExecution exec;

    @Autowired
    Queries query;

    @Value("${minio.url}")
    private String url;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(ImportTopology.class);

    public String importTopo(String topo, String repo) throws Exception {

        String results = "";

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Triple>>() {
        }.getType();
        List<Triple> triples = gson.fromJson(topo, type);

        System.out.println("Anzahl Triples: " + triples.size());

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            String namespace = domain + "/postgres/";
            String pro = domain + "/project/";
            String prefix = "postgres:";

            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace("postgres", namespace).setNamespace("geo", "http://www.opengis.net/ont/geosparql#")
                    .setNamespace("pro", pro).setNamespace("tto", domain + "/terraintwin/ontology/");

            // write triples
            triples.forEach((t) -> {
                builder.add(prefix + t.getSubject(), "geo:" + t.getPredicate(), prefix + t.getObject());
         
                // add tto relation between surfaces
                if ((t.getPredicate()) != "sfTouches") {
                    int dim1 = -1;
                    int dim2 = -1;
              
                    String res1 = exec.executeQuery(repo, query.getDimension(t.getSubject()));
                    if (!res1.isEmpty()) dim1 = Integer.parseInt(res1);
                
                    String res2 = exec.executeQuery(repo, query.getDimension(t.getObject()));
                    if (!res2.isEmpty()) dim2 = Integer.parseInt(res2);
                    
                    log.info("dim1: " + dim1 + " dim2: " + dim2);

                    if (dim1 == dim2 && dim1 == 2) {
                        // get features of geometry for linking
                        String subjFeature = exec.executeQuery(repo, query.findFeature(t.getSubject()));
                        String objFeature = exec.executeQuery(repo, query.findFeature(t.getObject()));
                        log.info("subjFeature: " + subjFeature + " \n objFeature: " + objFeature);

                        // check if they have bounds
                        String boundedFeatureS = exec.executeQuery(repo, query.findBoundedFeature(subjFeature));
                        String boundedFeatureO = exec.executeQuery(repo, query.findBoundedFeature(objFeature));
                        log.info("SFeature: " + boundedFeatureS);
                        log.info("OFeature: " + boundedFeatureO);
                      
                        // create relation instance
                        String inst = "pro:" + UUID.randomUUID().toString();

                        if (!boundedFeatureS.isEmpty() && boundedFeatureO.isEmpty()) {
                            builder.add(inst, RDF.TYPE, "tto:RelTopograficElOnCoverage")
                                    .add(inst, "tto:describedSurface", "pro:" + subjFeature.replace(pro, ""))
                                    .add(inst, "tto:describingStructure", "pro:" + objFeature.replace(pro, ""));
                        } else if (!boundedFeatureO.isEmpty() && boundedFeatureS.isEmpty()) {
                            builder.add(inst, RDF.TYPE, "tto:RelTopograficElOnCoverage")
                                    .add(inst, "tto:describedSurface", "pro:" + objFeature.replace(pro, ""))
                                    .add(inst, "tto:describingStructure", "pro:" + subjFeature.replace(pro, ""));
                        }
                    }
                }
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

        } catch (Exception e) {
            log.info("Message: " + e.getMessage());
            results += "Could not connect to GraphDB. Possibly the database is not available. Triples are not imported in GraphDB. \n Message: "
                    + e.getMessage();
        }
        return results;
    }
}