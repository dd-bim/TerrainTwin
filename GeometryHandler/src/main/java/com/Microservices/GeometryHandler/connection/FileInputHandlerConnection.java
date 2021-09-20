package com.Microservices.GeometryHandler.connection;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.Microservices.GeometryHandler.domain.model.PostgresInfos;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileInputHandlerConnection {

    Logger log = LoggerFactory.getLogger(FileInputHandlerConnection.class);

    public String graphdbImport(PostgresInfos info) {
                String conn = "";
        try {

            URL url = new URL("http://file-input-handler:7201/inputhandler/import/postgresinfos");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setConnectTimeout(5000);// 5 secs
            // connection.setReadTimeout(5000);// 5 secs

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
