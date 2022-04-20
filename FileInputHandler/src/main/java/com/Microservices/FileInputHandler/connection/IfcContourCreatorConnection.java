package com.Microservices.FileInputHandler.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IfcContourCreatorConnection {

    Logger log = LoggerFactory.getLogger(IfcContourCreatorConnection.class);

    public String creator(String bucket, String filename) {
        String conn = "";
        try {

            URL url = new URL(
                    "http://ifccontour-creator:7212/ifccontourcreator/createFromStorage/minio/" + bucket + "/file/" + filename);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            int res = connection.getResponseCode();
            if (res == 200) {
                log.info("Create contour from " + filename + ".\n");
                InputStream x = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(x));
                        // String s = reader.readLine(); // skip first line with descriptions for result
                        String s;
                        while ((s = reader.readLine()) != null) {
                            conn += s;
                        }
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
