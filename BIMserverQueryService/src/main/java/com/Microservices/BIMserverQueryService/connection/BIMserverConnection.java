package com.Microservices.BIMserverQueryService.connection;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BIMserverConnection {

    Logger log = LoggerFactory.getLogger(BIMserverConnection.class);

    public BimServerClient getConnection() {
        JsonBimServerClientFactory factory;
        BimServerClient client = null;

        try {
            // BIMserver connection in docker network needs to be on internal port 8080
			factory = new JsonBimServerClientFactory("http://bimserver:8080");
			client = factory.create(new UsernamePasswordAuthenticationInfo("sebastian.schilling@htw-dresden.de", "Master19!"));
			
		} catch (BimServerClientException e) {
			e.printStackTrace();
            log.error(e.getMessage(), e);
		} catch (ServiceException e) {
			e.printStackTrace();
            log.error(e.getMessage(), e);
		} catch (ChannelConnectionException e) {
			e.printStackTrace();
            log.error(e.getMessage(), e);
		}
        
        return client;
    }
}
