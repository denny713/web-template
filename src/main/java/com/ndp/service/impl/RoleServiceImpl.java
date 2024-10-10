package com.ndp.service.impl;

import com.ndp.exception.BadRequestException;
import com.ndp.exception.NotFoundException;
import com.ndp.exception.ServiceException;
import com.ndp.model.dto.request.RegisterRoleDto;
import com.ndp.model.dto.request.RoleMappingDto;
import com.ndp.model.dto.request.SearchRoleDto;
import com.ndp.model.dto.request.UpdateRoleDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.model.dto.response.RoleMappingResponseDto;
import com.ndp.model.dto.response.RoleResponseDto;
import com.ndp.model.entity.Menu;
import com.ndp.model.entity.Role;
import com.ndp.model.entity.RoleMapping;
import com.ndp.repository.MenuRepository;
import com.ndp.repository.RoleMappingRepository;
import com.ndp.repository.RoleRepository;
import com.ndp.repository.dao.RoleDao;
import com.ndp.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final RoleMappingRepository roleMappingRepository;

    private static final String SUCCESS = "Success";

    @Override
    @Transactional
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
                                    StringUtils.isEmpty(dto.getSortBy()) ? "id" : dto.getSortBy()
                            )
                    )
            );

            roles.forEach(x -> {
                List<RoleMappingResponseDto> mappings = new ArrayList<>();
                x.getRoleMapping().forEach(c -> mappings.add(new RoleMappingResponseDto(
                        c.getMenu().getId(),
                        c.getMenu().getName(),
                        c.getMenu().getDescription(),
                        c.getMenu().getUrl(),
                        c.getMenu().getIcon(),
                        c.isViewAccess(),
                        c.isCreateAccess(),
                        c.isEditAccess(),
                        c.isDeleteAccess()
                )));

                results.add(new RoleResponseDto(
                        x.getId(),
                        x.getName(),
                        x.getDescription(),
                        x.isActive(),
                        x.getCreatedDate(),
                        x.getUpdatedDate(),
                        mappings
                ));
            });

            return new PageResponseDto(200, SUCCESS, results,
                    dto.getDraw() == null ? 0 : dto.getDraw(),
                    roles.getTotalElements(), roles.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseDto roleDetail(long roleId) {
        if (roleId == 0) {
            throw new BadRequestException("Role ID cannot be null or empty");
        }

        Role role = getById(roleId);
        List<RoleMappingResponseDto> mappings = new ArrayList<>();
        role.getRoleMapping().forEach(c -> mappings.add(new RoleMappingResponseDto(
                c.getMenu().getId(),
                c.getMenu().getName(),
                c.getMenu().getDescription(),
                c.getMenu().getUrl(),
                c.getMenu().getIcon(),
                c.isViewAccess(),
                c.isCreateAccess(),
                c.isEditAccess(),
                c.isDeleteAccess()
        )));

        return new ResponseDto(200, SUCCESS, new RoleResponseDto(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.isActive(),
                role.getCreatedDate(),
                role.getUpdatedDate(),
                mappings
        ));
    }

    @Override
    @Transactional
    public ResponseDto registerRole(RegisterRoleDto dto) {
        dto.setDescription(StringUtils.isEmpty(dto.getDescription()) ? "-" : dto.getDescription());
        if (StringUtils.isEmpty(dto.getName())) {
            throw new BadRequestException("Role name cannot be null or empty");
        }

        if (getRoleByName(dto.getName()) != null) {
            throw new BadRequestException("Role already exists");
        }

        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        Role savedRole = roleRepository.save(role);

        List<RoleMapping> roleMappings = new ArrayList<>();
        List<RoleMappingDto> dtos = dto.getRoleMapping();
        List<Menu> menus = menuRepository.findByIdIn(dtos.stream().map(RoleMappingDto::getMenuId).toList());
        dtos.forEach(x -> {
            Menu menu = menus.stream().filter(menuDetail -> menuDetail.getId() == x.getMenuId()).findFirst().orElse(null);
            if (menu == null) {
                throw new NotFoundException("Menu detail not found");
            }

            RoleMapping roleMapping = new RoleMapping();
            roleMapping.setRole(savedRole);
            roleMapping.setMenu(menu);
            roleMapping.setViewAccess(x.isView());
            roleMapping.setCreateAccess(x.isCreate());
            roleMapping.setDeleteAccess(x.isDelete());
            roleMapping.setEditAccess(x.isEdit());
            roleMappings.add(roleMapping);
        });

        roleMappingRepository.saveAll(roleMappings);
        return new ResponseDto(201, SUCCESS, savedRole);
    }

    @Override
    @Transactional
    public ResponseDto getAllRolesToOptions() {
        List<Role> roles = roleRepository.findAll();

        List<Map<String, String>> roleOptions = new ArrayList<>();
        roles.forEach(x -> {
            if (!x.isDeleted()) {
                Map<String, String> roleOption = new HashMap<>();
                roleOption.put("key", String.valueOf(x.getId()));
                roleOption.put("value", x.getDescription());
                roleOptions.add(roleOption);
            }
        });

        return new ResponseDto(200, SUCCESS, roleOptions);
    }

    @Override
    @Transactional
    public ResponseDto updateRole(UpdateRoleDto dto) {
        dto.setDescription(StringUtils.isEmpty(dto.getDescription()) ? "-" : dto.getDescription());
        if (dto.getRoleId() == 0) {
            throw new BadRequestException("Role ID cannot be null or empty");
        }

        if (StringUtils.isEmpty(dto.getName())) {
            throw new BadRequestException("Role name cannot be null or empty");
        }

        Role role = getById(dto.getRoleId());
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());

        List<RoleMapping> existingRoleMaps = roleMappingRepository.findByRoleId(dto.getRoleId());
        if (!existingRoleMaps.isEmpty()) {
            roleMappingRepository.deleteAll(existingRoleMaps);
        }

        List<RoleMapping> roleMappings = new ArrayList<>();
        List<RoleMappingDto> dtos = dto.getRoleMapping();
        List<Menu> menus = menuRepository.findByIdIn(dtos.stream()
                .map(RoleMappingDto::getMenuId).toList());
        dtos.forEach(x -> {
            Menu menu = menus.stream().filter(menuDetail -> menuDetail.getId() == x.getMenuId()).findFirst().orElse(null);
            if (menu == null) {
                throw new NotFoundException("Menu detail not found");
            }

            RoleMapping roleMapping = new RoleMapping();
            roleMapping.setRole(role);
            roleMapping.setMenu(menu);
            roleMapping.setViewAccess(x.isView());
            roleMapping.setCreateAccess(x.isCreate());
            roleMapping.setDeleteAccess(x.isDelete());
            roleMapping.setEditAccess(x.isEdit());
            roleMappings.add(roleMapping);
        });

        roleRepository.save(role);
        roleMappingRepository.saveAll(roleMappings);
        return new ResponseDto(200, SUCCESS, null);
    }

    @Override
    @Transactional
    public ResponseDto deleteRole(UpdateRoleDto dto) {
        if (dto.getRoleId() == 0) {
            throw new BadRequestException("Role ID cannot be null or empty");
        }

        Role role = getById(dto.getRoleId());
        role.setDeleted(true);
        roleRepository.save(role);

        return new ResponseDto(200, SUCCESS, null);
    }

    @Override
    @Transactional
    public ResponseDto updateRoleStatus(UpdateRoleDto dto, boolean active) {
        if (dto.getRoleId() == 0) {
            throw new BadRequestException("Role ID cannot be null or empty");
        }

        Role role = getById(dto.getRoleId());
        role.setActive(!active);
        roleRepository.save(role);

        return new ResponseDto(200, SUCCESS, null);
    }

    private Role getById(long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            throw new NotFoundException("Role detail not found");
        }

        return role.get();
    }

    private Role getRoleByName(String name) {
        RoleDao roleDao = new RoleDao();
        return roleRepository.findOne(roleDao.buildFindByRoleName(name)).orElse(null);
    }
}
