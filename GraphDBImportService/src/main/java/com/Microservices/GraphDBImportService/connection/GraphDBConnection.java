package com.Microservices.GraphDBImportService.connection;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryImplConfig;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.sail.config.SailRepositoryConfig;
import org.eclipse.rdf4j.sail.config.SailImplConfig;
import org.eclipse.rdf4j.sail.memory.config.MemoryStoreConfig;
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
        RemoteRepositoryManager manager = new RemoteRepositoryManager(graphdb_url);
        manager.setUsernameAndPassword(graphdb_username, graphdb_password);
        manager.init();
        RepositoryConnection connection;
        try {
            connection = manager.getRepository(repo).getConnection();
        } catch (Exception r) {
            log.info(r.getMessage());
            log.info(repo + " didn't exist. Trying to create it.");
            SailImplConfig backendConfig = new MemoryStoreConfig();
            RepositoryImplConfig repositoryTypeSpec = new SailRepositoryConfig(backendConfig);
            RepositoryConfig repConfig = new RepositoryConfig(repo, repositoryTypeSpec);
            manager.addRepositoryConfig(repConfig);
            log.info("Created new repository " + repo + ".");
            connection = manager.getRepository(repo).getConnection();
        }
        log.info("Connected to repository " + repo + ".");
        return connection;
    }
}
