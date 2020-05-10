package com.smoke.xiguazi.model.factory;

import com.smoke.xiguazi.mapper.CarInfoMapper;
import com.smoke.xiguazi.mapper.DetectionTaskMapper;
import com.smoke.xiguazi.mapper.UserInfoMapper;
import com.smoke.xiguazi.model.po.TransactionInfo;
import com.smoke.xiguazi.model.vo.SellTransVo;

public class SellTransVo1Factory extends SellTransVoFactory {
    @Override
    public SellTransVo create(TransactionInfo transInfo, CarInfoMapper carInfoMapper, DetectionTaskMapper detectionTaskMapper, UserInfoMapper userInfoMapper) {
        transId = transInfo.getTransId();
        price = transInfo.getPrice().toString();
        carPicture = transInfo.getCarPicture().split(";")[0];
        title = getTitle(transInfo, carInfoMapper);
        status = transInfo.getTransStatus();
        transConsultantPhone = userInfoMapper.findPhoneById(transInfo.getConsultantId());
        buyerPhone = "";
        detectionConsultantPhone = "";
        detectionAddress = "";
        detectionDate = null;
        createTime = transInfo.getCreateTime();

        return new SellTransVo(transId, price, carPicture, title, status.toString(), transConsultantPhone, buyerPhone,
                detectionConsultantPhone, detectionAddress, detectionDate, createTime);
    }
}
