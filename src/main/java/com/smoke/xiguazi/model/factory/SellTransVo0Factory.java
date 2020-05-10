package com.smoke.xiguazi.model.factory;

import com.smoke.xiguazi.mapper.CarInfoMapper;
import com.smoke.xiguazi.mapper.DetectionTaskMapper;
import com.smoke.xiguazi.mapper.UserInfoMapper;
import com.smoke.xiguazi.model.po.DetectionTask;
import com.smoke.xiguazi.model.po.TransactionInfo;
import com.smoke.xiguazi.model.vo.SellTransVo;

public class SellTransVo0Factory extends SellTransVoFactory {

    @Override
    public SellTransVo create(TransactionInfo transInfo, CarInfoMapper carInfoMapper, DetectionTaskMapper detectionTaskMapper, UserInfoMapper userInfoMapper) {
        transId = transInfo.getTransId();

        DetectionTask detectionTask = detectionTaskMapper.findByTransId(transId);

        price = "";
        carPicture = "";
        title = getTitle(transInfo, carInfoMapper);
        status = transInfo.getTransStatus();
        transConsultantPhone = "待分配";
        buyerPhone = "";
        detectionConsultantPhone = detectionTask.taskStatus() == 0 ? "待分配" :
                userInfoMapper.findPhoneById(detectionTask.consultantId());
        detectionAddress = detectionTask.taskStatus() == 0 ? "待分配" : detectionTask.address();
        detectionDate = detectionTask.meetDate();
        createTime = transInfo.getCreateTime();

        return new SellTransVo(transId, price, carPicture, title, status.toString(), transConsultantPhone, buyerPhone,
                detectionConsultantPhone, detectionAddress, detectionDate, createTime);
    }
}
