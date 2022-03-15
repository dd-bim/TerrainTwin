package com.Microservices.BIMserverQueryService.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
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
    Gson gson = new Gson();


    public Queries() {
    }

    public Queries(String schema) {
        try {
            String path;
            if (schema == "ifc2x3tc1") {
                path = "/query-ifc2x3tc1.json";
            } else {
                path = "/query-ifc4.json";
            }
            InputStream in = this.getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            jObject = JsonParser.parseReader(reader).getAsJsonObject();
            log.info(jObject.getAsString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.info("File read failed: " + e.getMessage());
        }
    }

    public String getWalls(boolean basics) {
        JsonObject obj = jObject.getAsJsonObject("AllWalls").deepCopy();
        if (basics) {
            JsonArray base = jObject.getAsJsonArray("IncludeBasics");
            obj.add("includes", base);
        }
        return obj.toString();
    }

    public String getElementsInBoundingBox(double x, double y, double z, double width, double height, double depth,
            boolean partial, String elements, boolean basics) {
        JsonObject obj = jObject.getAsJsonObject("BoundingBox").deepCopy();

        if (elements != null) {
            JsonArray array = new JsonArray();
            String[] elementList = elements.replaceAll(" ", "").split(",");
            for (String element : elementList) {
                array.add(element);
            }
            obj.add("types", array);
        }

        JsonObject bb = obj.getAsJsonObject("inBoundingBox");
        bb.addProperty("x", x);
        bb.addProperty("y", y);
        bb.addProperty("z", z);
        bb.addProperty("width", width);
        bb.addProperty("height", height);
        bb.addProperty("depth", depth);
        bb.addProperty("partial", partial);

        if (basics) {
            JsonArray base = jObject.getAsJsonArray("IncludeBasics");
            obj.add("includes", base);
        }
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
        return obj.toString();
    }

    public String getExternalWalls(boolean basics) {
        JsonObject obj = jObject.getAsJsonObject("ExternalWalls").deepCopy();
        if (basics) {
            JsonArray base = jObject.getAsJsonArray("IncludeBasics");
            obj.add("includes", base);
        }
        return obj.toString();
    }

    public String getElementsByProperty(String types, String propertySet, String property, String value,
            boolean basics) {
        JsonObject obj = new JsonObject();

        if (types != null) {
            JsonArray array = new JsonArray();
            String[] typeList = types.replaceAll(" ", "").split(",");
            for (String type : typeList) {
                array.add(type);
            }
            obj.add("types", array);
        }
        JsonObject prop = new JsonObject();
        prop.addProperty(property, value);
        JsonObject pSet = new JsonObject();
        pSet.add(propertySet, prop);
        obj.add("properties", pSet);

        if (basics) {
            JsonArray base = jObject.getAsJsonArray("IncludeBasics");
            obj.add("includes", base);
        }
        return obj.toString();
    }

    public String getPropertiesFromElements(String guids, boolean basics) {
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        String[] elementList = guids.replaceAll(" ", "").split(",");
        for (String element : elementList) {
            array.add(element);
        }
        obj.add("guids", array);

        if (basics) {
            JsonArray base = jObject.getAsJsonArray("IncludeBasics").deepCopy();
            JsonArray baseProp = jObject.getAsJsonArray("IncludeAllProperties").deepCopy();
            base.addAll(baseProp);
            obj.add("includes", base);
        } else {
            JsonArray base = jObject.getAsJsonArray("IncludeAllProperties").deepCopy();
            obj.add("includes", base);
        }
        return obj.toString();
    }

    public String getWallsByType(String guids, boolean basics) {
        JsonObject obj = jObject.getAsJsonObject("WallsByType").deepCopy();
        JsonArray array = new JsonArray();
        String[] elementList = guids.replaceAll(" ", "").split(",");
        for (String element : elementList) {
            array.add(element);
        }
        obj.add("guids", array);

        if (basics) {
            JsonObject wall = obj.get("include").getAsJsonObject().get("include").getAsJsonObject();
            JsonArray base = jObject.getAsJsonArray("IncludeBasics");
            wall.add("includes", base);
        }
        return obj.toString();
    }

    public String getElementsInStorey(String guid, String types) {
        JsonObject obj = jObject.getAsJsonObject("ElementsInStorey").deepCopy();
        if (guid != null) {
            obj.addProperty("guid", guid);
        }
        if (types != null) {
            JsonArray arr = obj.getAsJsonArray("includes");
            JsonObject storey = arr.get(1).getAsJsonObject().get("include").getAsJsonObject();
            JsonArray outType = new JsonArray();
            String[] typeList = types.replaceAll(" ", "").split(",");
            for (String type : typeList) {
                outType.add(type);
            }
            storey.add("outputTypes", outType);
        }
        return obj.toString();
    }
}
