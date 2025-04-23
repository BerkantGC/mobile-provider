package com.mobileprovider.api.dto;

import com.mobileprovider.api.enumaration.Month;
import lombok.Data;

@Data
public class BillDTO {
    private String subscriber_number;
    private Month month;
    private int year;
}
