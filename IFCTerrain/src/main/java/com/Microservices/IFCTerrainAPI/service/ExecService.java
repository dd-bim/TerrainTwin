package com.Microservices.IFCTerrainAPI.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExecService {

    Logger log = LoggerFactory.getLogger(Service.class);

    // execute IFCTerrainCommand.exe in docker file with config file
    public String callConverter(String file) throws IOException, InterruptedException {

        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);

        // prepare command
        processBuilder.command("sh", "-c", "mono IFCTerrainCommand.exe " + file);

        try {

            // execute command
            Process process = processBuilder.start();

            // Stream logs from .exe and write to docker logs 
            InputStream stdout = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("IFCTERRAIN: " + line);
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                result = "Successful conversion. \n";
            } else {
                result = "Conversion failed \n";

            }

        } catch (IOException e) {
            result = "Conversion failed \n" + e.getMessage();
        }

        return result;
    }
}
