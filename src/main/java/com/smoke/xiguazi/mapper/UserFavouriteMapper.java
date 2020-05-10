package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.UserFavourite;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserFavouriteMapper {
    @Insert("insert into user_favourite (user_id, trans_id) values (#{userId}, #{transId})")
    Integer insert(@Param("userId") String userId, @Param("transId") String transId);

    @Delete("delete from user_favourite where user_id = #{userId} and trans_id = #{transId}")
    Integer deleteByKey(@Param("userId") String userId, @Param("transId") String transId);

    @Select("select count(*) from user_favourite where user_id = #{userId} and trans_id = #{transId}")
    Integer countByKey(@Param("userId") String userId, @Param("transId") String transId);

    @Select("select trans_id from user_favourite where user_id = #{userId}")
    List<String> findTransIdByUserId(@Param("userId") String userId);

    @Select("select * from user_favourite")
    @Results(id = "userFavouriteResultMap", value = {
            @Result(id = true, property = "userId", column = "user_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(id = true, property = "transId", column = "trans_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<UserFavourite> findAll();
}
