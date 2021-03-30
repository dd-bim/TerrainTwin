package com.Microservices.MinIOUploadService.domain.model;

import lombok.Getter;

@Getter
public class DTM {

    private String aktualitaet;
    private String lagereferenzsystem;
    private String hoehenreferenzsystem;
    private String projektion;
    private String raeumlicheAusdehnung;
    private String datenstruktur;
    private String darstellungsform;
    private String erfassungsmethode;
    private String messgenauigkeit;
    private String innereGenauigkeit;
    private String aeußereGenauigkeit;

    public DTM() {

    }

    public DTM(String aktualitaet, String lagereferenzsystem, String hoehenreferenzsystem, String projektion, String raeumlicheAusdehnung,
            String datenstruktur, String darstellungsform, String erfassungsmethode, String messgenauigkeit,
            String innereGenauigkeit, String aeußereGenauigkeit) {

        this.aktualitaet = aktualitaet;
        this.lagereferenzsystem = lagereferenzsystem;
        this.hoehenreferenzsystem = hoehenreferenzsystem;
        this.projektion = projektion;
        this.raeumlicheAusdehnung = raeumlicheAusdehnung;
        this.datenstruktur = datenstruktur;
        this.darstellungsform = darstellungsform;
        this.erfassungsmethode = erfassungsmethode;
        this.messgenauigkeit = messgenauigkeit;
        this.innereGenauigkeit = innereGenauigkeit;
        this.aeußereGenauigkeit = aeußereGenauigkeit;

    }
}
