package com.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.identity.entity.District;
import com.identity.entity.MasterEvent;

@Repository
public interface EventRepository extends JpaRepository<MasterEvent, Long>{
	//Office findByoid(long eid);
	List<MasterEvent> findByDistrict(District district);
	MasterEvent findByDistrictAndIsdefault(District district, boolean isdefault);
	
	@Modifying
    @Query("""
        update MasterEvent e
        set e.isdefault = false
        where e.district = :district
          and (:eventId is null or e.id <> :eventId)
    """)
    void clearDefaultForDistrictExcept(
            @Param("district")District district,
            @Param("eventId") Long eventId
    );
}
