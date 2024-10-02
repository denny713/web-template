package com.ndp.repository.dao.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonDao {

    protected static <T> Predicate like(String value, String fieldName, Path<T> path, CriteriaBuilder cb) {
        return value != null ? cb.like(cb.lower(path.get(fieldName)), "%" + value.toLowerCase() + "%") : null;
    }

    protected static <T> Predicate equals(String value, String fieldName, Path<T> path, CriteriaBuilder cb) {
        return StringUtils.isNotEmpty(value) ? cb.equal(path.get(fieldName), value) : null;
    }

    protected static Predicate or(CriteriaBuilder cb, Predicate... predicates) {
        List<Predicate> nonNullPredicates = new ArrayList<>();
        for (Predicate predicate : predicates) {
            if (predicate != null) {
                nonNullPredicates.add(predicate);
            }
        }
        return cb.or(nonNullPredicates.toArray(new Predicate[0]));
    }

    protected static <T> Predicate isTrue(String fieldName, Path<T> path, CriteriaBuilder cb) {
        return cb.isTrue(path.get(fieldName));
    }

    protected static <T> Predicate isFalse(String fieldName, Path<T> path, CriteriaBuilder cb) {
        return cb.isFalse(path.get(fieldName));
    }

    protected static void add(List<Predicate> predicates, Predicate predicate) {
        if (predicate != null) {
            predicates.add(predicate);
        }
    }
}
