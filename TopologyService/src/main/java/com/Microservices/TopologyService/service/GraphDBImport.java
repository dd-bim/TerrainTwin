package com.Microservices.TopologyService.service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class GraphDBImport {

    @Value("domain.url")
    private String domain;

    Logger log = LoggerFactory.getLogger(GraphDBImport.class);

    // public String graphdbImport(ArrayList<String[]> relations) {
        public String graphdbImport() {
                String conn = "";
        try {
            // ObjectMapper mapper = new ObjectMapper();
            // ObjectNode infos = mapper.createObjectNode();
            // String postgresUrl = domain + "/postgres/" + table + "/id/" + id;
            // infos.put("originId", originId).put("id", id.toString()).put("url", postgresUrl).put("type", type)
            //         .put("filename", filename).put("path", path).put("graphdbRepo", graphdbRepo);
            // String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(infos);

            // ArrayList<String> st = new ArrayList<String>();
            // relations.forEach((r) -> {
            //     String s = new Gson().toJson(r);
            //     st.add(s);
            // });
            // String json = new Gson().toJson(st);
// log.info(json);

URL url = new URL("http://host.docker.internal:7201/graphdbimport/topology");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);//5 secs
    connection.setReadTimeout(5000);//5 secs

    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");

    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());  
    // out.write(relations.toString());
    out.write("hello world!");
    out.flush();
    out.close();

    int res = connection.getResponseCode();

    System.out.println(res);

            // HttpResponse response = postRequestGraphDBImport(json);

            // System.out.println(response.toString());
            // if (response.getStatusLine().getStatusCode() == 200) {
            //     log.info("Import in GraphDB successfully finished.");
            // }

        } catch (Exception e) {
            log.error("HttpRequest failed. \n " + e.getMessage());
            conn = "HttpRequest failed. \n " + e.getMessage();
        }
        return conn;
    }

    HttpResponse postRequestGraphDBImport(String json) throws ClientProtocolException, IOException {
        return Request.Post("http://graphdbimporter:7201/graphdbimport/postgresinfos")  //"http://172.17.0.1:7201/graphdbimport/postgresinfos" http://host.docker.internal:7201/graphdbimport/postgresinfos
                // .viaProxy(new HttpHost("myproxy", 8080))
                // .viaProxy(new HttpHost("graphdb-import-service", 7201))
                .bodyString(json, ContentType.APPLICATION_JSON).execute().returnResponse();
    }

}
