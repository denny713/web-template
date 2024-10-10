package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateRoleDto {

    private long roleId;
    private String name;
    private String description;
    private List<RoleMappingDto> roleMapping;
}
