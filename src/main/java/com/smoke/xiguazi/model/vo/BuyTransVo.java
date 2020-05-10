package com.smoke.xiguazi.model.vo;

import java.time.LocalDateTime;

public record BuyTransVo(String transId, String price, String carPicture, String title, String status,
                         String transConsultantPhone, String ownerPhone, LocalDateTime buyTime) {}
