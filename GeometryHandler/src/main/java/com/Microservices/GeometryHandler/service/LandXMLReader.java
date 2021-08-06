package com.Microservices.GeometryHandler.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.Microservices.GeometryHandler.connection.FileInputHandlerConnection;
import com.Microservices.GeometryHandler.domain.model.PostgresInfos;
import com.Microservices.GeometryHandler.repositories.BreaklinesRepository;
import com.Microservices.GeometryHandler.repositories.SpecialPointsRepository;
import com.Microservices.GeometryHandler.repositories.TINRepository;
import com.Microservices.GeometryHandler.schemas.Breaklines;
import com.Microservices.GeometryHandler.schemas.SpecialPoints;
import com.Microservices.GeometryHandler.schemas.TIN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LandXMLReader {
    
    @Autowired
    TINRepository tinRepository;

    @Autowired
    BreaklinesRepository blRepository;

    @Autowired
    SpecialPointsRepository spRepository;

    @Autowired
    FileInputHandlerConnection graphdb;

    Logger log = LoggerFactory.getLogger(LandXMLReader.class);

    @Value("${domain.url}")
    private String domain;

    ArrayList<String> breaklines = new ArrayList<String>();
    HashMap<Integer, String> points = new HashMap<Integer, String>();
    HashMap<Integer, String> cgpoints = new HashMap<Integer, String>();
    ArrayList<int[]> faces = new ArrayList<int[]>();

    public String importTIN(InputStream stream, String path, String filename, String graphdbRepo) throws Exception {

        int srid = 25832; // muss noch variabel gestaltet werden
        int blCount = 0;
        String urlPrefix = domain + "/geometry/export/collections/";
        readFile(stream);

        TIN tin = new TIN("SRID=" + srid + ";" + buildWktTIN());
        tinRepository.save(tin);
        log.info("'ID: " + tin.getId() + ", WKT: " + tin.getGeometry() + "'");

        String postgresUrl = urlPrefix + "dtm_tin" + "/items/" + tin.getId();
        PostgresInfos p = new PostgresInfos(-1, tin.getId(), postgresUrl, 4, 3, filename, path, graphdbRepo);
        graphdb.graphdbImport(p);

        for (int i = 0; i < breaklines.size(); i++) {
            Breaklines bl = new Breaklines(tin.getId(), "SRID=" + srid + ";" + getBreaklines(i));
            blRepository.save(bl);
            blCount++;
            log.info("'ID: " + bl.getId() + ", WKT: " + bl.getGeometry() + ", tin_id: " + bl.getTin_id() + "'");
            

            String postgresUrlBl = urlPrefix + "dtm_breaklines" + "/items/" + bl.getId();
            PostgresInfos pBl = new PostgresInfos(-1, bl.getId(), postgresUrlBl, 1, 3, filename, path, graphdbRepo);
            graphdb.graphdbImport(pBl);
        }

        cgpoints.forEach((key, value) -> {
            SpecialPoints spPoint = new SpecialPoints(tin.getId() ,
            "SRID=" + srid + ";POINTZ (" + value + ")");
            spRepository.save(spPoint);

            String postgresUrlSp = urlPrefix + "dtm_specialpoints" + "/items/" + spPoint.getId();
            PostgresInfos pBl = new PostgresInfos(-1, spPoint.getId(), postgresUrlSp, 0, 3, filename, path, graphdbRepo);
            graphdb.graphdbImport(pBl);
        });

        return "TIN with " + blCount + " Breaklines and " + cgpoints.size() + " special points has been imported.";
    }

    // file has only one TIN
    private void readFile(InputStream in) throws Exception {

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);

        String element = "";
        while (streamReader.hasNext()) {
            if (streamReader.isStartElement()) {

                if (streamReader.getLocalName().equals("CgPoint")) {
                    cgpoints.put(Integer.parseInt(streamReader.getAttributeValue(0)), streamReader.getElementText());
                }

                if (element.equals("Breakline") && streamReader.getLocalName().equals("PntList3D")) {
                    breaklines.add(streamReader.getElementText());
                }

                if (streamReader.getLocalName().equals("P")) {
                    points.put(Integer.parseInt(streamReader.getAttributeValue(0)), streamReader.getElementText());
                }

                if (streamReader.getLocalName().equals("F")) {
                    String[] face = streamReader.getElementText().split(" ");
                    int[] intFace = new int[] { Integer.parseInt(face[0]), Integer.parseInt(face[1]),
                            Integer.parseInt(face[2]) };
                    faces.add(intFace);
                }
                element = streamReader.getLocalName();
            }
            streamReader.next();
        }
    }

    private String buildWktTIN() {
        log.info("Import TIN");
        StringBuilder wkt = new StringBuilder();
        wkt.append("TIN (");
        for (int[] face : faces) {
            String s = "((";
            for (int point : face) {
                String[] p = points.get(point).split(" ");
                s = s + p[1] + " " + p[0] + " " + p[2] + ", ";
            }
            String[] q = points.get(face[0]).split(" ");
            s = s + q[1] + " " + q[0] + " " + q[2] + ")), ";
            wkt.append(s);
        }
        wkt.setLength(wkt.length() - 2);
        wkt.append(" )");
        return wkt.toString();
    }

    private String getBreaklines(int i) {
        log.info("Import Breakline");
        String line = "LINESTRING (";
        String[] points = breaklines.get(i).split(" ");
        for (int j = 0; j < points.length; j = j + 3) {
            line = line + points[j + 1] + " " + points[j] + " " + points[j + 2] + ", ";
        }
        line = line.substring(0, line.length() - 2) + ")";
        return line;
    }
}
