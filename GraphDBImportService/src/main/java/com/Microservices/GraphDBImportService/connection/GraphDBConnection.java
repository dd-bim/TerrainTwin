package com.Microservices.GraphDBImportService.connection;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
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

    // connect to GraphDB repository
    public RepositoryConnection connection(String repo) {
        RemoteRepositoryManager manager = new RemoteRepositoryManager(graphdb_url);
        manager.setUsernameAndPassword(graphdb_username, graphdb_password);
        manager.init();
        RepositoryConnection connection = manager.getRepository(repo).getConnection();
        return connection;
    }

}
