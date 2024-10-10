package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleMappingDto {

    private long menuId;
    private boolean view;
    private boolean create;
    private boolean edit;
    private boolean delete;
}
