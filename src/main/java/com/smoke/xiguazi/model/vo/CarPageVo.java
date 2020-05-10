package com.smoke.xiguazi.model.vo;

import com.smoke.xiguazi.model.po.CarInfo;
import com.smoke.xiguazi.model.po.DetectionResult;
import com.smoke.xiguazi.model.po.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarPageVo {
    private String transId;
    private CarInfo carInfo;
    private Map<Integer, DetectionResult> detectionResults;

    private String consultantPhone;

    private String city;
    private List<String> carPicture;
    private Long price;
    private Integer status;

    private Boolean isInFavourite;
    private Boolean isReserved;

    public CarPageVo(TransactionInfo transactionInfo, CarInfo carInfo, Map<Integer, DetectionResult> detectionResults,
                     String consultantPhone, Integer status, Boolean isInFavourite, Boolean isReserved) {
        this.transId = transactionInfo.getTransId();
        this.carInfo = carInfo;
        this.detectionResults = detectionResults;
        this.consultantPhone = consultantPhone;
        this.city = transactionInfo.getCity();
        this.carPicture = Arrays.asList(transactionInfo.getCarPicture().split(";"));
        this.price = transactionInfo.getPrice();
        this.status = status;
        this.isInFavourite = isInFavourite;
        this.isReserved = isReserved;
    }
}
