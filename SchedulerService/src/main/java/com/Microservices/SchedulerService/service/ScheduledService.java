package com.Microservices.SchedulerService.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {

    Logger log = LoggerFactory.getLogger(ScheduledService.class);

    public String fileInputHandler(String bucket, String repo) {
        String conn = "";
        try {

            URL url = new URL(
                    "http://file-input-handler:7201/inputhandler/import/miniobucket/" + bucket + "/repository/" + repo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                conn += line + "\n";
            }
      
            int res = connection.getResponseCode();
            connection.disconnect();

            if (res == 200) {
                log.info("200");
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
