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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public ResponseDto getAllRolesToOptions() {
        List<Role> roles = roleRepository.findAll();

        List<Map<String, String>> roleOptions = new ArrayList<>();
        roles.forEach(x -> {
            Map<String, String> roleOption = new HashMap<>();
            roleOption.put("key", x.getId().toString());
            roleOption.put("value", x.getDescription());
            roleOptions.add(roleOption);
        });

        return new ResponseDto(200, "Success", roleOptions);
    }
}
