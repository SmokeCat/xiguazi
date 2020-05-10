package com.smoke.xiguazi.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransSearchParam {
    private String city = "all";
    private String make = "all";
    private String mileage = "all";
    private String enginedisplament = "all";
    private String transmission = "all";
    private String price = "all";
}
