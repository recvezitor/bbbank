package com.dimas.bbbank.repository;

import com.dimas.bbbank.domain.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface EmailDataRepository extends JpaRepository<EmailData, Long>, QuerydslPredicateExecutor<EmailData> {

    List<EmailData> findByUserId(Long userId);

}