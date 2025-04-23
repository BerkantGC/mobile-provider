package com.mobileprovider.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BillResponse {
    private Float total;
    private Boolean is_paid;
}
