package com.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identity.entity.Cell;
import com.identity.entity.District;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long>{
	
	List<Cell> findByDistrict(District district);
}
