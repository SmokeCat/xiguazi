package com.smoke.xiguazi.model.vo;

import com.smoke.xiguazi.model.po.CarInfo;
import com.smoke.xiguazi.model.po.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransVo {
    private String transId;
    private String consultantId;
    private CarVo car;
    private String city;
    private String carPicture;
    private Long price;
    private LocalDateTime createTime;

    public TransVo(TransactionInfo transInfo, CarInfo carInfo){
        this.transId = transInfo.getTransId();
        this.consultantId = transInfo.getConsultantId() instanceof String consultantId? consultantId : null;
        this.car = new CarVo(carInfo);
        this.city = transInfo.getCity();
        this.carPicture = transInfo.getCarPicture() instanceof String picture? picture.split(";")[0] : null;
        this.price = transInfo.getPrice() instanceof Long price ? price : null;
        this.createTime = transInfo.getCreateTime();
    }
}
