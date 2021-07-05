package com.Microservices.TopologyService.service;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.Microservices.TopologyService.domain.model.Triple;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphDBImport {

    Logger log = LoggerFactory.getLogger(GraphDBImport.class);

    public String graphdbImport(List<Triple> relations, String repo) {
        String conn = "";
        try {

            URL url = new URL("http://graphdb-import-service:7201/graphdbimport/topology/graphdbrepo/" + repo); //http://host.docker.internal:7201/graphdbimport/topology/graphdbrepo/
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);// 5 secs
            connection.setReadTimeout(5000);// 5 secs

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            String json = new Gson().toJson(relations);
            out.write(json);
            out.flush();
            out.close();

            int res = connection.getResponseCode();

            if (res == 200) {
                log.info("200 - Import in GraphDB successfully finished.");
            } else {
                log.info(res + " - Error");
            }

        } catch (Exception e) {
            log.error("HttpRequest failed. \n " + e.getMessage());
            conn = "HttpRequest failed. \n " + e.getMessage();
        }
        return conn;
    }
}
