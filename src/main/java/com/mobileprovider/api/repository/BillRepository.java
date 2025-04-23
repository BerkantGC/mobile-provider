package com.mobileprovider.api.repository;

import com.mobileprovider.api.enumaration.Month;
import com.mobileprovider.api.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BillRepository extends JpaRepository<Bill, Long> {
    Bill findBillBySubscriberNumberAndMonthAndYear(String subscriber_number, Month month, int year);
}
