package com.mobileprovider.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PayBillDTO extends BillDTO {
    private Float amount;
}
