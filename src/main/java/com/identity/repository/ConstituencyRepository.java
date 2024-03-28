package com.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.identity.entity.Constituency;
import com.identity.entity.District;

public interface ConstituencyRepository extends JpaRepository<Constituency, Long> {
	List<Constituency> findByDistrict(District district);
}
