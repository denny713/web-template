package com.ndp.repository.dao;

import com.ndp.model.dto.request.SearchRoleDto;
import com.ndp.model.entity.Role;
import com.ndp.repository.dao.common.CommonDao;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RoleDao extends CommonDao {

    public Specification<Role> buildFindRoles(SearchRoleDto dto) {
        dto.setName(StringUtils.isEmpty(dto.getName()) ? "" : dto.getName());
        dto.setDescription(StringUtils.isEmpty(dto.getDescription()) ? "" : dto.getDescription());

        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            add(predicates, isFalse("deleted", root, cb));

            add(predicates, like(dto.getName(), "name", root, cb));
            add(predicates, like(dto.getDescription(), "description", root, cb));

            if (dto.getActive() != null) {
                add(predicates, Boolean.TRUE.equals(dto.getActive()) ? isTrue("active", root, cb)
                        : isFalse("active", root, cb));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Role> buildFindByRoleName(String name) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            add(predicates, isTrue("active", root, cb));
            add(predicates, isFalse("deleted", root, cb));
            add(predicates, equals(name, "name", root, cb));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
