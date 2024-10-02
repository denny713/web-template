package com.ndp.service.impl;

import com.ndp.exception.BadRequestException;
import com.ndp.exception.ServiceException;
import com.ndp.model.dto.request.RegisterRoleDto;
import com.ndp.model.dto.request.SearchRoleDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.model.dto.response.RoleResponseDto;
import com.ndp.model.entity.Role;
import com.ndp.repository.RoleRepository;
import com.ndp.repository.dao.RoleDao;
import com.ndp.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private static final String SUCCESS = "Success";

    @Override
    public PageResponseDto searchRole(SearchRoleDto dto) {
        try {
            RoleDao roleDao = new RoleDao();
            List<RoleResponseDto> results = new ArrayList<>();

            Page<Role> roles = roleRepository.findAll(
                    roleDao.buildFindRoles(dto),
                    PageRequest.of(
                            dto.getPage() == null ? 0 : dto.getPage(),
                            dto.getSize() == null || dto.getSize() <= 0 ? 1 : dto.getSize(),
                            Sort.by(
                                    dto.getSort() == null ? Sort.Direction.ASC : dto.getSort(),
                                    StringUtils.isEmpty(dto.getSortBy()) ? "name" : dto.getSortBy()
                            )
                    )
            );

            roles.forEach(x -> results.add(new RoleResponseDto(
                    x.getId(),
                    x.getName(),
                    x.getDescription(),
                    x.isActive(),
                    x.getCreatedDate(),
                    x.getUpdatedDate()
            )));

            return new PageResponseDto(200, SUCCESS, results, roles.getNumber(),
                    roles.getSize(), roles.getTotalPages(), roles.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ResponseDto registerRole(RegisterRoleDto dto) {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new BadRequestException("Role name cannot be null or empty");
        }

        dto.setDescription(StringUtils.isEmpty(dto.getDescription()) ? "-" : dto.getDescription());

        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        roleRepository.save(role);

        return new ResponseDto(201, SUCCESS, null);
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

        return new ResponseDto(200, SUCCESS, roleOptions);
    }
}
