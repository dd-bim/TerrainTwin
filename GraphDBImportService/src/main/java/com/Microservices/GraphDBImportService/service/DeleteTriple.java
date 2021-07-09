package com.Microservices.GraphDBImportService.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import com.Microservices.GraphDBImportService.connection.GraphDBConnection;
import com.Microservices.GraphDBImportService.connection.MinIOConnection;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

@Service
public class DeleteTriple {

    @Autowired
    GraphDBConnection dbconnection;

    Logger log = LoggerFactory.getLogger(DeleteTriple.class);



    public String delete(String repo) throws Exception {

        String results = "";
    
        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection2(repo);
            ValueFactory factory = SimpleValueFactory.getInstance();
            String ns = "http://www.opengis.net/ont/geosparql#";
            IRI p = factory.createIRI("geo:dimension");
            IRI s = factory.createIRI("https://terrain.dd-bim.org/postgres/8f64ad6c-c2f2-4ca0-88ed-011d8654997f");
            Value o = factory.createLiteral(100);
            log.info(db.toString());
            // RepositoryResult<Namespace> d = db.getNamespaces();
            // d.forEach(n -> {
            //     System.out.println(n.toString());
            // });
            try {
                db.begin();
              db.remove(s,null,null); 
              db.close(); 
            } catch (Exception e) {
                results += e.getMessage(); 
            }

            // String name = db.getNamespace("geo");
            // log.info(name);

            results += "Removed";
           
        } catch (Exception e) {
            results += "Could not connect to GraphDB. Possibly the database is not available. \n Message: "
                    + e.getMessage();
        }
        return results;
    }

}