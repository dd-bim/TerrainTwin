package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Breaklines;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreaklinesRepository extends CrudRepository<Breaklines, Integer> {
  
    // @Query(value="select * from terraintwinv2.dtm_breaklines", nativeQuery = true )
    // List<Breaklines> getBreaklines();

    @Query(value = "select bl_id, tin_id, count(*) from terraintwinv2.dtm_breaklines group by bl_id, tin_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getBlDuplicates();
}
