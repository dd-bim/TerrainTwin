create schema if not exists terraintwin;
create table if not exists terraintwin.surfaces_rohdaten (id int PRIMARY KEY, surface_id int, wkt varchar, srid int);
create table if not exists terraintwin.surfaces (id int PRIMARY KEY , surface_id int, geometry geometry(POLYGON,25832), CONSTRAINT rohdaten_fk FOREIGN KEY (id) REFERENCES terraintwin.surfaces_rohdaten (id));

--drop function terraintwin.copyWKTToGeometry()


create or replace function terraintwin.copyWKTToGeometry() returns trigger as '
	begin
 		insert into terraintwin.surfaces (id, surface_id, geometry)
 			select id, surface_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
 			from terraintwin.surfaces_rohdaten
 		on conflict(id) do nothing;
 		return new;
 	end;
' language plpgsql; 

drop trigger if exists copywkt on terraintwin.surfaces_rohdaten;
create trigger copywkt
	after insert on terraintwin.surfaces_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyWKTToGeometry();
	
create table if not exists terraintwin.tin_rohdaten (tin_id int PRIMARY KEY, wkt varchar, srid int);
create table if not exists terraintwin.tin (tin_id int PRIMARY KEY , geometry geometry(TINZ,25832), CONSTRAINT tin_rohdaten_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.tin_rohdaten (tin_id));	
	
create or replace function terraintwin.copyTINWKTToTINGeometry() returns trigger as '
	begin
 		insert into terraintwin.tin (tin_id, geometry)
 			select tin_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
 			from terraintwin.tin_rohdaten
 		on conflict(tin_id) do nothing;
 		return new;
 	end;
' language plpgsql;

drop trigger if exists copyTINwkt on terraintwin.tin_rohdaten;
create trigger copyTINwkt
	after insert on terraintwin.tin_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyTINWKTToTINGeometry();
	
create table if not exists terraintwin.breaklines_rohdaten (bl_id int PRIMARY KEY, wkt varchar, srid int, tin_id int, CONSTRAINT tin_breaklines_rohdaten_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.tin_rohdaten (tin_id));
create table if not exists terraintwin.breaklines (bl_id int PRIMARY KEY , geometry geometry(LINESTRINGZ,25832), tin_id int, CONSTRAINT tin_breaklines_fk FOREIGN KEY (tin_id) REFERENCES terraintwin.tin (tin_id), CONSTRAINT breaklines_rohdaten_fk FOREIGN KEY (bl_id) REFERENCES terraintwin.breaklines_rohdaten (bl_id));

create or replace function terraintwin.copyBlWKTToBlGeometry() returns trigger as '
	begin
 		insert into terraintwin.breaklines (bl_id, geometry, tin_id)
 			select bl_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry, tin_id
 			from terraintwin.breaklines_rohdaten
 		on conflict(bl_id) do nothing;
 		return new;
 	end;
' language plpgsql;

drop trigger if exists copyBlwkt on terraintwin.breaklines_rohdaten;
create trigger copyBlwkt
	after insert on terraintwin.breaklines_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyBlWKTToBlGeometry();

create table if not exists terraintwin.multipolygon_rohdaten (poly_id int PRIMARY KEY, wkt varchar, srid int);
create table if not exists terraintwin.multipolygon (poly_id int PRIMARY KEY , geometry geometry(MULTIPOLYGON,25832), CONSTRAINT multipolygon_rohdaten_fk FOREIGN KEY (poly_id) REFERENCES terraintwin.multipolygon_rohdaten (poly_id));	
	
create or replace function terraintwin.copyMULTIPOLYWKTToTINGeometry() returns trigger as '
	begin
 		insert into terraintwin.multipolygon (poly_id, geometry)
 			select poly_id, ST_Transform(ST_setSRID(ST_GeomFromText(wkt),srid),25832) as geometry
 			from terraintwin.multipolygon_rohdaten
 		on conflict(poly_id) do nothing;
 		return new;
 	end;
' language plpgsql;

drop trigger if exists copyMULTIPOLYwkt on terraintwin.multipolygon_rohdaten;
create trigger copyMULTIPOLYwkt
	after insert on terraintwin.multipolygon_rohdaten
 	EXECUTE PROCEDURE terraintwin.copyMULTIPOLYWKTToTINGeometry();