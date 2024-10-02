package com.ndp.service;

import com.ndp.model.dto.request.RegisterRoleDto;
import com.ndp.model.dto.request.SearchRoleDto;
import com.ndp.model.dto.request.UpdateRoleDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import jakarta.transaction.Transactional;

@Transactional
public interface RoleService {

    PageResponseDto searchRole(SearchRoleDto dto);

    ResponseDto registerRole(RegisterRoleDto dto);

    ResponseDto getAllRolesToOptions();

    ResponseDto updateRole(UpdateRoleDto dto);

    ResponseDto deleteRole(UpdateRoleDto dto);
}
