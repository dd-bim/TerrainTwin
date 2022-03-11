package com.Microservices.BIMserverQueryService.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;

import org.bimserver.client.BimServerClient;
import org.bimserver.interfaces.objects.SDownloadResult;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Functions {

    Logger log = LoggerFactory.getLogger(Functions.class);

    public String getAllProjects(BimServerClient client) {
        String result = "";
        List<SProject> pList = null;

        try {
            pList = client.getServiceInterface().getAllProjects(true, true);
            for (SProject p : pList) {
                result += "Project name: " + p.getName() + ", Oid: " + p.getOid() + "\n";
            }
        } catch (Exception e) {
            result = e.getMessage();
        }

        return result;
    }

    public ResponseEntity<Resource> getFileByQuery(BimServerClient client, long roid, long serializerOid,
            String query) {

        String result = "";
        Set<Long> roids = new HashSet<Long>();
        roids.add(roid);
        File file;
        ResponseEntity<Resource> response = null;
        try {
            file = File.createTempFile("IfcOutput", ".tmp");

            SDownloadResult r = new SDownloadResult();
            try {
                log.info(query);
                log.info("R: " + roids);
                log.info("S: " + serializerOid);
                long s = client.getServiceInterface().download(roids, query, serializerOid, true);
                log.info("TopicId: " + s);
                r = client.getServiceInterface().getDownloadData(s);
                log.info(r.getProjectName());
            } catch (ServerException | UserException | PublicInterfaceNotFoundException e) {
                e.printStackTrace();
            }
            FileOutputStream out;
            try {
                out = new FileOutputStream(file.getAbsolutePath());
                DataHandler d = r.getFile();
                d.writeTo(out);
                modifyFile(file.getAbsolutePath());

                result = "Download successful";
                // file.deleteOnExit();

                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
                file.delete();

                ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(file.getName())
                        .build();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(contentDisposition);

                response = ResponseEntity.ok().headers(headers).contentLength(file.length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

            } catch (FileNotFoundException e1) {
                result = e1.getMessage();
                e1.printStackTrace();
            } catch (IOException e) {
                result = e.getMessage();
                e.printStackTrace();
            }
        } catch (IOException e2) {
            result = e2.getMessage();
            e2.printStackTrace();
        }
        return response;
    }

    public void modifyFile(String filePath) {
        File fileToBeModified = new File(filePath);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            // Reading all the lines of input text file into oldContent
            String line = reader.readLine();
            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }

            // Replacing oldString with newString in the oldContent
            String newContent = oldContent.replace("FILE_DESCRIPTION (('')",
                    "FILE_DESCRIPTION(('ViewDefinition [DesignTransferView_V1.0]')");

            // Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long[] getProjectRoidAndSerializerOid(BimServerClient client, String projectName) {

        long[] pInfos = new long[2];

        try {
            List<SProject> projects = client.getServiceInterface().getProjectsByName(projectName);

            pInfos[0] = projects.get(0).getLastRevisionId();
            String serializerName = projects.get(0).getSchema().replaceFirst("i", "I");

            SSerializerPluginConfiguration serializer = client.getServiceInterface()
                    .getSerializerByName(serializerName);
            pInfos[1] = serializer.getOid();

        } catch (ServerException | UserException | PublicInterfaceNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return pInfos;
    }
}
