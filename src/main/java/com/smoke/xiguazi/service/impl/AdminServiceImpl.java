package com.smoke.xiguazi.service.impl;

import com.smoke.xiguazi.mapper.UserInfoMapper;
import com.smoke.xiguazi.model.po.UserInfo;
import com.smoke.xiguazi.service.AdminService;
import com.smoke.xiguazi.utils.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserInfoMapper userInfoMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminServiceImpl(UserInfoMapper userInfoMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userInfoMapper = userInfoMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 创建一个顾问账号，使用默认密码
     * @param phone
     * @return
     */
    @Override
    public String addConsultant(String phone) {
        //  加密密码
        UserInfo userInfo = new UserInfo(phone,  bCryptPasswordEncoder.encode(ConstUtil.DEFAULT_PASSWORD),
                ConstUtil.ROLE_CONSULTANT_ID);
        userInfoMapper.insert(userInfo);
        return ConstUtil.DEFAULT_PASSWORD;
    }
}
