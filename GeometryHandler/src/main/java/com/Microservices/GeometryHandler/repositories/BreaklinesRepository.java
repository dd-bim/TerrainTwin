package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Breaklines;

import org.springframework.stereotype.Repository;

@Repository
public interface BreaklinesRepository extends PostgresTableRepository<Breaklines> {
  
    // @Query(value="select * from terraintwin.breaklines_rohdaten", nativeQuery = true )
    // List<Breaklines> getBreaklines();

}
