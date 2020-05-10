package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.DetectionTask;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetectionTaskMapper {
    @Insert("insert into detection_task (trans_id, consultant_id, address, meet_date, task_status) values " +
            "(#{transId}, #{consultantId}, #{address}, #{meetDate}, #{taskStatus})")
    Integer insert(DetectionTask detectionTask);

    @Update("update detection_task set task_status = #{status} where trans_id = #{transId}")
    void updateStatusByTransId(@Param("status") Integer status, @Param("transId") String transId);

    /**
     * 查询管理员未完成的车检任务
     * @param consultantId
     * @return
     */
    @Select("select * from detection_task where consultant_id = #{consultantId} and task_status <> 2")
    @ResultMap("detectionTaskResultMap")
    List<DetectionTask> findByConsultantId(@Param("consultantId") String consultantId);

    @Select("select * from detection_task where trans_id = #{transId}")
    @Results(id = "detectionTaskResultMap", value = {
            @Result(id = true, property = "transId", column = "trans_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "consultantId", column = "consultant_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "address", column = "address", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "meetDate", column = "meet_date", jdbcType = JdbcType.DATE, javaType =
                    LocalDate.class),
            @Result(property = "taskStatus", column = "task_status", jdbcType = JdbcType.TINYINT, javaType =
                    Integer.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    DetectionTask findByTransId(@Param("transId")String transId);
}
