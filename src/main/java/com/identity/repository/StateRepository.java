package com.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identity.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

}
