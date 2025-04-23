package com.mobileprovider.api.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BillDetailedResponse extends BillResponse {
    private String phone_minutes_used;
    private String internet_MB_used;
}
