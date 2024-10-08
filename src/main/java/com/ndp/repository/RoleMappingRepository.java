package com.ndp.repository;

import com.ndp.model.entity.RoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMappingRepository extends JpaRepository<RoleMapping, Long>, JpaSpecificationExecutor<RoleMapping> {

    List<RoleMapping> findByRoleId(Long roleId);
}
