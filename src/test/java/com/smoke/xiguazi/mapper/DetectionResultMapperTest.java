package com.smoke.xiguazi.mapper;

import com.smoke.xiguazi.model.po.DetectionResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DetectionResultMapperTest {
    @Autowired
    DetectionResultMapper detectionResultMapper;

    @Test
    void insert() {
        DetectionResult detectionResult = new DetectionResult("1", 1, "10", 1, "没事", null, null);
        detectionResultMapper.insert(detectionResult);
    }
}