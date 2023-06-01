package com.cao.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Role {
    private Integer id; // 主键ID
    private String roleCode;    // 角色编码
    private String roleName;    // 角色名称
    private Integer createdBy;  // 创建者
    private Date creationDate;  // 创建时间
    private Integer modifyBy;   // 修改者
    private Date modifyDate;    // 修改时间

}
