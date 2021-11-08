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
import java.util.List;
import java.util.UUID;

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
import org.bimserver.interfaces.objects.SRevision;
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

    @Autowired
    ImportIfcInfos ifcInfos;

    Logger log = LoggerFactory.getLogger(ImportIfc.class);

    // import ifc files to BIMserver and metadata to GraphDB
    public String importIfcFile(String bucket, String repo, String filename) throws IOException {

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
            result += e1.getMessage();
        }

        // use file stream
        OutputStream outputStream = null;
        try {
            // copy stream to temporary file
            outputStream = new FileOutputStream(file);
            IOUtils.copy(stream, outputStream);

            // find out ifc version from tag FILE_SCHEMA in the file
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
            log.info(e.getMessage());
        } finally{
            stream.close();
            outputStream.close();
        }
        
        // connect to BIMserver
        BimServerClient bimClient = bimserver.getConnection();

        // create new project with name of the file
        SProject newProject = null;
        try {
            newProject = bimClient.getServiceInterface().addProject(pName, version);
        } catch (ServerException | UserException | PublicInterfaceNotFoundException e1) {
            e1.printStackTrace();
            result += e1.getMessage();
        }
        long poid = newProject.getOid();

        // find the correct deserializer for uploading the file
        SDeserializerPluginConfiguration deserializer = null;
        try {
            deserializer = bimClient.getServiceInterface().getSuggestedDeserializerForExtension("ifc",
                    poid);
        } catch (ServerException | UserException | PublicInterfaceNotFoundException e1) {
            e1.printStackTrace();
            result += e1.getMessage();
        }

        // try to upload the file to the new project
        DataSource source = new FileDataSource(file);
        DataHandler data = new DataHandler(source);

        SLongCheckinActionState state;
        try {
            state = bimClient.getServiceInterface().checkinSync(poid, "", deserializer.getOid(),
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

        // get the revision id (roid) of the uploaded file
        List<SRevision> revs = null;
        try {
          revs = bimClient.getServiceInterface().getAllRevisionsOfProject(poid);
        } catch (ServerException | UserException | PublicInterfaceNotFoundException e) {
          e.printStackTrace();
        }
    
        Long roid = revs.get(0).getOid();
        // UUID uuid = revs.get(0).getUuid(); // ist immer Null
        UUID uuid = newProject.getUuid();
        log.info("poid: " + poid + "\n" + "roid: " + roid + "\n" + "uuid: " + uuid);

        // import poid, roid, uuid as metadate vor linking into the GraphDB repository
        result += "\n" + ifcInfos.importIfcInfos(poid, roid, uuid, filename, bucket, repo) + "\n";


        return result;
    }
}
