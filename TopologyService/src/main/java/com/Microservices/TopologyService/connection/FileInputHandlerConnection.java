package com.Microservices.TopologyService.connection;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.Microservices.TopologyService.domain.model.Triple;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileInputHandlerConnection {

    Logger log = LoggerFactory.getLogger(FileInputHandlerConnection.class);

    public String graphdbImport(List<Triple> relations, String repo) {
        String conn = "";
        try {

            URL url = new URL("http://file-input-handler:7201/inputhandler/import/topology/repository/" + repo);
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
