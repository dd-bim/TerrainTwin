create schema if not exists terraintwin;
create table if not exists terraintwin.polygon_rohdaten (id int PRIMARY KEY, polygon_id int, wkt varchar, srid int);
create table if not exists terraintwin.polygon (id int PRIMARY KEY , polygon_id int, geometry geometry(POLYGON,25832), CONSTRAINT rohdaten_fk FOREIGN KEY (id) REFERENCES terraintwin.polygon_rohdaten (id));

create or replace function terraintwin.copyWKTToGeometry() returns trigger as '
	begin
 		insert into terraintwin.polygon (id, polygon_id, geometry)
 			select id, polygon_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
 			from terraintwin.polygon_rohdaten
 		on conflict(id) do nothing;
 		return new;
 	end;
' language plpgsql; 

drop trigger if exists copywkt on terraintwin.polygon_rohdaten;
create trigger copywkt
	after insert on terraintwin.polygon_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyWKTToGeometry();
	
create table if not exists terraintwin.dtm_tin_rohdaten (tin_id int PRIMARY KEY, wkt varchar, srid int);
create table if not exists terraintwin.dtm_tin (tin_id int PRIMARY KEY , geometry geometry(dtm_tinZ,25832), CONSTRAINT dtm_tin_rohdaten_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.dtm_tin_rohdaten (tin_id));	
	
create or replace function terraintwin.copyDtmTinWktToDtmTinGeometry() returns trigger as '
	begin
 		insert into terraintwin.dtm_tin (tin_id, geometry)
 			select tin_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
 			from terraintwin.dtm_tin_rohdaten
 		on conflict(tin_id) do nothing;
 		return new;
 	end;
' language plpgsql;

drop trigger if exists copyDtmTinWkt on terraintwin.dtm_tin_rohdaten;
create trigger copyDtmTinWkt
	after insert on terraintwin.dtm_tin_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyDtmTinWktToDtmTinGeometry();
	
create table if not exists terraintwin.dtm_breaklines_rohdaten (bl_id int PRIMARY KEY, wkt varchar, srid int, tin_id int, CONSTRAINT dtm_breaklines_rohdaten_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.dtm_tin_rohdaten (tin_id));
create table if not exists terraintwin.dtm_breaklines (bl_id int PRIMARY KEY , geometry geometry(LINESTRINGZ,25832), tin_id int, CONSTRAINT dtm_breaklines_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.dtm_tin (tin_id), CONSTRAINT dtm_breaklines_rohdaten_fk FOREIGN KEY (bl_id) REFERENCES terraintwin.dtm_breaklines_rohdaten (bl_id));

create or replace function terraintwin.copyBlWKTToBlGeometry() returns trigger as '
	begin
 		insert into terraintwin.dtm_breaklines (bl_id, geometry, tin_id)
 			select bl_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry, tin_id
 			from terraintwin.dtm_breaklines_rohdaten
 		on conflict(bl_id) do nothing;
 		return new;
 	end;
' language plpgsql;

drop trigger if exists copyBlwkt on terraintwin.dtm_breaklines_rohdaten;
create trigger copyBlwkt
	after insert on terraintwin.dtm_breaklines_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyBlWKTToBlGeometry();

-- create table if not exists terraintwin.multipolygon_rohdaten (poly_id int PRIMARY KEY, wkt varchar, srid int);
-- create table if not exists terraintwin.multipolygon (poly_id int PRIMARY KEY , geometry geometry(MULTIPOLYGON,25832), CONSTRAINT multipolygon_rohdaten_fk FOREIGN KEY (poly_id) REFERENCES terraintwin.multipolygon_rohdaten (poly_id));	
	
-- create or replace function terraintwin.copyMULTIPOLYWKTToDtmTinGeometry() returns trigger as '
-- 	begin
--  		insert into terraintwin.multipolygon (poly_id, geometry)
--  			select poly_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
--  			from terraintwin.multipolygon_rohdaten
--  		on conflict(poly_id) do nothing;
--  		return new;
--  	end;
-- ' language plpgsql;

-- drop trigger if exists copyMULTIPOLYwkt on terraintwin.multipolygon_rohdaten;
-- create trigger copyMULTIPOLYwkt
-- 	after insert on terraintwin.multipolygon_rohdaten
--  	EXECUTE PROCEDURE terraintwin.copyMULTIPOLYWKTToDtmTinGeometry();

---------------------------- new ------------------------
create table if not exists terraintwin.points_rohdaten (id int PRIMARY KEY, point_id int, wkt varchar, srid int);
create table if not exists terraintwin.points (id int PRIMARY KEY , point_id int, geometry geometry(POINTZ,25832), CONSTRAINT points_rohdaten_fk FOREIGN KEY (id) REFERENCES terraintwin.points_rohdaten (id));

create or replace function terraintwin.copyWKTPointToGeometry() returns trigger as '
	begin
 		insert into terraintwin.points (id, point_id, geometry)
 			select id, point_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
 			from terraintwin.points_rohdaten
 		on conflict(id) do nothing;
 		return new;
 	end;
' language plpgsql; 

drop trigger if exists copyPointWkt on terraintwin.points_rohdaten;
create trigger copyPointWkt
	after insert on terraintwin.points_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyWKTPointToGeometry();

create table if not exists terraintwin.lines_rohdaten (id int PRIMARY KEY, line_id int, wkt varchar, srid int);
create table if not exists terraintwin.lines (id int PRIMARY KEY , line_id int, geometry geometry(LINESTRINGZ,25832), CONSTRAINT lines_rohdaten_fk FOREIGN KEY (id) REFERENCES terraintwin.lines_rohdaten (id));

create or replace function terraintwin.copyWKTLineToGeometry() returns trigger as '
	begin
 		insert into terraintwin.lines (id, line_id, geometry)
 			select id, line_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
 			from terraintwin.lines_rohdaten
 		on conflict(id) do nothing;
 		return new;
 	end;
' language plpgsql; 

drop trigger if exists copyLineWkt on terraintwin.lines_rohdaten;
create trigger copyLineWkt
	after insert on terraintwin.lines_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyWKTLineToGeometry();

create table if not exists terraintwin.solid_rohdaten (id int PRIMARY KEY, solid_id int, wkt varchar, srid int);
create table if not exists terraintwin.solid (id int PRIMARY KEY , line_id int, geometry geometry(POLYHEDRALSURFACEZ,25832), CONSTRAINT solid_rohdaten_fk FOREIGN KEY (id) REFERENCES terraintwin.solid_rohdaten (id));

create or replace function terraintwin.copyWKTSolidToGeometry() returns trigger as '
	begin
 		insert into terraintwin.solid (id, solid_id, geometry)
 			select id, solid_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
 			from terraintwin.solid_rohdaten
 		on conflict(id) do nothing;
 		return new;
 	end;
' language plpgsql; 

drop trigger if exists copySolidWkt on terraintwin.solid_rohdaten;
create trigger copySolidWkt
	after insert on terraintwin.solid_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyWKTSolidToGeometry();

create table if not exists terraintwin.dtm_specialPoints_rohdaten (point_id int PRIMARY KEY, wkt varchar, srid int, tin_id int, CONSTRAINT dtm_specialPoints_rohdaten_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.dtm_tin_rohdaten (tin_id));
create table if not exists terraintwin.dtm_specialPoints (point_id int PRIMARY KEY , geometry geometry(LINESTRINGZ,25832), tin_id int, CONSTRAINT dtm_specialPoints_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.dtm_tin (tin_id), CONSTRAINT dtm_specialPoints_rohdaten_fk FOREIGN KEY (point_id) REFERENCES terraintwin.dtm_specialPoints_rohdaten (point_id));

create or replace function terraintwin.copySpecialPointsWKTToGeometry() returns trigger as '
	begin
 		insert into terraintwin.dtm_specialPoints (point_id, geometry, tin_id)
 			select point_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry, tin_id
 			from terraintwin.specialPoints_rohdaten
 		on conflict(point_id) do nothing;
 		return new;
 	end;
' language plpgsql;

drop trigger if exists copySpecialPointsWkt on terraintwin.specialPoints_rohdaten;
create trigger copySpecialPointsWkt
	after insert on terraintwin.specialPoints_rohdaten
 	EXECUTE PROCEDURE terraintwin.copySpecialPointsWKTToGeometry();

create table if not exists terraintwin.dtm_embarkment_rohdaten (embarkment_id int PRIMARY KEY, wkt varchar, srid int, tin_id int, CONSTRAINT dtm_embarkment_rohdaten_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.dtm_tin_rohdaten (tin_id));
create table if not exists terraintwin.dtm_embarkment (embarkment_id int PRIMARY KEY , geometry geometry(LINESTRINGZ,25832), tin_id int, CONSTRAINT dtm_embarkment_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.dtm_tin (tin_id), CONSTRAINT dtm_embarkment_rohdaten_fk FOREIGN KEY (point_id) REFERENCES terraintwin.dtm_embarkment_rohdaten (point_id));

create or replace function terraintwin.copyEmbarkmentWKTToGeometry() returns trigger as '
	begin
 		insert into terraintwin.dtm_embarkment (point_id, geometry, tin_id)
 			select embarkment_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry, tin_id
 			from terraintwin.embarkment_rohdaten
 		on conflict(embarkment_id) do nothing;
 		return new;
 	end;
' language plpgsql;

drop trigger if exists copyEmbarkmentWkt on terraintwin.embarkment_rohdaten;
create trigger copyEmbarkmentWkt
	after insert on terraintwin.embarkment_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyEmbarkmentWKTToGeometry();