package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.CarInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarInfoMapper {
    @Insert("insert into car_info (owner_id, make, model, license_plate_year, mileage) values (#{ownerId}, #{make}, " +
            "#{model}, #{licensePlateYear}, #{mileage})")
    @Options(keyProperty = "carId", useGeneratedKeys = true)
    Integer insert(CarInfo carInfo);

    @Update("update car_info set make = #{make}, model = #{model}, model_year = #{modelYear}, " +
            "license_plate_year = #{licensePlateYear}, mileage = #{mileage, jdbcType=INTEGER}, " +
            "is_automatic_transmission = #{isAutomaticTransmission, jdbcType=BOOLEAN}, gearbox = #{gearbox, " +
            "jdbcType=VARCHAR}, wheelbase = " +
            "#{wheelbase, jdbcType=SMALLINT}, engine_displacement = #{engineDisplacement, jdbcType=DECIMAL}, " +
            "horsepower = #{horsepower, jdbcType=SMALLINT}, cylinder = #{cylinder, jdbcType=VARCHAR}, " +
            "aspirate_mode = #{aspirateMode, jdbcType=VARCHAR}, fuel_type = #{fuelType, jdbcType=VARCHAR}, " +
            "fuel_marking = #{fuelMarking, jdbcType=CHAR}, drivetrain_type = #{drivetrainType, jdbcType=VARCHAR}," +
            " power_steering = #{powerSteering, jdbcType=VARCHAR}, brake_type = #{brakeType, jdbcType=VARCHAR}, " +
            "front_suspension = #{frontSuspension, jdbcType=VARCHAR}, rear_suspension = #{rearSuspension, jdbcType" +
            "=VARCHAR} where car_id = #{carId}")
    Integer updateById(CarInfo carInfo);

    @Select("select * from car_info where car_id = #{carId}")
    @ResultMap(value = "carInfoResultMap")
    CarInfo findById(@Param("carId") String carId);

    @Select("select concat(make, ' ', model) from car_info where car_id = #{carId}")
    String getConcatTitleById(@Param("carId") String carId);

    @Select("select * from car_info")
    @Results(id = "carInfoResultMap", value = {
            @Result(id = true, property = "carId", column = "car_id", jdbcType = JdbcType.BIGINT, javaType =
                    String.class),
            @Result(property = "ownerId", column = "owner_id", jdbcType = JdbcType.BIGINT, javaType = String.class),
            @Result(property = "make", column = "make", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "model", column = "model", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "modelYear", column = "model_year", javaType = String.class),
            @Result(property = "licensePlateYear", column = "license_plate_year", javaType = String.class),
            @Result(property = "mileage", column = "mileage", jdbcType = JdbcType.INTEGER, javaType = Long.class),
            @Result(property = "isAutomaticTransmission", column = "is_automatic_transmission", jdbcType = JdbcType.BOOLEAN
                    , javaType = Boolean.class),
            @Result(property = "gearbox", column = "gearbox", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "wheelbase", column = "wheelbase", jdbcType = JdbcType.SMALLINT, javaType = Integer.class),
            @Result(property = "engineDisplacement", column = "engine_displacement", jdbcType = JdbcType.DECIMAL, javaType
                    = BigDecimal.class),
            @Result(property = "horsepower", column = "horsepower", jdbcType = JdbcType.SMALLINT, javaType = Integer.class),
            @Result(property = "cylinder", column = "cylinder", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "aspirateMode", column = "aspirate_mode", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "fuelType", column = "fuel_type", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "fuelMarking", column = "fuel_marking", jdbcType = JdbcType.CHAR, javaType = String.class),
            @Result(property = "drivetrainType", column = "drivetrain_type", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "powerSteering", column = "power_steering", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "brakeType", column = "brake_type", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "frontSuspension", column = "front_suspension", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "rearSuspension", column = "rear_suspension", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Result(property = "createTime", column = "create_time", javaType = LocalDateTime.class),
            @Result(property = "updateTime", column = "update_time", javaType = LocalDateTime.class)
    })
    List<CarInfo> findAll();
}
