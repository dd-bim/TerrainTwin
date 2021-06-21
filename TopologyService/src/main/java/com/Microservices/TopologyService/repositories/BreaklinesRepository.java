package com.Microservices.TopologyService.repositories;

import java.util.List;
import java.util.UUID;

import com.Microservices.TopologyService.schemas.Breaklines;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreaklinesRepository extends CrudRepository<Breaklines, UUID> {
  
    @Query(value="select * from terraintwin.breaklines_rohdaten", nativeQuery = true )
    List<Breaklines> getBreaklines();

}
