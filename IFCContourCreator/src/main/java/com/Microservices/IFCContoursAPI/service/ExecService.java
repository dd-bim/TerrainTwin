package com.Microservices.IFCContoursAPI.service;

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
    public String callConverter(String file, Integer epsg) throws IOException, InterruptedException {

        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);

        // prepare command
        if (epsg != null) {
            processBuilder.command("sh", "-c", "mono IfcGeometryExtractor.exe " + file + " -epsg " + epsg);
        } else {
            processBuilder.command("sh", "-c", "mono IfcGeometryExtractor.exe " + file);
        }
        

        try {

            // execute command
            Process process = processBuilder.start();

            // Stream logs from .exe and write to docker logs 
            InputStream stdout = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line;
            boolean err = false;
            while ((line = reader.readLine()) != null) {
                log.info("IFCCONTOURS: " + line);
                if (line.contains("ERROR")) err = true;
            }

            int exitVal = process.waitFor();
            if (exitVal == 0 && err==false) {
                result = "Successful creation. \n";
            } else {
                result = "Creation failed \n";

            }

        } catch (IOException e) {
            result = "Creation failed \n" + e.getMessage();
        }

        return result;
    }
}
