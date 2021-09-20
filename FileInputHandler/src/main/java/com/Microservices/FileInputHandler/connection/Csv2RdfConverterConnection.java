package com.Microservices.FileInputHandler.connection;

import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Csv2RdfConverterConnection {
    
    Logger log = LoggerFactory.getLogger(Csv2RdfConverterConnection.class);

    public String[] convertCsv2Rdf(String bucket, String filename) {
        String[] conn = new String[2];

        try {

            URL url = new URL("http://csv2rdf-converter-service:7202/csv2rdf/convert/bucket/" + bucket + "/file/" + filename);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setConnectTimeout(5000);
            // connection.setReadTimeout(5000);

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            int res = connection.getResponseCode();
            if (res == 200) {
                log.info("Conversion of file " + filename + " successfully finished.\n");
                conn[0] = "Conversion of file " + filename + " successfully finished.\n";
                conn[1] = "200";
            } else {
                log.info("Error: " + res + ". Couldn't convert " + filename + " to RDF syntax.\n");
                conn[0] = "Error: " + res + ". Couldn't convert " + filename + " to RDF syntax.\n";
            }

        } catch (Exception e) {
            log.error("HttpRequest to Csv2Rdf Converter failed. \n " + e.getMessage());
            conn[0] = "HttpRequest to Csv2Rdf Converter failed. \n " + e.getMessage() + "\n";
            conn[1] = "404";
        }
        return conn;
    }
}
