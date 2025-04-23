package com.mobileprovider.api.service;

import com.mobileprovider.api.dto.BillDTO;
import com.mobileprovider.api.dto.BillDetailedResponse;
import com.mobileprovider.api.dto.PayBillDTO;
import com.mobileprovider.api.enumaration.UsageType;
import com.mobileprovider.api.model.Bill;
import com.mobileprovider.api.model.Usage;
import com.mobileprovider.api.repository.BillRepository;
import com.mobileprovider.api.repository.UsageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.List;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final UsageRepository usageRepository;

    public BillService(BillRepository billRepository, UsageRepository usageRepository) {
        this.billRepository = billRepository;
        this.usageRepository = usageRepository;
    }

    // Constants for billing calculations
    private static final int FREE_PHONE_MINUTES = 1000;
    private static final double PHONE_RATE_PER_1000 = 10.0;
    private static final double INTERNET_BASE_RATE = 50.0;
    private static final int INTERNET_BASE_LIMIT_MB = 20 * 1024; // 20GB in MB
    private static final double INTERNET_ADDITIONAL_RATE = 10.0;
    private static final int INTERNET_ADDITIONAL_BLOCK_MB = 10 * 1024; // 10GB in MB

    public Bill queryBill(BillDTO billDTO) {
        return billRepository.findBillBySubscriberNumberAndMonthAndYear(
                billDTO.getSubscriber_number(),
                billDTO.getMonth(),
                billDTO.getYear()
        );
    }

    public HashMap<String, Object> queryBillDetailed(BillDTO billDTO, PageRequest pageRequest) {
        Bill bill = billRepository.findBillBySubscriberNumberAndMonthAndYear(
                billDTO.getSubscriber_number(),
                billDTO.getMonth(),
                billDTO.getYear()
        );
        float total = bill.getInternet_bill_amount() + bill.getPhone_bill_amount();
        List<Usage> usageList = usageRepository.findBySubscriberNoAndMonthAndYear(billDTO.getSubscriber_number(), billDTO.getMonth(), billDTO.getYear(), pageRequest);

        BillDetailedResponse billDetailedResponse = new BillDetailedResponse();
        billDetailedResponse.setTotal(total);
        billDetailedResponse.setIs_paid(bill.getAmount_paid() == total);
        billDetailedResponse.setInternet_MB_used(bill.getInternet_bill_amount() + " MB");
        billDetailedResponse.setPhone_minutes_used(bill.getPhone_bill_amount() + " Minutes");

        HashMap<String, Object> response = new HashMap<>();
        response.put("usage_list_size", pageRequest.getPageSize());
        response.put("usage_list_page", pageRequest.getPageNumber() + 1);
        response.put("usage_list", usageList);
        response.put("bill", billDetailedResponse);

        return response;
    }

    public Bill payBill(PayBillDTO payBillDTO) {
        Bill bill = billRepository.findBillBySubscriberNumberAndMonthAndYear(
                payBillDTO.getSubscriber_number(),
                payBillDTO.getMonth(),
                payBillDTO.getYear()
        );

        if (bill == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill not found");

        if (payBillDTO.getAmount() == null || payBillDTO.getAmount() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid amount");

        if (bill.getIsPaid())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Bill is already paid");

        float total = bill.getInternet_bill_amount() + bill.getPhone_bill_amount();
        Float amountPaid = (float) bill.getAmount_paid();

        if (payBillDTO.getAmount() + amountPaid < total) {
            bill.setAmount_paid(payBillDTO.getAmount() + amountPaid);
            bill.set_paid(false);
            billRepository.save(bill);

            return bill;
        }

        if (payBillDTO.getAmount() + amountPaid >= total) {
            bill.setAmount_paid(payBillDTO.getAmount() + amountPaid);
            bill.set_paid(true);
            billRepository.save(bill);

            return bill;
        }

        return bill;
    }

    /*
    Calculates bill for given month for
    given subscriber
    Minutes of Phone Calls – 1000
    minutes free. Each 1000 minutes is
    10$ after

    Internet Usage – 50$ up to 20GB,
    10$ for each 10GB after
    */
    public Bill calculate(BillDTO billDTO) {

        List<Usage> usageList = usageRepository.findBySubscriberNoAndMonthAndYear(
                billDTO.getSubscriber_number(),
                billDTO.getMonth(),
                billDTO.getYear()
        );

        if (usageList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usage data not found");
        }

        double internetUsageMB = 0.0;
        double phoneUsageMinutes = 0.0;

        for (Usage usage : usageList) {
            if (usage.getType() == UsageType.PHONE) {
                phoneUsageMinutes += usage.getAmount();
            } else if (usage.getType() == UsageType.INTERNET) {
                internetUsageMB += usage.getAmount();
            }
        }

        // Calculate phone bill
        double phoneBill = 0.0;
        if (phoneUsageMinutes > FREE_PHONE_MINUTES) {
            double extraMinutes = phoneUsageMinutes - FREE_PHONE_MINUTES;
            phoneBill = Math.ceil(extraMinutes / 1000.0) * PHONE_RATE_PER_1000;
        }

        // Calculate internet bill
        double internetBill = INTERNET_BASE_RATE;
        if (internetUsageMB > INTERNET_BASE_LIMIT_MB) {
            double extraMB = internetUsageMB - INTERNET_BASE_LIMIT_MB;
            internetBill += Math.ceil(extraMB / INTERNET_ADDITIONAL_BLOCK_MB) * INTERNET_ADDITIONAL_RATE;
        }

        Bill bill = billRepository.findBillBySubscriberNumberAndMonthAndYear(
                billDTO.getSubscriber_number(),
                billDTO.getMonth(),
                billDTO.getYear()
        );

        if (bill == null) {
            bill = new Bill();
            bill.setSubscriberNumber(billDTO.getSubscriber_number());
            bill.setMonth(billDTO.getMonth());
            bill.setYear((long) billDTO.getYear());
            bill.setAmount_paid(0.0f);
            bill.set_paid(false);
        }

        bill.setPhone_bill_amount((float) phoneBill);
        bill.setInternet_bill_amount((float) internetBill);

        billRepository.save(bill);
        return bill;
    }

}
