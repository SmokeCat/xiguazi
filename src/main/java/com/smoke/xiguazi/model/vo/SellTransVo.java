package com.smoke.xiguazi.model.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SellTransVo(String transId, String price, String carPicture, String title, String status,
                          String transConsultantPhone, String buyerPhone, String detectionConsultantPhone,
                          String detectionAddress, LocalDate detectionDate, LocalDateTime createTime) {}
