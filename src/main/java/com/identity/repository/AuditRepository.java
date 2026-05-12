package com.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.identity.entity.AuditTrail;
import com.identity.entity.District;




public interface AuditRepository extends JpaRepository<AuditTrail, Long> {
	List<AuditTrail> findByDistrictOrderByIdDesc(District district);
	List<AuditTrail> findAllByOrderByIdDesc();
}
