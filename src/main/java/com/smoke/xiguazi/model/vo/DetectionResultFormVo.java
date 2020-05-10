package com.smoke.xiguazi.model.vo;

import com.smoke.xiguazi.model.po.CarInfo;
import com.smoke.xiguazi.model.po.DetectionResult;
import com.smoke.xiguazi.model.po.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectionResultFormVo{
    private String consultantId;
    private TransactionInfo transactionInfo;
    private CarInfo carInfo;
    private String ownerPhone;
    private List<DetectionResult> detectionResultList;
}
