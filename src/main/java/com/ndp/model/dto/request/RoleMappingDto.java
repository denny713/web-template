package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleMappingDto {

    private UUID menuId;
    private boolean view;
    private boolean create;
    private boolean edit;
    private boolean delete;
}
