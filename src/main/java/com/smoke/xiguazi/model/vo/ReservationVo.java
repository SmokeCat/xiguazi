package com.smoke.xiguazi.model.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationVo(String transId, String price, String carPicture, String title, String status,
                            String address, LocalDate meetDate, String ownerPhone, String transConsultantPhone,
                            LocalDateTime createTime) {}
