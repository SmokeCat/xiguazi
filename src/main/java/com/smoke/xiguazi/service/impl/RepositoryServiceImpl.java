package com.smoke.xiguazi.service.impl;

import com.smoke.xiguazi.mapper.CarModelMapper;
import com.smoke.xiguazi.mapper.DetectionItemMapper;
import com.smoke.xiguazi.mapper.SysRoleMapper;
import com.smoke.xiguazi.mapper.TransactionInfoMapper;
import com.smoke.xiguazi.model.po.DetectionItem;
import com.smoke.xiguazi.model.po.SysRole;
import com.smoke.xiguazi.service.RepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RepositoryServiceImpl implements RepositoryService {
    private final SysRoleMapper sysRoleMapper;
    private final CarModelMapper carModelMapper;
    private final TransactionInfoMapper transactionInfoMapper;
    private final DetectionItemMapper detectionItemMapper;

    @Autowired
    public RepositoryServiceImpl(SysRoleMapper sysRoleMapper, CarModelMapper carModelMapper,
                                 TransactionInfoMapper transactionInfoMapper, DetectionItemMapper detectionItemMapper){
        this.sysRoleMapper = sysRoleMapper;
        this.carModelMapper = carModelMapper;
        this.transactionInfoMapper = transactionInfoMapper;
        this.detectionItemMapper = detectionItemMapper;
    }

    @Override
    public Map<Integer, String> getRoleIdToName() {
        List<SysRole> sysRoleList = sysRoleMapper.findAll();
        Map<Integer, String> roleIdToName = new HashMap<>();

        for (SysRole sysRole : sysRoleList) {
            roleIdToName.put(sysRole.getRoleId(), sysRole.getRoleName());
        }

        return roleIdToName;
    }

    @Override
    public List<String> getCarMakeList() {
        return carModelMapper.findAllMake();
    }

    @Override
    public Map<String, String> getCarMakeMap() {
        Map<String, String> carMakeMap = new HashMap<>();
        int i = 0;
        for (String s : carModelMapper.findAllMake()) {
            carMakeMap.put(String.valueOf(i++), s);
        }
        return carMakeMap;
    }

    @Override
    public Map<String, String> getCityMap() {
        Map<String, String> cityMap = new HashMap<>();
        int i = 0;
        for (String s : transactionInfoMapper.findCityList()) {
            cityMap.put(String.valueOf(i++), s);
        }
        return cityMap;
    }

    @Override
    public List<DetectionItem> getDetectionItemList() {
        return detectionItemMapper.findAll();
    }


    @Override
    public Map<String, List<DetectionItem>> getDetectTypeItemListMap(List<DetectionItem> detectionItemList) {
        Map<String, List<DetectionItem>> detectItemMap = new HashMap<>();
        for (DetectionItem detectionItem : detectionItemList) {
            if(!detectItemMap.containsKey(detectionItem.detectionType())){
                detectItemMap.put(detectionItem.detectionType(), new ArrayList<>());
            }
            detectItemMap.get(detectionItem.detectionType()).add(detectionItem);
        }
        return detectItemMap;
    }

    @Override
    public Map<Integer, DetectionItem> getDetectIdItemMap(List<DetectionItem> detectionItemList) {
        Map<Integer, DetectionItem> detectIdItemMap = new HashMap<>();
        detectionItemList.forEach(detectionItem -> detectIdItemMap.put(detectionItem.itemId(), detectionItem));
        return detectIdItemMap;
    }


}
