package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.UserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Mapper
public interface UserInfoMapper {

    @Insert("insert into user_info (phone, passwd, user_role) values (#{phone}, #{passwd}, #{userRole, jdbcType = " +
            "TINYINT})")
    @Options(keyProperty = "userId", useGeneratedKeys = true)
    Integer insert(UserInfo userInfo);

    @Update("update user_info set user_name = #{userName, jdbcType = VARCHAR}, email = #{email, jdbcType = VARCHAR}, " +
            "gender = #{gender, jdbcType = TINYINT}, address = #{address, jdbcType=VARCHAR} where user_id = #{userId}")
    Integer updateBaseInfoById(@Param("userName") String userName, @Param("email") String email,
                               @Param("gender") Integer gender, @Param("address") String address,
                               @Param("userId") String userId);

    @Update("update user_info set passwd = #{passwd} where user_id = #{userId}")
    Integer updatePasswdById(@Param("passwd") String passwd, @Param("userId") String userId);

    @Select("select count(*) from user_info")
    BigInteger countAll();

    @Select("select * from user_info where user_id = #{userId}")
    @ResultMap(value = "userInfoResultMap")
    UserInfo findById(@Param("userId") String userId);

    @Select("select * from user_info where phone = #{phone}")
    @ResultMap(value = "userInfoResultMap")
    UserInfo findByPhone(@Param("phone") String phone);

    @Select("select phone from user_info where user_id = #{userId}")
    String findPhoneById(@Param("userId") String userId);

    @Select("select passwd from user_info where user_id = #{userId}")
    String findPasswdById(@Param("userId") String userId);

    @Select("select * from user_info")
    @Results(id = "userInfoResultMap", value = {
            @Result(id = true, property = "userId", column = "user_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "passwd", column = "passwd", jdbcType = JdbcType.CHAR, javaType = String.class),
            @Result(property = "phone", column = "phone", jdbcType = JdbcType.CHAR, javaType = String.class),
            @Result(property = "userName", column = "user_name", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "email", column = "email", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "gender", column = "gender", jdbcType = JdbcType.TINYINT, javaType = Integer.class),
            @Result(property = "address", column = "address", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "userRole", column = "user_role", jdbcType = JdbcType.TINYINT, javaType = Integer.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<UserInfo> findAll();
}
