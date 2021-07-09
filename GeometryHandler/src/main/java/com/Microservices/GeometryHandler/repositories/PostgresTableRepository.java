package com.Microservices.GeometryHandler.repositories;

import java.util.List;
import java.util.UUID;

import com.Microservices.GeometryHandler.schemas.PostgresTables;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PostgresTableRepository<T extends PostgresTables> extends CrudRepository<T, UUID> {

    @Query(value = "select table_name from information_schema.tables where table_schema = 'terraintwinv2'", nativeQuery = true)
    List<String> getCollections();

    @Query(value = "select * from information_schema.tables where table_schema = 'terraintwinv2' and table_name = ?#{[0]}", nativeQuery = true)
    String getCollection(String collectionId);

    @Query(value = "select cast(id as varchar) as item_id from #{#entityName}", nativeQuery = true)
    List<String> getItems();

    @Query(value = "select id, origin_id, ST_ASEWKT(geometry) as geometry from #{#entityName} where id = ?#{[0]}", nativeQuery = true)
    T getItem(UUID featureId);

    @Query(value = "select id, tin_id, ST_ASEWKT(geometry) as geometry from #{#entityName} where id = ?#{[0]}", nativeQuery = true)
    T getItem2(UUID featureId);

}
