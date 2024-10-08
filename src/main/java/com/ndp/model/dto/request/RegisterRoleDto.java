package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegisterRoleDto {

    private String name;
    private String description;
    private List<RoleMappingDto> roleMapping;
}
