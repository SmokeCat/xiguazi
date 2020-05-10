package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.CarModel;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarModelMapper {

    @Select("select distinct make from car_model")
    List<String> findAllMake();

    @Select("select * from car_model")
    @Results(id = "carModelResultMap", value = {
            @Result(id = true, property = "make", column = "make", jdbcType = JdbcType.VARCHAR, javaType =
                    String.class),
            @Result(id = true, property = "model", column = "model", jdbcType = JdbcType.VARCHAR, javaType =
                    String.class)
    })
    List<CarModel> findAll();
}
