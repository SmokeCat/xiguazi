package com.smoke.xiguazi.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInfo {
    private String transId;
    private String carId;
    private String ownerId;
    private String buyerId;
    private String consultantId;
    private String city;
    private String address;
    private String carPicture;
    private Long price;
    private Integer transStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public TransactionInfo(String carId, String ownerId, String city) {
        this.carId = carId;
        this.ownerId = ownerId;
        this.city = city;
    }
}
