package com.Microservices.Csv2RdfConverter.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import com.Microservices.Csv2RdfConverter.connection.MinIOConnection;
import com.Microservices.Csv2RdfConverter.domain.model.ConvertInfos;

import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Service
public class Csv2RdfService {

    @Autowired
    MinIOConnection connection;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(Csv2RdfService.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    String namespace = "http://example.org/Sachdaten/";
    String ns = "sd";
    String superclass = "Sachdaten";
    String delimiter = ";";
    String feedback = "";

    public String convert(ConvertInfos infos, int index)
            throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {
        String bucket = null;
        String s = convert(infos, index, bucket);
        return s;
    }

    public String convert(ConvertInfos infos, int index, String bucket) throws InvalidKeyException,
            ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
        MinioClient client = connection.connection();
        File filename = infos.getFile();
        String source = domain + "/minio/" + bucket + "/";
        String sourceObj = bucket + ":" + filename.getName();

        // check, if parameters are set
        if (infos.getNamespace() != null) {
            namespace = infos.getNamespace();
            if (!namespace.endsWith("/"))
                namespace = namespace + "/";
        }
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
            builder.setNamespace(ns, namespace).setNamespace(RDFS.NS).setNamespace(OWL.NS).setNamespace("tto", domain + "/terraintwin/ontology/").setNamespace(bucket, source);
            builder.add(headclass, RDF.TYPE, RDFS.CLASS);

            // create triples from csv array
            // make DatatypeProperties from table header
            String[] header = csvInhalt.get(0);
            for (int i = 0; i < header.length; i++) {
                String column = ns1 + header[i].replaceAll("\s", "_").replaceAll("\\W", "");
                builder.add(column, RDF.TYPE, OWL.DATATYPEPROPERTY).add(column, RDFS.DOMAIN, headclass).add(column,
                        RDFS.RANGE, RDFS.LITERAL);
            }
            // create resources with literals as DatatypeProperties
            for (int i = 1; i < csvInhalt.size(); i++) {
                String resource = ns1 + UUID.randomUUID().toString();
                builder.add(resource, RDF.TYPE, headclass)
                .add(resource, "tto:hasSource", sourceObj);
                String[] dataset = csvInhalt.get(i);
                for (int j = 0; j < dataset.length; j++) {
                    builder.add(resource, ns1 + header[j].replaceAll("\s", "_").replaceAll("\\W", ""), dataset[j]);
                }
            }
            Model m = builder.build();
            String timestamp = format.format(new Date()).replaceAll(":", "-") + "_";

            // write model in turtle file
            // case index 1 is for the GUI, whitch can only be used as a program on a local
            // maschine
            // file is saved under the source directory
            // other case is for the API
            // file is saved in a MinIO Bucket
            if (index == 1) {
                convFile = timestamp + filename.getAbsolutePath().split(Pattern.quote("."))[0] + ".ttl";
                File file = new File(convFile);
                FileOutputStream out = new FileOutputStream(file);
                try {
                    Rio.write(m, out, RDFFormat.TURTLE);

                } finally {
                    out.close();
                }
                feedback = "File created under: " + convFile;
            } else {

                // create file locally
                convFile = filename.getName().split(Pattern.quote("."))[0] + ".ttl";
                // convFile = timestamp + filename.getName().split(Pattern.quote("."))[0] +
                // ".ttl";
                String path = System.getProperty("java.io.tmpdir") + "/" + convFile;
                File file = new File(path);
                FileOutputStream out = new FileOutputStream(file);
                try {
                    Rio.write(m, out, RDFFormat.TURTLE);

                } finally {
                    out.close();
                }

                // Make bucket if not exist.
                boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
                if (!found) {
                    // Make a new bucket.
                    feedback += createBucket(bucket);
                } else {
                    feedback += "Bucket " + bucket + " already exists.\n";
                }

                // Upload to MinIO
                client.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(convFile).filename(path).build());
                feedback += convFile + " is successfully uploaded to bucket " + bucket + ".\n";
            }

        } catch (FileNotFoundException f) {
            feedback = "400 - File not found. \n" + f.getMessage();
            f.printStackTrace();
        } catch (IOException e) {
            feedback = "400 - Something went wrong. \n" + e.getMessage();
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            feedback = "400 - Possibly the namespace is not a valid URI.";
        }
        return feedback;
    }

    // convert multipart class file to java class file
    public File multipartToFile(MultipartFile file) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

    // create new bucket
    public String createBucket(String bucket) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        MinioClient client = connection.connection();
        client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        String results = "Bucket " + bucket + " created.\n";
        log.info(results);
        return results;
    }

    // get file from MinIO bucket
    public File getSourceFile(String bucket, String filename) {
        MinioClient client = connection.connection();
        File file = new File(filename);
        try (InputStream stream = client.getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(stream, outputStream);
            outputStream.close();

        } catch (Exception e) {
            log.info("Couldn't read source file.");
        }
        return file;
    }

}