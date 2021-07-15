package com.Microservices.FileInputHandler.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
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
        
        // if repo not exists, create it and connect to them
        // RepositoryConnection rc = null;
        // try {
        //     rc = connection.getConnection();
        // } catch (Exception r) {
        //     log.info(r.getMessage());
        //     log.info(repo + " didn't exist. Trying to create it.");
        //     try {
        //         createRepo(repo);
        //         rc = connection.getConnection();
        //         log.info("Created repository.");
        //     } catch (Exception e) {
        //         log.info(e.getMessage());
        //     }

        // }
        return rc;
    }

    // create a new GraphDB repository
    public void createRepo(String repo) throws RDFParseException, RDFHandlerException, IOException {
        String file = "/var/config/repo-config.ttl";
        TreeModel graph = new TreeModel();

        // init connection to GraphDB
        RemoteRepositoryManager manager = new RemoteRepositoryManager(graphdb_url);
        manager.setUsernameAndPassword(graphdb_username, graphdb_password);
        manager.init();

        // Read repository configuration file
        InputStream config = new FileInputStream(file);
        RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
        rdfParser.setRDFHandler(new StatementCollector(graph));
        rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
        config.close();

        // Retrieve the repository node as a resource
        Resource repositoryNode = graph.filter(null, RDF.TYPE, RepositoryConfigSchema.REPOSITORY).subjects().iterator()
                .next();
        
        // create new repository configuration with the default file, set the repository id and add it to GraphDB
        RepositoryConfig repConfig = new RepositoryConfig();
        repConfig.parse(graph, repositoryNode);
        repConfig.setID(repo);
        manager.addRepositoryConfig(repConfig);
    }

}
