package com.Microservices.BIMserverQueryService.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Queries {

    Logger log = LoggerFactory.getLogger(Queries.class);
    public static JsonObject jObject = null;

    public Queries() {
        try {
            // BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/query-ifc2x3tc1.json"),
            //         StandardCharsets.UTF_8);
            // String path = this.getClass().getClassLoader().getResource("query-ifc2x3tc1.json").toExternalForm();
            // log.info(path);
            // BufferedReader reader = Files.newBufferedReader(Paths.get(path),
            //         StandardCharsets.UTF_8);
            InputStream in = this.getClass().getResourceAsStream("/query-ifc2x3tc1.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            // JsonParser parser = new JsonParser();
            // JsonElement tree = parser.parse(reader);
            // jObject = tree.getAsJsonObject();
 
            // jObject = JsonParser.parseString().getAsJsonObject();
            jObject = JsonParser.parseReader(reader).getAsJsonObject();
            log.info(jObject.getAsString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.info("File read failed: " + e.getMessage());
        }
    }

    public String getWalls() {
        return jObject.getAsJsonObject("AllWalls").toString();

    }

    public String getStoreyWalls(String guid) {
        JsonObject obj = jObject.getAsJsonObject("WallsInStorey");
        if (guid != null) {
            obj.addProperty("guid", guid);
        }
        return obj.toString();
    }

    public String getInBoundingBox(double x, double y, double z, double width, double height, double depth,
            boolean partial) {
        JsonObject obj = jObject.getAsJsonObject("BoundingBox");
        JsonObject bb = obj.getAsJsonObject("inBoundingBox");
        bb.addProperty("x", x);
        bb.addProperty("y", y);
        bb.addProperty("z", z);
        bb.addProperty("width", width);
        bb.addProperty("height", height);
        bb.addProperty("depth", depth);
        bb.addProperty("partial", partial);

        return obj.toString();
    }

    public String getElements(String elements, boolean basics) {
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        String[] elementList = elements.replaceAll(" ", "").split(",");
        for (String element : elementList) {
            array.add(element);
        }

        obj.add("types", array);
        if (basics) {
            JsonArray base = jObject.getAsJsonArray("IncludeBasics");
            obj.add("includes", base);
        }
        System.out.println(obj.toString());
        return obj.toString();
    }
}
