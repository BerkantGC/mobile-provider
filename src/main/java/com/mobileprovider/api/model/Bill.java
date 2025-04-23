package com.mobileprovider.api.model;

import com.mobileprovider.api.enumaration.Month;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bill")
public class Bill {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "bill_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_seq")
    private Long id;


    @Column(name = "subscriber_number")
    private String subscriberNumber;
    private Month month;
    private Long year;

    private Float phone_bill_amount;
    private Float internet_bill_amount;

    private boolean is_paid;

    public boolean getIsPaid() {
        return is_paid;
    }

    private double amount_paid;
}
