package com.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identity.entity.District;
import com.identity.entity.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long>{
	Office findByoid(long eid);
	List<Office> findByDistrict(District district);
}
