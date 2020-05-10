package com.smoke.xiguazi.model.vo;

import com.smoke.xiguazi.model.po.CarInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarVo {
    private String carId;
    private String make;
    private String model;
    private String modelYear;
    private Long mileage;
    private Boolean isAutomaticTransmission;
    private BigDecimal engineDisplacement;

    public CarVo(CarInfo carInfo){
        this.carId = carInfo.getCarId();
        this.make = carInfo.getMake();
        this.model = carInfo.getModel();
        this.modelYear = carInfo.getModelYear() instanceof String modelYear ? modelYear : "";
        this.mileage = carInfo.getMileage();
        this.isAutomaticTransmission = carInfo.getIsAutomaticTransmission();
        this.engineDisplacement = carInfo.getEngineDisplacement();
    }
}
