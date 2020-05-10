package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SysRoleMapper {

    @Select("select role_name from sys_role where role_id = #{roleId}")
    String findNameById(@Param("roleId") Integer roleId);

    @Select("select * from sys_role")
    @Results({
            @Result(property = "roleId", column = "role_id", jdbcType = JdbcType.TINYINT, javaType = Integer.class),
            @Result(property = "roleName", column = "role_name", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "roleDescription", column = "role_description", jdbcType = JdbcType.VARCHAR, javaType
                    = String.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<SysRole> findAll();
}
