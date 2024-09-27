package com.ndp.service.impl;

import com.ndp.exception.BadRequestException;
import com.ndp.model.dto.request.RegisterRoleDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.model.entity.Role;
import com.ndp.repository.RoleRepository;
import com.ndp.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public ResponseDto registerRole(RegisterRoleDto dto) {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new BadRequestException("Role name cannot be null or empty");
        }

        dto.setDescription(StringUtils.isEmpty(dto.getDescription()) ? "-" : dto.getDescription());

        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        roleRepository.save(role);

        return new ResponseDto(201, "Success", null);
    }
}
