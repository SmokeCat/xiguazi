package com.smoke.xiguazi.model.factory;

import com.smoke.xiguazi.mapper.CarInfoMapper;
import com.smoke.xiguazi.mapper.DetectionTaskMapper;
import com.smoke.xiguazi.mapper.UserInfoMapper;
import com.smoke.xiguazi.model.po.TransactionInfo;
import com.smoke.xiguazi.model.vo.SellTransVo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class SellTransVoFactory {
    String transId;
    String price;
    String carPicture;
    String title;
    Integer status;
    String transConsultantPhone;
    String buyerPhone;
    String detectionConsultantPhone;
    String detectionAddress;
    LocalDate detectionDate;
    LocalDateTime createTime;

    public abstract SellTransVo create(TransactionInfo transInfo, CarInfoMapper carInfoMapper,
                                       DetectionTaskMapper detectionTaskMapper,
                                       UserInfoMapper userInfoMapper);

    protected String getTitle(TransactionInfo transInfo, CarInfoMapper carInfoMapper){
        return String.join(" ", carInfoMapper.getConcatTitleById(transInfo.getCarId()), transInfo.getCity());
    }
}
