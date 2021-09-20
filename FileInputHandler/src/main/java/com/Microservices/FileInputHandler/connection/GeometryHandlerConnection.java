package com.Microservices.FileInputHandler.connection;

import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GeometryHandlerConnection {

    Logger log = LoggerFactory.getLogger(GeometryHandlerConnection.class);

    public String geometryImport(String bucket, String repo, String filename) {
        String conn = "";
        try {

            URL url = new URL("http://geometry-handler:7203/geometry/import/bucket/" + bucket + "/graphDbRepo/" + repo
                    + "/file/" + filename);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setConnectTimeout(10000);
            // connection.setReadTimeout(10000);

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            int res = connection.getResponseCode();
            if (res == 200) {
                log.info("Import geometries from " + filename + " into Postgres successfully finished.\n");
                conn = "Import geometries from " + filename + " into Postgres successfully finished.\n";
            } else {
                log.info("Error: " + res);
            }

        } catch (Exception e) {
            log.error("HttpRequest failed. \n " + e.getMessage());
            conn = "HttpRequest failed. \n " + e.getMessage() + "\n";
        }
        return conn;
    }
}
