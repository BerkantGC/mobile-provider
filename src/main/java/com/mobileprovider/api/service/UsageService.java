package com.mobileprovider.api.service;

import com.mobileprovider.api.dto.UsageDTO;
import com.mobileprovider.api.model.Usage;
import com.mobileprovider.api.repository.UsageRepository;
import org.springframework.stereotype.Service;

@Service
public class UsageService {
    private final UsageRepository usageRepository;
    public UsageService(UsageRepository usageRepository) {
        this.usageRepository = usageRepository;
    }

    public Usage addUsage(UsageDTO usageDTO) {
        Usage usage = new Usage();
        usage.setAmount(usageDTO.getAmount());
        usage.setMonth(usageDTO.getMonth());
        usage.setYear(usageDTO.getYear());
        usage.setSubscriberNo(usageDTO.getSubscriber_number());
        usage.setType(usageDTO.getUsage_type());

        return usageRepository.save(usage);
    }
}
