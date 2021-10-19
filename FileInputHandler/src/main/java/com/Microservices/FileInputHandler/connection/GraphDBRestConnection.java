package com.Microservices.FileInputHandler.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GraphDBRestConnection {

    @Value("${graphdb.url}")
    private String graphdb_url;
    @Value("${graphdb.username}")
    private String graphdb_username;
    @Value("${graphdb.password}")
    private String graphdb_password;

    Logger log = LoggerFactory.getLogger(GraphDBRestConnection.class);

    public String getRepositories() {
        String result = "";
        try {

            URL url = new URL(graphdb_url + "/rest/login/" + graphdb_username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setConnectTimeout(10000);
            // connection.setReadTimeout(10000);
            connection.setRequestProperty("X-GraphDB-Password", graphdb_password);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // connection.setRequestProperty("Content-Type", "application/json");

            int res = connection.getResponseCode();
            if (res == 200) {
                String token = (String) connection.getHeaderField("Authorization");

                try {
                    URL url1 = new URL(graphdb_url + "/repositories");
                    HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                    connection1.setRequestProperty("Authorization", token);
                    connection1.setRequestMethod("GET");
                    connection1.setDoOutput(true);
                    connection1.setRequestProperty("Content-Type", "application/json");

                    int res1 = connection1.getResponseCode();
                    if (res1 == 200) {
                        InputStream x = connection1.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(x));
                        String s;
                        while ((s = reader.readLine()) != null) {
                            String[] line = s.split(",");
                            result += line[1] + ",\n";
                        }
                    }

                } catch (Exception e) {
                    log.error("HttpRequest failed. \n " + e.getMessage());
                    result = "HttpRequest failed. \n " + e.getMessage() + "\n";
                }

            } else {
                log.info("Error: " + res);
            }

        } catch (Exception e) {
            log.error("HttpRequest failed. \n " + e.getMessage());
            result = "HttpRequest failed. \n " + e.getMessage() + "\n";
        }
        return result;
    }
}
