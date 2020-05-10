package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.Reservation;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationMapper {

    @Insert("insert into reservation (trans_id, booker_id, meet_date) values (#{transId}, #{bookerId}, " +
            "#{meetDate})")
    Integer insert(@Param("transId") String transId, @Param("bookerId") String bookerId,
                   @Param("meetDate") LocalDate meetDate);

    @Delete("delete from reservation where trans_id = #{transId} and booker_id = #{bookerId}")
    Integer deleteByKey(@Param("transId") String transId, @Param("bookerId") String bookerId);

    @Update("update reservation set address = #{address} where trans_id = #{transId} and booker_id = #{bookerId}")
    Integer updateAddressByKey(@Param("address") String address, @Param("transId") String transId,
                               @Param("bookerId") String bookerId);

    @Select("select count(*) from reservation where trans_id = #{transId} and booker_id = #{bookerId}")
    Integer countByKey(@Param("transId") String transId, @Param("bookerId") String bookerId);

    @Select("select * from reservation where trans_id = #{transId} and booker_id = #{bookerId}")
    @ResultMap(value = "reservationResultMap")
    Reservation findByKey(@Param("transId") String transId, @Param("bookerId") String bookerId);

    @Select("select * from reservation where trans_id = #{transId}")
    @ResultMap(value = "reservationResultMap")
    List<Reservation> findByTrans(@Param("transId") String transId);

    @Select("select * from reservation")
    @Results(id = "reservationResultMap", value = {
            @Result(id = true, property = "transId", column = "trans_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(id = true, property = "bookerId", column = "booker_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "address", column = "address", jdbcType = JdbcType.VARCHAR, javaType =
                    String.class),
            @Result(property = "meetDate", column = "meet_date", jdbcType = JdbcType.DATE, javaType =
                    LocalDate.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<Reservation> findAll();
}
