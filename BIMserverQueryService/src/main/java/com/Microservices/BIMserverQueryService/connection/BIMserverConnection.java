package com.Microservices.BIMserverQueryService.connection;

import java.net.HttpURLConnection;
import java.net.URL;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BIMserverConnection {

    Logger log = LoggerFactory.getLogger(BIMserverConnection.class);

    public String getConnection() {
String result = "";
        try {
			JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:9005");
            // JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:9005");
			BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo("sebastian.schilling@htw-dresden.de", "Master19!"));
			
			SProject newProject = client.getServiceInterface().addProject("Test1", "ifc2x3tc1");
			System.out.println(newProject.getOid());
            result = "Oid: " + newProject.getOid();
		} catch (BimServerClientException e) {
			e.printStackTrace();
            result = e.getMessage();
		} catch (ServiceException e) {
			e.printStackTrace();
            result = e.getMessage();
		} catch (ChannelConnectionException e) {
			e.printStackTrace();
            result = e.getMessage();
		}
        
        return result;
    }
}
