package com.Microservices.GraphDBImportService.service;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;

import io.github.cdimascio.dotenv.Dotenv;

public class GraphDBConnection {

    RepositoryConnection connection;
    Dotenv env = Dotenv.configure().directory("./GraphDBImportService").load();

    public RepositoryConnection connect(String repo) throws Exception {
        System.out.println(env.get("GRAPHDB_URL") + repo);
        RemoteRepositoryManager manager = new RemoteRepositoryManager(env.get("GRAPHDB_URL"));
        manager.setUsernameAndPassword(env.get("GRAPHDB_USERNAME"), env.get("GRAPHDB_PASSWORD"));
        manager.init();
        try {
            connection = manager.getRepository(repo).getConnection();
        } catch (Exception e) {
            return null;
        }

        return connection;
    }
}