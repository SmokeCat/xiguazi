package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.CarModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarModelMapperTest {

    @Autowired
    CarModelMapper carModelMapper;

    @Test
    void findAll() {
        List<CarModel> all = carModelMapper.findAll();
        for (CarModel carModel : all) {
            System.out.println(carModel);
        }
    }
}