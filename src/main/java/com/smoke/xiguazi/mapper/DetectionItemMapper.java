package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.DetectionItem;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetectionItemMapper {

    @Select("select * from detection_item")
    @Results(id = "detectionItemResultMap", value = {
            @Result(id = true, property = "itemId", column = "item_id", jdbcType = JdbcType.SMALLINT, javaType =
                    Integer.class),
            @Result(property = "detectionType", column = "detection_type", jdbcType = JdbcType.VARCHAR, javaType =
                    String.class),
            @Result(property = "content", column = "content", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<DetectionItem> findAll();
}
