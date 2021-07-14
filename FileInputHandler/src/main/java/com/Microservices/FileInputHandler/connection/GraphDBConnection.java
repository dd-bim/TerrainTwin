package com.Microservices.FileInputHandler.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ontotext.trree.MemoryConfig;

import org.apache.http.client.HttpClient;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ModelFactory;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
import org.eclipse.rdf4j.repository.config.RepositoryImplConfig;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.http.config.HTTPRepositoryFactory;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.sail.config.SailRepositoryConfig;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
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
        String endpoint = graphdb_url + "/repositories/" + repo;
        HTTPRepository connection = new HTTPRepository(endpoint);
        connection.setUsernameAndPassword(graphdb_username, graphdb_password);
        connection.init();
        RepositoryConnection rc = connection.getConnection();

        return rc;
    }

    // connect to GraphDB repository
    // public RepositoryConnection connection(String repo) throws RDFParseException,
    // RDFHandlerException, IOException {
    // RemoteRepositoryManager manager = new RemoteRepositoryManager(graphdb_url);
    // manager.setUsernameAndPassword(graphdb_username, graphdb_password);
    // manager.init();
    // // RepositoryConnection connection;
    // RepositoryConnection connection =
    // manager.getRepository(repo).getConnection();

    // try {
    // connection = manager.getRepository(repo).getConnection();
    // } catch (Exception r) {
    // log.info(r.getMessage());
    // log.info(repo + " didn't exist. Trying to create it.");
    // SailImplConfig backendConfig = new MemoryStoreConfig(true);
    // RepositoryImplConfig repositoryTypeSpec = new
    // SailRepositoryConfig(backendConfig);
    // RepositoryConfig repConfig = new RepositoryConfig(repo, repositoryTypeSpec);
    // manager.addRepositoryConfig(repConfig);

    // RepositoryManager repositoryManager = new LocalRepositoryManager(new
    // File("."));
    // repositoryManager.init();

    // // Instantiate a repository graph model
    // TreeModel graph = new TreeModel();

    // // Read repository configuration file
    // InputStream config =
    // RepositoryConnection.class.getResourceAsStream("/var/repo-defaults.ttl");
    // if(config == null) System.out.println("Config empty");
    // RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
    // rdfParser.setRDFHandler(new StatementCollector(graph));
    // rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
    // config.close();

    // // Retrieve the repository node as a resource
    // // Resource repositoryNode = GraphUtil.getUniqueSubject(graph, RDF.TYPE,
    // RepositoryConfigSchema.REPOSITORY);
    // Resource repositoryNode = graph.filter(null, RDF.TYPE,
    // RepositoryConfigSchema.REPOSITORY).subjects().iterator().next();
    // System.out.println(repositoryNode.toString());
    // System.out.println(graph.toString());
    // // Create a repository configuration object and add it to the
    // repositoryManager
    // RepositoryConfig repositoryConfig = RepositoryConfig.create(graph,
    // repositoryNode);
    // repositoryManager.addRepositoryConfig(repositoryConfig);

    // Process process = Runtime.getRuntime().exec("curl -G
    // https://terrain.dd-bim.org/graphdb/rest/repositories -H 'Accept:
    // application/json' -u graphdb:123456");
    // Process process = Runtime.getRuntime().exec("curl -X POST
    // https://terrain.dd-bim.org/graphdb/rest/repositories -H 'Content-Type:
    // multipart/form-data' -F \"config=@/var/repo-defaults.ttl\" -u
    // graphdb:123456");
    // System.out.println(process.info().toString() + process.exitValue());

    // log.info("Created new repository " + repo + ".");
    // connection = manager.getRepository(repo).getConnection();

    // }
    // log.info("Connected to repository " + repo + ".");

    // return connection;
    // }

    public void createRepo() throws RDFParseException, RDFHandlerException, IOException
    {
        String file = "/var/config/repo-config.ttl";
    
    //     // Instantiate a repository graph model
    // TreeModel graph = new TreeModel();

    // // Read repository configuration file
    // InputStream config =
    // RepositoryConnection.class.getResourceAsStream(file);
    // RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
    // rdfParser.setRDFHandler(new StatementCollector(graph));
    // rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
    // config.close();

    // // Retrieve the repository node as a resource
    // // Resource repositoryNode = GraphUtil.getUniqueSubject(graph, RDF.TYPE,
    // // RepositoryConfigSchema.REPOSITORY);
    // Resource repositoryNode = graph.filter(null, RDF.TYPE,
    // RepositoryConfigSchema.REPOSITORY).subjects().iterator().next();
    // System.out.println(repositoryNode.toString());
    // System.out.println(graph.toString());
    //     RepositoryConfig repositoryConfig = RepositoryConfig.create(graph,
    // repositoryNode);

    //     HTTPRepositoryFactory factory = new HTTPRepositoryFactory();

    //     RepositoryConnection connection = null;




    Path path = Paths.get(".").toAbsolutePath().normalize();
    String strRepositoryConfig = path.toFile().getAbsolutePath() + file;
      
    // Instantiate a local repository manager and initialize it
    RepositoryManager repositoryManager = new LocalRepositoryManager(new File("."));
    repositoryManager.init();
    // repositoryManager.getAllRepositories();

    // Instantiate a repository graph model
    TreeModel graph = new TreeModel();

    // Read repository configuration file
    InputStream config = new FileInputStream(strRepositoryConfig);
    RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
    rdfParser.setRDFHandler(new StatementCollector(graph));
    rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
    config.close();

    // Retrieve the repository node as a resource
    Resource repositoryNode =  Models.subject(graph
       .filter(null, RDF.TYPE, RepositoryConfigSchema.REPOSITORY))
       .orElseThrow(() -> new RuntimeException(
           "Oops, no <http://www.openrdf.org/config/repository#> subject found!"));

    // Create a repository configuration object and add it to the repositoryManager
    RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
    repositoryManager.addRepositoryConfig(repositoryConfig);


    // log.info(repositoryManager.getAllRepositories().toString());
    // Get the repository from repository manager, note the repository id
    // set in configuration .ttl file
    // Repository repository = repositoryManager.getRepository("funktioniert");

    // // Open a connection to this repository
    // RepositoryConnection repositoryConnection = repository.getConnection();

    // // ... use the repository

    // // Shutdown connection, repository and manager
    // repositoryConnection.close();
    // repository.shutDown();
    repositoryManager.shutDown();					


    // HTTPRepository connection = new HTTPRepository(graphdb_url + "/repositories/");
    // connection.setUsernameAndPassword(graphdb_username, graphdb_password);
    // connection.init();
    // RepositoryConnection rc = connection.getConnection();
    // rc.add(new File(file), null, RDFFormat.TURTLE);
    }

}