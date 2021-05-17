package com.Microservices.PostgresImportService.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.Microservices.PostgresImportService.repositories.BreaklinesRepository;
import com.Microservices.PostgresImportService.repositories.SpecialPointsRepository;
import com.Microservices.PostgresImportService.repositories.TINRepository;
import com.Microservices.PostgresImportService.schemas.Breaklines;
import com.Microservices.PostgresImportService.schemas.SpecialPoints;
import com.Microservices.PostgresImportService.schemas.TIN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LandXMLReader {

    @Autowired
    TINRepository tinRepository;

    @Autowired
    BreaklinesRepository blRepository;

    @Autowired
    SpecialPointsRepository spRepository;

    Logger log = LoggerFactory.getLogger(LandXMLReader.class);

    ArrayList<String> breaklines = new ArrayList<String>();
    HashMap<Integer, String> points = new HashMap<Integer, String>();
    HashMap<Integer, String> cgpoints = new HashMap<Integer, String>();
    ArrayList<int[]> faces = new ArrayList<int[]>();

    public String importTIN(InputStream stream) throws Exception {

        int srid = 25832; // muss noch variabel gestaltet werden
        int blCount = 0;
        readFile(stream);



        TIN tin = new TIN("SRID=" + srid + ";" + buildWktTIN());
        tinRepository.save(tin);
        log.info("'ID: " + tin.getTin_id() + ", WKT: " + tin.getGeometry() + "'");

        for (int i = 0; i < breaklines.size(); i++) {
            Breaklines bl = new Breaklines(tin.getTin_id(), "SRID=" + srid + ";" + getBreaklines(i));
            blRepository.save(bl);
            blCount++;
            log.info("'ID: " + bl.getBl_id() + ", WKT: " + bl.getGeometry() + ", tin_id: " + bl.getTin_id() + "'");
        }

        cgpoints.forEach((key, value) -> {
            SpecialPoints spPoint = new SpecialPoints(key, tin.getTin_id() ,
            "SRID=" + srid + ";POINTZ (" + value + ")");
            spRepository.save(spPoint);
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
