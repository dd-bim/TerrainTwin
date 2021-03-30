package com.Microservices.MinIOUploadService.domain.model;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metadata {

    @Hidden
    private UUID id;
    @Hidden
    private String name;
    @Hidden
    private String created;
    @Hidden
    private String location;
    @Hidden
    private String mimetype;
    private String type;
    private String description;
    private String creator;
    private String sender;
    private String recipients;
    private String suitability;
    private String revision;
    private String version;
    private String status;
    private String projectId;
    private String metadataSchema;
    private String schema;
    private String schemaVersion;
    private String schemaSubset;

    public Metadata() {

    }

    public Metadata(String type, String description, String creator, String sender, String recipients,
            String suitability, String revision, String version, String status, String projectId, String metadataSchema,
            String schema, String schemaVersion, String schemaSubset) {

        this.type = type;
        this.description = description;
        this.creator = creator;
        this.sender = sender;
        this.recipients = recipients;
        this.suitability = suitability;
        this.revision = revision;
        this.version = version;
        this.status = status;
        this.projectId = projectId;
        this.metadataSchema = metadataSchema;
        this.schema = schema;
        this.schemaVersion = schemaVersion;
        this.schemaSubset = schemaSubset;
    }

    public Metadata(UUID id, String name, String created, String mimetype, String type, String description,
            String creator, String sender, String recipients, String suitability, String revision, String version,
            String status, String projectId, String metadataSchema, String schema, String schemaVersion,
            String schemaSubset) {

        this.id = id;
        this.name = name;
        this.created = created;
        this.mimetype = mimetype;
        this.type = type;
        this.description = description;
        this.creator = creator;
        this.sender = sender;
        this.recipients = recipients;
        this.suitability = suitability;
        this.revision = revision;
        this.version = version;
        this.status = status;
        this.projectId = projectId;
        this.metadataSchema = metadataSchema;
        this.schema = schema;
        this.schemaVersion = schemaVersion;
        this.schemaSubset = schemaSubset;
    }

    public Metadata(String bucket, String name, String created, String type, String description, String creator,
            String sender, String recipients, String suitability, String revision, String version, String status,
            String projectId, String metadataSchema, String schema, String schemaVersion, String schemaSubset) {

        this.id = UUID.randomUUID();
        this.created = created;
        this.name = created + "_" + name;
        this.location = "https://terrain.dd-bim.org" + "/minio/" + bucket + "/" + this.name;

        String ext = name.split("\\.")[1];
        switch (ext) {
        case "ifc":
            this.mimetype = "application/x-step";
            break;
        case "dwg":
            this.mimetype = "application/acad";
            break;
        case "dxf":
            this.mimetype = "application/dxf";
            break;
        case "gml":
            this.mimetype = "application/gml+xml";
            break;
        case "ttl":
            this.mimetype = "text/turtle";
            break;
        case "owl":
            this.mimetype = "application/rdf+xml";
            break;
        case "xml":
            this.mimetype = "application/xml";
            break;
        default:
            this.mimetype = "";
        }

        this.type = type;
        this.description = description;
        this.creator = creator;
        this.sender = sender;
        this.recipients = recipients;
        this.suitability = suitability;
        this.revision = revision;
        this.version = version;
        this.status = status;
        this.projectId = projectId;
        this.metadataSchema = metadataSchema;
        this.schema = schema;
        this.schemaVersion = schemaVersion;
        this.schemaSubset = schemaSubset;
    }
}
