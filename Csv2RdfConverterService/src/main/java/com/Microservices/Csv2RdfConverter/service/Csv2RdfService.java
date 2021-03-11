package com.Microservices.Csv2RdfConverter.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.stereotype.Service;
import com.Microservices.Csv2RdfConverter.domain.model.ConvertInfos;

@Service
public class Csv2RdfService {

    String namespace = "http://example.org/Sachdaten/";
    String ns = "sd";
    String superclass = "Sachdaten";
    String delimiter = ";";
    String feedback = "";

    public String convert(ConvertInfos infos) {
        File filename = new File(infos.getFile());
        if (infos.getNamespace() != null)
            namespace = infos.getNamespace();
        if (infos.getPrefix() != null)
            ns = infos.getPrefix();
        if (infos.getSuperclass() != null)
            superclass = infos.getSuperclass();
        if (infos.getDelimiter() != null)
            delimiter = infos.getDelimiter();
        String ns1 = ns + ":";
        String headclass = ns1 + superclass;
        String convFile = "";

        try {
            // read csv file line by line to array
            BufferedReader csvReader = new BufferedReader(new FileReader(filename));
            String row;
            ArrayList<String[]> csvInhalt = new ArrayList<String[]>();
            while ((row = csvReader.readLine()) != null) {
                csvInhalt.add(row.split(delimiter));
            }
            csvReader.close();

            // create model for rdf triples
            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace(ns, namespace).setNamespace(RDFS.NS).setNamespace(OWL.NS);
            builder.add(headclass, RDF.TYPE, RDFS.CLASS);

            // create triples from csv array
            String[] header = csvInhalt.get(0);
            for (int i = 0; i < header.length; i++) {
                String column = ns1 + header[i].replaceAll("\s", "_").replaceAll("\\W", "");
                builder.add(column, RDF.TYPE, OWL.DATATYPEPROPERTY).add(column, RDFS.DOMAIN, headclass).add(column,
                        RDFS.RANGE, RDFS.LITERAL);
            }
            for (int i = 1; i < csvInhalt.size(); i++) {
                String resource = ns1 + UUID.randomUUID().toString();
                builder.add(resource, RDF.TYPE, headclass);
                String[] dataset = csvInhalt.get(i);
                for (int j = 0; j < dataset.length; j++) {
                    builder.add(resource, ns1 + header[j].replaceAll("\s", "_").replaceAll("\\W", ""), dataset[j]);
                }
            }
            Model m = builder.build();

            // write model in turtle file
            convFile = filename.getAbsolutePath().split(Pattern.quote("."))[0] + ".ttl";
            File file = new File(convFile);
            FileOutputStream out = new FileOutputStream(file);
            try {
                Rio.write(m, out, RDFFormat.TURTLE);

            } finally {
                out.close();
            }
            feedback = "File created under: " + convFile;
        } catch (FileNotFoundException f) {
            feedback = "400 - File not found. \n" + f.getMessage();
            f.printStackTrace();
        } catch (IOException e) {
            feedback = "400 - Something went wrong. \n" + e.getMessage();
            e.printStackTrace();
        }
        return feedback;
    }
}