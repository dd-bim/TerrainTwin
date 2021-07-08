create schema if not exists terraintwinv2;

create table if not exists terraintwinv2.point_2d (id uuid PRIMARY KEY , origin_id int, geometry geometry(POINT,25832));

create table if not exists terraintwinv2.point_3d (id uuid PRIMARY KEY , origin_id int, geometry geometry(POINTZ,25832));

create table if not exists terraintwinv2.line_2d (id uuid PRIMARY KEY , origin_id int, geometry geometry(LINESTRING,25832));

create table if not exists terraintwinv2.line_3d (id uuid PRIMARY KEY , origin_id int, geometry geometry(LINESTRINGZ,25832));

create table if not exists terraintwinv2.polygon_2d (id uuid PRIMARY KEY , origin_id int, geometry geometry(POLYGON,25832));

create table if not exists terraintwinv2.polygon_3d (id uuid PRIMARY KEY , origin_id int, geometry geometry(POLYGONZ,25832));

create table if not exists terraintwinv2.solid (id uuid PRIMARY KEY , origin_id int, geometry geometry(POLYHEDRALSURFACEZ,25832));
	
create table if not exists terraintwinv2.dtm_tin (id uuid PRIMARY KEY , geometry geometry(TINZ,25832));	
	
create table if not exists terraintwinv2.dtm_breaklines (id uuid PRIMARY KEY , geometry geometry(LINESTRINGZ,25832), tin_id uuid, CONSTRAINT dtm_breaklines_fk FOREIGN KEY (tin_id) REFERENCES terraintwinv2.dtm_tin (id));

create table if not exists terraintwinv2.dtm_specialpoints (id uuid PRIMARY KEY , geometry geometry(POINTZ,25832), tin_id uuid, CONSTRAINT dtm_specialPoints_fk FOREIGN KEY (tin_id) REFERENCES terraintwinv2.dtm_tin (id));

create table if not exists terraintwinv2.dtm_embarkment (id uuid PRIMARY KEY, geometry geometry(POLYGON,25832), tin_id uuid, CONSTRAINT dtm_embarkment_fk FOREIGN KEY (tin_id) REFERENCES terraintwinv2.dtm_tin (id));
