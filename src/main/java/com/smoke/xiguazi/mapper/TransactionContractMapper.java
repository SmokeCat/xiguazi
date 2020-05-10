package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.TransactionContract;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionContractMapper {
    @Insert("insert into transaction_contract (trans_id, party_a, party_b, party_c, contract_file) values " +
            "(#{transId}, #{partyA}, #{partyB}, #{partyC}, #{contractFile})")
    Integer insert(TransactionContract transactionContract);

    @Select("select create_time from transaction_contract where trans_id = #{transId}")
    LocalDateTime findCreateTimeByTransId(@Param("transId") String transId);

    @Select("select * from transaction_contract")
    @Results(id = "transactionContractResultMap", value = {
            @Result(id = true, property = "transId", column = "trans_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "partyA", column = "party_a", jdbcType = JdbcType.BIGINT, javaType = String.class),
            @Result(property = "partyB", column = "party_b", jdbcType = JdbcType.BIGINT, javaType = String.class),
            @Result(property = "partyC", column = "party_c", jdbcType = JdbcType.BIGINT, javaType = String.class),
            @Result(property = "contractFile", column = "contract_file", jdbcType = JdbcType.VARCHAR, javaType =
                    String.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<TransactionContract> findAll();

}
