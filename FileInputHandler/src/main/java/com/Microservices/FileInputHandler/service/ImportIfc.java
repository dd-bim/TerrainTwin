package com.Microservices.FileInputHandler.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import com.Microservices.FileInputHandler.connection.BIMserverConnection;
import com.Microservices.FileInputHandler.connection.MinIOConnection;
import com.Microservices.FileInputHandler.domain.model.IfcFile;

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


    public IfcFile getIfcFile(String bucket, String filename) throws IOException {
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
            log.error(e1.getMessage());
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

        return new IfcFile(file, version);

    }

    // import ifc files to BIMserver and metadata to GraphDB
    // public String importIfcFile(String bucket, String repo, String filename) throws IOException, InterruptedException {
    public String importIfcFile(IfcFile ifcFile, String bucket, String repo) throws IOException, InterruptedException {

        String result = "";
        File file = ifcFile.getFile();
        String filename = file.getName();
        String pName = filename.replace(".ifc", "");
        // String pName = filename.replace(".ifc", "");
        // File file = new File("/tmp/" + filename);
        // String version = null;

        // MinioClient client = connection.connection();

        // // get stream from given bucket and object name
        // InputStream stream = null;
        // try {
        //     stream = client.getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build());
        // } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
        //         | InvalidResponseException | NoSuchAlgorithmException | io.minio.errors.ServerException
        //         | XmlParserException | IllegalArgumentException | IOException e1) {
        //     e1.printStackTrace();
        //     result += e1.getMessage();
        // }

        // // use file stream
        // OutputStream outputStream = null;
        // try {
        //     // copy stream to temporary file
        //     outputStream = new FileOutputStream(file);
        //     IOUtils.copy(stream, outputStream);

        //     // find out ifc version from tag FILE_SCHEMA in the file
        //     BufferedReader reader2 = new BufferedReader(new FileReader(file));
        //     while (reader2.readLine() != null  && version == null) {
        //         String line = reader2.readLine();
        //         if (line.contains("FILE_SCHEMA")) {
        //             if (line.toUpperCase().contains("IFC4")) {
        //                 version = "IFC4";
        //             } else if (line.toUpperCase().contains("IFC2X3")) {
        //                 version = "IFC2X3TC1";
        //             }
        //         }
        //     }
        //     reader2.close();

        // } catch (IOException e) {
        //     log.info(e.getMessage());
        // } finally{
        //     stream.close();
        //     outputStream.close();
        // }

        // connect to BIMserver
        BimServerClient bimClient = bimserver.getConnection();

        // create new project with name of the file
        SProject newProject = null;
        try {
            newProject = bimClient.getServiceInterface().addProject(pName, ifcFile.getVersion());
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
            result += e.getMessage();
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

       // remove all entities with geometry context 
       public Path createIfcPropertyFile(IfcFile ifcFile) throws FileNotFoundException, IOException, InterruptedException {
        File file = ifcFile.getFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            String l = "";
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            List<String> keysToClear = new ArrayList<String>();
            Set<String> keys = new HashSet<String>();
            StringBuffer header = new StringBuffer();
            StringBuffer end = new StringBuffer();
            boolean data = false;

            // read line by line and decide, what to do with it 
            while ((line = br.readLine()) != null) {
                l += line;
                if (!line.isEmpty() && line.charAt(line.length() - 1) != ';') {
                    continue;
                } else {
                    if (l.startsWith("#")) {
                        String[] split = l.split("=", 2);
                        String value = split[1].trim();
                        // identify lines with geometry content
                        if (value.startsWith("IFCLOCALPLACEMENT")
                                || value.startsWith("IFCPRODUCTDEFINITIONSHAPE")
                                || value.startsWith("IFCAXIS2PLACEMENT")
                                || value.startsWith("IFCSTYLEDITEM")
                                || value.startsWith("IFCPRESENTATIONLAYERASSIGNMENT")
                                || value.startsWith("IFCPRESENTATIONSTYLEASSIGNMENT")
                                || value.contains("REPRESENTATION")
                                || value.startsWith("IFCUNITASSIGNMENT")
                                || value.startsWith("IFCFACETEDBREP")
                                || value.startsWith("IFCEXTRUDEDAREASOLID")
                                || value.startsWith("IFCBOOLEANCLIPPINGRESULT")
                                || value.startsWith("IFCCONNECTIONSURFACEGEOMETRY")
                                ) {
                            keysToClear.add(split[0]);
                        }

                        // some times can be ignored directly, because of no needed relations
                        if (value.startsWith("IFCRELCONNECTSPATHELEMENTS")
                                || value.startsWith("IFCRELASSOCIATESMATERIAL")
                                || value.startsWith("IFCPOLYL") // POLYLINE und POLYLOOP
                                || value.startsWith("IFCRELSPACEBOUNDARY")) {
                        } else if (value.startsWith("IFCANNOTATION")
                                || value.startsWith("IFCCARTESIANPOINT")
                                || value.startsWith("IFCDIRECTION")
                                || value.startsWith("IFCMATERIAL")) {
                            keys.add(split[0]);
                        } else {
                            map.put(split[0], value);
                        }

                    // copy lines around DATA section to new file
                    } else if (!data) {
                        if (l.equals("DATA;"))
                            data = true;
                        header.append(l + "\n");
                    } else {
                        end.append(l + "\n");
                    }
                    l = "";
                }
            }

            // find references in lines and delete this lines until no new references were found
            do {
                Set<String> uniqueKeys = new HashSet<>(keysToClear);
                keys.addAll(uniqueKeys);
                keysToClear.clear();

                uniqueKeys.forEach(key -> {
                    try {
                        String value = map.get(key);
                        String[] s = value.split("[,()]");
                        for (String t : s) {
                            if (t.startsWith("#")) {
                                keysToClear.add(t);
                            }
                        }
                        map.remove(key);
                    } catch (Exception e) {
                    }
                });
            } while (keysToClear.size() > 0);

            // remove references to delete lines in remaining lines
            // to delete only the right references, the chars bevor and after the references need to be looked at it 
            map.forEach((key, value) -> {
                String[] s = value.split("[,()]");
                for (String k : s) {
                    if (k.startsWith("#")) {
                        if (keys.contains(k)) {
                            // special decision for entity IFCRELCONTAINEDINSPATIALSTRUCTURE, because references in set have to be deleted instead ov replaced with $
                            if (s[0].startsWith("IFCRELCONTAINEDINSPATIALSTRUCTURE") && !k.equals(s[3])
                                    && !k.equals(s[4]) && !k.equals(s[5]) && !k.equals(s[s.length - 2])) {
                                if (value.contains("," + k + ",") || value.contains("," + k + ")")) {
                                    value = value.replace("," + k, "");
                                } else {
                                    value = value.replace(k + ",", "");
                                }
                            } else if (value.contains("(" + k + ")")) {
                                value = value.replace("(" + k + ")", "$");
                            } else if (value.contains(",(" + k + ",")) {
                                value = value.replace(k + ",", "");
                            } else if (value.contains("," + k + "),")) {
                                value = value.replace("," + k, "");
                            } else if (value.contains(k + ",") || value.contains(k + ")")) {
                                value = value.replace(k, "$");
                            }
                        }
                    }
                }
                header.append(key + "= " + value + "\n");
            });

            // write new IFC file
            String ifc = header.toString() + end.toString();
            Path path = Paths.get(file.getAbsolutePath().replace(".ifc", "_ohne_Geometrie.ifc"));
            try {
                Files.writeString(path, ifc, StandardCharsets.UTF_8);
                log.info("Ifc property file successfully created");
            } catch (IOException ex) {
                log.error("Failed to create Ifc property file");
            }

            // convertIfcToTtl(path);
            return path;
        }
    }

    // use IFCtoRDF converter to convert IFC to RDF TTL 

    public Path convertIfcToTtl(Path path) {
        String filename = path.getFileName().toString();
        Path ttl = Paths.get(path.toString().replace(".ifc", ".ttl"));
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);
        // processBuilder.command("java", "-jar", "src/main/resources/IFCtoRDF-0.5-shaded.jar", path.toString(),
        //         ttl.toString());
        processBuilder.command("java", "-jar", "/opt/lib/IFCtoRDF-0.5-shaded.jar", path.toString(),
        ttl.toString());
                
        Process process;
        String converterlog;
        // StringBuffer logs = new StringBuffer();
        
        try {
            process = processBuilder.start();
            InputStream stdout = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            boolean err = false;
            while ((converterlog = reader.readLine()) != null) {
                log.info(filename + ": " + converterlog);
                // logs.append(converterlog + "\n");
                if (converterlog.contains("ERROR"))
                    err = true;
            }

            int exitVal = process.waitFor();
            if (exitVal == 0 && err == false) {
                log.info(filename + ": Successful RDF creation");
                // logs.append(filename + ": Successful RDF creation" + "\n");
            } else {
                log.error(filename + ": RDF creation failed");
                // logs.append(filename + ": RDF creation failed" + "\n");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return ttl;
        // Path logfile = Paths.get("src/main/resources/logs.txt");
        // try {
        //     Files.writeString(logfile, logs.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        // } catch (IOException ex) {

        // }
        

    }
}
