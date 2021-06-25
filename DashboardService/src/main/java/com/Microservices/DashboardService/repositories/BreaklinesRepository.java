package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Breaklines;

import org.springframework.stereotype.Repository;

@Repository
public interface BreaklinesRepository extends PostgresTableRepository<Breaklines> {
  
    // @Query(value="select * from terraintwinv2.dtm_breaklines", nativeQuery = true )
    // List<Breaklines> getBreaklines();

    // @Query(value = "select id, count(*) from terraintwinv2.dtm_breaklines group by id, tin_id, geometry having count(*) > 1", nativeQuery = true)
    // Integer[][] getBlDuplicates();
}
