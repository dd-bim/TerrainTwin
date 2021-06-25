package com.Microservices.PostgresImportService.repositories;

import java.util.List;

import com.Microservices.PostgresImportService.schemas.Breaklines;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BreaklinesRepository extends PostgresTableRepository<Breaklines> {
  
    // @Query(value="select * from terraintwin.breaklines_rohdaten", nativeQuery = true )
    // List<Breaklines> getBreaklines();

}
