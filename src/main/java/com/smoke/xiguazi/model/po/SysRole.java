package com.smoke.xiguazi.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole {
    private Integer roleId;
    private String roleName;
    private String roleDescription;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
