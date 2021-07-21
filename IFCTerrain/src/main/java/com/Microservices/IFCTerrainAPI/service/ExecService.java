package com.Microservices.IFCTerrainAPI.service;

import java.io.IOException;

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

        // prepare command
        processBuilder.command("sh", "-c", "mono IFCTerrainCommand.exe " + file);
       
        try {

            // execute command
            Process process = processBuilder.start();

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
