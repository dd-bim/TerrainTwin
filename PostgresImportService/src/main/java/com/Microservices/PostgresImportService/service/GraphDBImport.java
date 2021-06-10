package com.Microservices.PostgresImportService.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphDBImport {

    Logger log = LoggerFactory.getLogger(GraphDBImport.class);

    public String graphdbImport(int originId, int id, String type, String table, String filename, String path,
            String graphdbRepo) {
                String conn = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode infos = mapper.createObjectNode();
            String postgresUrl = "http://localhost:7203/postgres/" + table + "/id/" + id;
            infos.put("originId", originId).put("id", id).put("url", postgresUrl).put("type", type)
                    .put("filename", filename).put("path", path).put("graphdbRepo", graphdbRepo);
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(infos);

            HttpResponse response = postRequestGraphDBImport(json);

            System.out.println(response.toString());
            if (response.getStatusLine().getStatusCode() == 200) {
                log.info("Import in GraphDB successfully finished.");
            }

        } catch (Exception e) {
            log.error("HttpRequest failed. \n " + e.getMessage());
            conn = "HttpRequest failed. \n " + e.getMessage();
        }
        return conn;
    }

    HttpResponse postRequestGraphDBImport(String json) throws ClientProtocolException, IOException {
        return Request.Post("http://172.17.0.1:7201/graphdbimport/postgresinfos")  //http://host.docker.internal:7201/graphdbimport/postgresinfos
                // .viaProxy(new HttpHost("myproxy", 8080))
                .bodyString(json, ContentType.APPLICATION_JSON).execute().returnResponse();
    }

}
