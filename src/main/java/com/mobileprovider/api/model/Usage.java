package com.mobileprovider.api.model;

import com.mobileprovider.api.enumaration.Month;
import com.mobileprovider.api.enumaration.UsageType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "usages")
public class Usage {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "usage_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usage_seq")
    private Long id;

    @Column(name = "subscriber_number")
    private String subscriberNo;

    @Enumerated
    private Month month;

    private int year;

    private Long Amount;

    @Enumerated(EnumType.STRING)
    private UsageType type;
}
