package com.smoke.xiguazi.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInfo {
    private String carId;
    private String ownerId;
    private String make;
    private String model;
    private String modelYear;
    private String licensePlateYear;
    private Long mileage;
    private Boolean isAutomaticTransmission;
    private String gearbox;
    private Integer wheelbase;
    private BigDecimal engineDisplacement;
    private Integer horsepower;
    private String cylinder;
    private String aspirateMode;
    private String fuelType;
    private String fuelMarking;
    private String drivetrainType;
    private String powerSteering;
    private String brakeType;
    private String frontSuspension;
    private String rearSuspension;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public CarInfo(String ownerId, String make, String model, String licensePlateYear, Long mileage) {
        this.ownerId = ownerId;
        this.make = make;
        this.model = model;
        this.licensePlateYear = licensePlateYear;
        this.mileage = mileage;
    }
}
