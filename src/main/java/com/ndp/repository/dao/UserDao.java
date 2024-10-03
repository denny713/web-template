package com.ndp.repository.dao;

import com.ndp.model.dto.request.SearchUserDto;
import com.ndp.model.entity.User;
import com.ndp.repository.dao.common.CommonDao;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao extends CommonDao {

    public Specification<User> buildFindUsers(SearchUserDto dto) {
        dto.setUsername(StringUtils.isEmpty(dto.getUsername()) ? "" : dto.getUsername());
        dto.setName(StringUtils.isEmpty(dto.getName()) ? "" : dto.getName());
        dto.setEmail(StringUtils.isEmpty(dto.getEmail()) ? "" : dto.getEmail());
        dto.setRole(StringUtils.isEmpty(dto.getRole()) ? "" : dto.getRole());

        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            add(predicates, isFalse("deleted", root, cb));

            add(predicates, like(dto.getUsername(), "username", root, cb));
            add(predicates, like(dto.getName(), "name", root, cb));
            add(predicates, like(dto.getEmail(), "email", root, cb));
            add(predicates, equals(dto.getRole(), "description", root.join("role", JoinType.LEFT), cb));

            if (dto.getActive() != null) {
                add(predicates, Boolean.TRUE.equals(dto.getActive()) ? isTrue("active", root, cb)
                        : isFalse("active", root, cb));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<User> buildFindByUsername(String username) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            add(predicates, isTrue("active", root, cb));
            add(predicates, isFalse("deleted", root, cb));
            add(predicates, equals(username, "username", root, cb));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
