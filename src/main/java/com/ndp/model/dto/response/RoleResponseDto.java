package com.ndp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {

    private long id;
    private String name;
    private String description;
    private boolean active;
    private Date createdDate;
    private Date updatedDate;
    private List<RoleMappingResponseDto> roleMapping;
}
