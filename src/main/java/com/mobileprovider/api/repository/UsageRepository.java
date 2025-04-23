package com.mobileprovider.api.repository;

import com.mobileprovider.api.enumaration.Month;
import com.mobileprovider.api.model.Usage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;


public interface UsageRepository extends JpaRepository<Usage, Long> {
    List<Usage> findBySubscriberNoAndMonthAndYear(String subscriberNo, Month month, int year);
    List<Usage> findBySubscriberNoAndMonthAndYear(String subscriberNo, Month month, int year, PageRequest pageable);
}
