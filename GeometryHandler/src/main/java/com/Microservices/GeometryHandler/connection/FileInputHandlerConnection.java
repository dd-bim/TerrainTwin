package com.Microservices.GeometryHandler.connection;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    public String graphdbGetTINInfos(String geometry, String repo) {
        String conn = "";
        try {

            URL url = new URL("http://file-input-handler:7201/inputhandler/geometry/tinVersion/repo/" + repo
                    + "?geometry=" + geometry);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setConnectTimeout(5000);// 5 secs
            // connection.setReadTimeout(5000);// 5 secs

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            int res = connection.getResponseCode();
            if (res == 200) {
                InputStream o = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = o.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }
                conn = baos.toString("UTF-8");
                log.info("Result: " + conn);
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
