package com.smoke.xiguazi.model.factory;

import com.smoke.xiguazi.mapper.CarInfoMapper;
import com.smoke.xiguazi.mapper.DetectionTaskMapper;
import com.smoke.xiguazi.mapper.UserInfoMapper;
import com.smoke.xiguazi.model.po.TransactionInfo;
import com.smoke.xiguazi.model.vo.SellTransVo;

public class SellTransVo3Factory extends SellTransVoFactory {
    @Override
    public SellTransVo create(TransactionInfo transInfo, CarInfoMapper carInfoMapper, DetectionTaskMapper detectionTaskMapper, UserInfoMapper userInfoMapper) {
        return null;
    }
}
