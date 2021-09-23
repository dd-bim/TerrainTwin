package com.Microservices.SemanticConnector.connection;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphDBConnection {

    @Value("${graphdb.url}")
    private String graphdb_url;
    @Value("${graphdb.username}")
    private String graphdb_username;
    @Value("${graphdb.password}")
    private String graphdb_password;

    Logger log = LoggerFactory.getLogger(GraphDBConnection.class);

    // connect to GraphDB repository
    public RepositoryConnection connection(String repo) {
        String endpoint = graphdb_url + "/repositories/" + repo;
        HTTPRepository connection = new HTTPRepository(endpoint);
        connection.setUsernameAndPassword(graphdb_username, graphdb_password);
        connection.init();
        RepositoryConnection rc = connection.getConnection();
        
        return rc;
    }

}
