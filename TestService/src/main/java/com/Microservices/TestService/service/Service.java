package com.Microservices.TestService.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Service {

    public Service() {
        
    }
    
    public String sayHello(String name) throws IOException, InterruptedException{

        File tmp = File.createTempFile("turtle", "tmp");
        FileWriter writer = new FileWriter(tmp);
        writer.write(name);
        writer.close();
        
        String[] cmd = { "mono 'IFCTerrainCommand.exe' ", tmp.getAbsolutePath() };
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
        

        return "Tesrt";
    }
}
