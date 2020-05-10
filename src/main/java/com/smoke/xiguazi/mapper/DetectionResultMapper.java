package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.DetectionResult;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetectionResultMapper {

    @Insert("insert into detection_result (trans_id, item_id, consultant_id, problem_level, problem_description) " +
            "values" +
            " (#{transId}, #{itemId}, #{consultantId}, #{problemLevel}, #{problemDescription})")
    Integer insert(DetectionResult detectionResult);

    @Select("select * from detection_result where trans_id = #{transId}")
    @ResultMap(value = "detectionResultResultMap")
    List<DetectionResult> findByTransId(@Param("transId") String transId);

    @Select("select * from detection_result")
    @Results(id = "detectionResultResultMap", value = {
            @Result(id = true, property = "transId", column = "trans_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(id = true, property = "itemId", column = "item_id", jdbcType = JdbcType.SMALLINT, javaType =
                    Integer.class),
            @Result(property = "consultantId", column = "consultant_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "problemLevel", column = "problem_level", jdbcType = JdbcType.TINYINT, javaType =
                    Integer.class),
            @Result(property = "problemDescription", column = "problem_description", jdbcType = JdbcType.VARCHAR,
                    javaType = String.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<DetectionResult> findAll();
}
