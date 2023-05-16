package com.dimas.bbbank.repository;

import com.dimas.bbbank.domain.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long>, QuerydslPredicateExecutor<PhoneData> {

    List<PhoneData> findByUserId(Long userId);

}