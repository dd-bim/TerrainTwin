create schema if not exists terraintwinv2;

create table if not exists terraintwinv2.polygon (id int PRIMARY KEY , polygon_id int, geometry geometry(POLYGON,25832));
	
create table if not exists terraintwinv2.dtm_tin (tin_id int PRIMARY KEY , geometry geometry(TINZ,25832));	
	
create table if not exists terraintwinv2.dtm_breaklines (bl_id int PRIMARY KEY , geometry geometry(LINESTRINGZ,25832), tin_id int, CONSTRAINT dtm_breaklines_fk FOREIGN KEY (tin_id) REFERENCES terraintwinv2.dtm_tin (tin_id));

create table if not exists terraintwinv2.points (id int PRIMARY KEY , point_id int, geometry geometry(POINTZ,25832));

create table if not exists terraintwinv2.lines (id int PRIMARY KEY , line_id int, geometry geometry(LINESTRINGZ,25832));

create table if not exists terraintwinv2.solid (id int PRIMARY KEY , solid_id int, geometry geometry(POLYHEDRALSURFACEZ,25832));

create table if not exists terraintwinv2.dtm_specialPoints (point_id int PRIMARY KEY , geometry geometry(LINESTRINGZ,25832), tin_id int, CONSTRAINT dtm_specialPoints_fk FOREIGN KEY (tin_id) REFERENCES terraintwinv2.dtm_tin (tin_id));

create table if not exists terraintwinv2.dtm_embarkment (embarkment_id int PRIMARY KEY , geometry geometry(LINESTRINGZ,25832), tin_id int, CONSTRAINT dtm_embarkment_fk FOREIGN KEY (tin_id) REFERENCES terraintwinv2.dtm_tin (tin_id));
