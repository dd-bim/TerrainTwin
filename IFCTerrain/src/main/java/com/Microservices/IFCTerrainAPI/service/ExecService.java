package com.Microservices.IFCTerrainAPI.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.Microservices.IFCTerrainAPI.domain.model.InputConfigs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExecService {

    Logger log = LoggerFactory.getLogger(Service.class);

    public ExecService() {

    }

    public String callConverter(InputConfigs configs) throws IOException, InterruptedException {
        log.info("3d: " + configs.getIs3D()+ ", TIN " +  configs.getIsTin());
        File tmp = File.createTempFile("tmp", "json");
        FileWriter writer = new FileWriter(tmp);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String content = gson.toJson(configs);
        log.info(content);
        writer.write(content);
        writer.close();

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", "mono IFCTerrainCommand.exe " + tmp.getAbsolutePath());
        String result = "";
        try {

            Process process = processBuilder.start();

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                result = "Successful converted.";
            } else {
                result = "Conversion failed \n";

                result += process.getErrorStream().read();
            }
        } catch (IOException e) {
            result = "Conversion failed \n" + e.getMessage();
        }

        return result;
    }
}
