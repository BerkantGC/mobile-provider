package com.mobileprovider.api.dto;

import com.mobileprovider.api.enumaration.Month;
import com.mobileprovider.api.enumaration.UsageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsageDTO {
    private String subscriber_number;
    private Long amount;
    private int year;
    private Month month;
    private UsageType usage_type;
}
