package com.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identity.entity.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
	 Optional <District> findById(Long id);
	 District findByDistrictId(long id);
}
