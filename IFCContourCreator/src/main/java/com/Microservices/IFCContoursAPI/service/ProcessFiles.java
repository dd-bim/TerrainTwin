package com.Microservices.IFCContoursAPI.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProcessFiles {

    Logger log = LoggerFactory.getLogger(ProcessFiles.class);

    // convert multipart class file to java class file
    public String multipartToFile(MultipartFile file) throws IllegalStateException, IOException {
        File sourceFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(sourceFile);
        File convFile = new File("files/" + file.getOriginalFilename());
        Files.copy(sourceFile.toPath(), convFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file.getOriginalFilename();
    }

    // remove created files from container
    public void removeFiles() {
        File file = new File("files");
        try {
            FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
