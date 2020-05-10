package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.TransactionInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionInfoMapper {
    @Insert("insert into transaction_info (car_id, owner_id, city) values (#{carId}, #{ownerId}, #{city})")
    @Options(keyProperty = "transId", useGeneratedKeys = true)
    Integer insert(TransactionInfo transactionInfo);

    @Update("update transaction_info set price = #{price} where trans_id = #{transId}")
    Integer updatePriceByTransId(@Param("price") Long price, @Param("transId") String transId);

    @Update("update transaction_info set address = #{address}, price = #{price} where trans_id = #{transId}")
    Integer updateDetectionByTransId(@Param("address") String address, @Param("price") Long price,
                                 @Param("transId") String transId);

    @Update("update transaction_info set car_picture = #{picture} where trans_id = #{transId}")
    Integer updatePictureByTransId(@Param("picture") String picture, @Param("transId") String transId);

    @Update("update transaction_info set trans_status = #{status} where trans_id = #{transId}")
    Integer updateStatusByTransId(@Param("status") Integer status, @Param("transId") String transId);

    @Update("update transaction_info set consultant_id = #{consultantId} where trans_id = #{transId}")
    Integer updateConsultantByTransId(@Param("consultantId") String consultantId, @Param("transId") String transId);

    @Update("update transaction_info set buyer_id = #{buyerId}, trans_status = #{status} where trans_id = #{transId}")
    Integer signedTrans(@Param("buyerId") String buyerId, @Param("status") Integer status,
                        @Param("transId") String transId);

    @Select("select count(*) from transaction_info")
    BigInteger countAll();

    @Select("select * from transaction_info where trans_id = #{transId}")
    @ResultMap(value = "transactionInfoResultMap")
    TransactionInfo findByTransId(@Param("transId") String transId);

    @Select("select * from transaction_info where car_id = #{carId}")
    @ResultMap(value = "transactionInfoResultMap")
    TransactionInfo findByCarId(@Param("carId") String carId);

    @Select("select * from transaction_info where owner_id = #{ownerId}")
    @ResultMap(value = "transactionInfoResultMap")
    List<TransactionInfo> findByOwnerId(@Param("ownerId") String ownerId);

    @Select("select * from transaction_info where buyer_id = #{buyerId}")
    @ResultMap(value = "transactionInfoResultMap")
    List<TransactionInfo> findByBuyerId(@Param("buyerId") String buyerId);

    @Select("select * from transaction_info where consultant_id = #{consultantId} and trans_status in (1, 2)")
    @ResultMap(value = "transactionInfoResultMap")
    List<TransactionInfo> findUnfinishedByConsultantIdAndStatus(@Param("consultantId") String consultantId);

    @Select("select * from transaction_info where consultant_id = #{consultantId} and trans_status = 3")
    @ResultMap(value = "transactionInfoResultMap")
    List<TransactionInfo> findFinishedByConsultantId(@Param("consultantId") String consultantId);

    @Select("select * from transaction_info where consultant_id = #{consultantId} and trans_status = #{status}")
    @ResultMap(value = "transactionInfoResultMap")
    List<TransactionInfo> findByConsultantId(@Param("consultantId") String consultantId,
                                                      @Param("status") Integer status);

    @Select("select * from transaction_info where trans_status = #{status}")
    @ResultMap(value = "transactionInfoResultMap")
    List<TransactionInfo> findByStatus(@Param("status") Integer status);

    /**
     * 关联reservation表的booker_id
     * @param bookerId  reservation表的booker_id字段
     * @return
     */
    @Select("select t.* from transaction_info as t, reservation as r where r.booker_id = #{bookerId} and t.trans_id =" +
            " r.trans_id")
    @ResultMap(value = "transactionInfoResultMap")
    List<TransactionInfo> findByBookerId(@Param("bookerId") String bookerId);

    /**
     * 查询指定交易的车主电话
     * 关联user_info表的user_id
     * @param tansId
     * @return
     */
    @Select("select u.phone from user_info as u, transaction_info as t where t.trans_id = #{transId} and u.user_id = " +
            "t.owner_id")
    String findOwnerPhoneBytransId(@Param("transId") String tansId);

    @Select("select car_id from transaction_info where trans_id = #{transId}")
    String findCarIdByTransId(@Param("transId") String transId);

    @Select("select address from transaction_info where trans_id = #{transId}")
    String findAddressByTransId(@Param("transId") String transId);

    @Select("select update_time from transaction_info where trans_id = #{transId}")
    LocalDateTime findUpdateTimeByTransId(@Param("transId") String transId);

    @Select("select distinct city from transaction_info")
    List<String> findCityList();

    @Select("select * from transaction_info")
    @Results(id = "transactionInfoResultMap", value = {
            @Result(id = true, property = "transId", column = "trans_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "carId", column = "car_id", jdbcType = JdbcType.BIGINT, javaType = String.class),
            @Result(property = "ownerId", column = "owner_id", jdbcType = JdbcType.BIGINT, javaType = String.class),
            @Result(property = "buyerId", column = "buyer_id", jdbcType = JdbcType.BIGINT, javaType = String.class),
            @Result(property = "consultantId", column = "consultant_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "city", column = "city", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "address", column = "address", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "carPicture", column = "car_picture", jdbcType = JdbcType.VARCHAR, javaType =
                    String.class),
            @Result(property = "transStatus", column = "trans_status", jdbcType = JdbcType.TINYINT, javaType =
                    Integer.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<TransactionInfo> findAll();
}
