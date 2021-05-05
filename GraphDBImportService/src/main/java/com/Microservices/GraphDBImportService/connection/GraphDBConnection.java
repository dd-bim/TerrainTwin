package com.Microservices.GraphDBImportService.connection;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;

public class GraphDBConnection {

    // connect to GraphDB repository
    public RepositoryConnection connection(String graphdb_url, String graphdb_username, String graphdb_password,
            String repo) {
        RemoteRepositoryManager manager = new RemoteRepositoryManager(graphdb_url);
        manager.setUsernameAndPassword(graphdb_username, graphdb_password);
        manager.init();
        RepositoryConnection connection = manager.getRepository(repo).getConnection();
        return connection;
    }

}
