package com.ndp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleMappingResponseDto {

    private long menuId;
    private String menuName;
    private String menuDesc;
    private String menuUrl;
    private String menuIcon;
    private boolean view;
    private boolean create;
    private boolean edit;
    private boolean delete;
}
