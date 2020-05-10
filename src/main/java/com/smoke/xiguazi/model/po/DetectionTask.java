package com.smoke.xiguazi.model.po;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DetectionTask(String transId, String consultantId, String address, LocalDate meetDate, String note,
                            Integer taskStatus, LocalDateTime createTime, LocalDateTime updateTime) {}
