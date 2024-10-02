package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRoleDto extends PageRequestDto {

    private String name;
    private String description;
    private Boolean active;
}
