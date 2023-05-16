package com.dimas.bbbank.domain;

import com.querydsl.core.types.Predicate;

public interface IEntityFilter {
    Predicate toPredicate();
}