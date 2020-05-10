package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.CarInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarInfoMapperTest {

    @Autowired
    CarInfoMapper carInfoMapper;

    @Test
    void findAll() {
        List<CarInfo> all = carInfoMapper.findAll();
        for (int i = 0; i < 10; i++) {
            System.out.println(all.get(i));
        }
    }

    @Test
    void updateById() {
        CarInfo carInfo = carInfoMapper.findById("1");
        carInfo.setModelYear("1919");
        carInfo.setLicensePlateYear("1929");
        carInfoMapper.updateById(carInfo);
    }
}