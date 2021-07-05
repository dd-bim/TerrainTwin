package com.Microservices.PostgresImportService.service;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.Microservices.PostgresImportService.domain.model.PostgresInfos;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphDBImport {

    Logger log = LoggerFactory.getLogger(GraphDBImport.class);

    public String graphdbImport(PostgresInfos info) {
                String conn = "";
        try {

            URL url = new URL("http://graphdb-import-service:7201/graphdbimport/postgresinfos");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);// 5 secs
            connection.setReadTimeout(5000);// 5 secs

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            String json = new Gson().toJson(info);
            out.write(json);
            out.flush();
            out.close();

            int res = connection.getResponseCode();
            if (res == 200) {
                log.info("Import in GraphDB successfully finished.");
            } else {
                log.info("Error: " + res);
            }

        } catch (Exception e) {
            log.error("HttpRequest failed. \n " + e.getMessage());
            conn = "HttpRequest failed. \n " + e.getMessage();
        }
        return conn;
    }
}
