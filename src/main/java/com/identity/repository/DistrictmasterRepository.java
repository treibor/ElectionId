package com.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identity.entity.District;
import com.identity.entity.Districtmaster;
@Repository
public interface DistrictmasterRepository extends JpaRepository<Districtmaster, Long>{
	Districtmaster findByDistrict(District district);
	
	Districtmaster findByDistrictAndMasterLabel(District district, String label);
}
