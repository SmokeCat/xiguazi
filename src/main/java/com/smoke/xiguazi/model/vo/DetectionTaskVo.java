package com.smoke.xiguazi.model.vo;

import java.time.LocalDate;

public record DetectionTaskVo(String transId, String carId, String title, String address, String ownerPhone,
                              String note, LocalDate meetDate) {}
