package com.smoke.xiguazi.model.po;

import java.time.LocalDate;
import java.time.LocalDateTime;
public record Reservation(String transId, String bookerId, String address, LocalDate meetDate,
                          LocalDateTime createTime, LocalDateTime updateTime) {}
