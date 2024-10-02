package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UpdateRoleDto {

    private UUID roleId;
    private String name;
    private String description;
    private List<RoleMappingDto> roleMapping;
}
