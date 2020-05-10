package com.smoke.xiguazi.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectionResult {
    private String transId;
    private Integer itemId;
    private String consultantId;
    private Integer problemLevel;
    private String problemDescription;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
