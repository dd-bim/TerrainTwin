package com.Microservices.FileInputHandler.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import com.Microservices.FileInputHandler.connection.BIMserverConnection;
import com.Microservices.FileInputHandler.connection.MinIOConnection;

import org.apache.commons.io.IOUtils;
import org.bimserver.client.BimServerClient;
import org.bimserver.interfaces.objects.SDeserializerPluginConfiguration;
import org.bimserver.interfaces.objects.SLongCheckinActionState;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.XmlParserException;

@Service
public class ImportIfc {

    @Autowired
    MinIOConnection connection;

    @Autowired
    BIMserverConnection bimserver;

    Logger log = LoggerFactory.getLogger(ImportIfc.class);

    public String importIfcFile(String bucket, String filename) throws IOException {

        String result = "";
        String pName = filename.replace(".ifc", "");
        File file = new File("/tmp/" + filename);
        String version = null;

        MinioClient client = connection.connection();

        // get stream from given bucket and object name
        InputStream stream = null;
        try {
            stream = client.getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | io.minio.errors.ServerException
                | XmlParserException | IllegalArgumentException | IOException e1) {
            e1.printStackTrace();
            result += "1: " + e1.getMessage();
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            IOUtils.copy(stream, outputStream);

            BufferedReader reader2 = new BufferedReader(new FileReader(file));
            while (reader2.readLine() != null  && version == null) {
                String line = reader2.readLine();
                if (line.contains("FILE_SCHEMA")) {
                    if (line.toUpperCase().contains("IFC4")) {
                        version = "IFC4";
                    } else if (line.toUpperCase().contains("IFC2X3")) {
                        version = "IFC2X3TC1";
                    }
                }
            }
            reader2.close();

        } catch (IOException e) {
            log.info("5: " + e.getMessage());
        } finally{
            stream.close();
            outputStream.close();
        }
        
        BimServerClient bimClient = bimserver.getConnection();
        SProject newProject = null;
        try {
            newProject = bimClient.getServiceInterface().addProject(pName, version);
        } catch (ServerException | UserException | PublicInterfaceNotFoundException e1) {
            e1.printStackTrace();
            result += "3: " + e1.getMessage();
        }

        SDeserializerPluginConfiguration deserializer = null;
        try {
            deserializer = bimClient.getServiceInterface().getSuggestedDeserializerForExtension("ifc",
                    newProject.getOid());
        } catch (ServerException | UserException | PublicInterfaceNotFoundException e1) {
            e1.printStackTrace();
            result += "4: " + e1.getMessage();
        }

        DataSource source = new FileDataSource(file);
        DataHandler data = new DataHandler(source);

        SLongCheckinActionState state;
        try {
            state = bimClient.getServiceInterface().checkinSync(newProject.getOid(), "", deserializer.getOid(),
                    file.getTotalSpace(), file.getName(), data, false);

            result += "\n Title: " + state.getTitle() + "\n Oid: " + state.getOid() + "\n Roid: " + state.getRoid()
                    + "\n Rid: " + state.getRid() + "\n Stage: " + state.getStage() + "\n TopicId: "
                    + state.getTopicId() + "\n Progress: " + state.getProgress() + "\n Uuid: " + state.getUuid()
                    + "\n State: " + state.getState();
            log.info("Title: " + state.getTitle());
        } catch (ServerException | UserException | PublicInterfaceNotFoundException e) {

            e.printStackTrace();
            result += "6: " + e.getMessage();
        }

        return result;
    }
}
