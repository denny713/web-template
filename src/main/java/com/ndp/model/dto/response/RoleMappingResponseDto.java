package com.ndp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleMappingResponseDto {

    private UUID menuId;
    private String menuName;
    private boolean view;
    private boolean create;
    private boolean edit;
    private boolean delete;
}
