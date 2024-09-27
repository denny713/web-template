package com.ndp.service;

import com.ndp.model.dto.request.RegisterRoleDto;
import com.ndp.model.dto.response.ResponseDto;
import jakarta.transaction.Transactional;

@Transactional
public interface RoleService {

    ResponseDto registerRole(RegisterRoleDto dto);
}
