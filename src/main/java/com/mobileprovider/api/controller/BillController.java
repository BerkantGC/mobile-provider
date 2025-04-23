package com.mobileprovider.api.controller;

import com.mobileprovider.api.dto.BillDTO;
import com.mobileprovider.api.dto.BillDetailedResponse;
import com.mobileprovider.api.dto.BillResponse;
import com.mobileprovider.api.dto.PayBillDTO;
import com.mobileprovider.api.model.Bill;
import com.mobileprovider.api.model.Usage;
import com.mobileprovider.api.repository.UsageRepository;
import com.mobileprovider.api.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/bill")
@Tag(name = "Bill Controller", description = "API for billing operations")
public class BillController {
    private final BillService billService;
    private final UsageRepository usageRepository;

    public BillController(BillService billService, UsageRepository usageRepository) {
        this.billService = billService;
        this.usageRepository = usageRepository;
    }

    @PostMapping("/calculate")
    @Operation(description = "Calculate Bill")
    public ResponseEntity<BillResponse> calculateBill(@RequestBody BillDTO billDTO) {
            Bill bill = billService.calculate(billDTO);
            BillResponse billResponse = new BillResponse();

            billResponse.setIs_paid(bill.is_paid());
            billResponse.setTotal(bill.getInternet_bill_amount() + bill.getPhone_bill_amount());

            return ResponseEntity.ok(billResponse);
    }

    @GetMapping
    @Operation(description = "Query Bill",
        responses = {
               @ApiResponse(responseCode = "200", description = "Bill Total, Paid Status")
        }
    )
    public ResponseEntity<BillResponse> getBill(@ModelAttribute BillDTO billDTO) {
        Bill bill = billService.queryBill(billDTO);

        if (bill == null)
            return ResponseEntity.notFound().build();

        BillResponse billResponse = new BillResponse();
        float total = bill.getInternet_bill_amount() + bill.getPhone_bill_amount();
        billResponse.setTotal(total);
        billResponse.setIs_paid(bill.getAmount_paid() == total);
        return ResponseEntity.ok(billResponse);
    }

    @GetMapping("/details")
    @Operation(summary = "Get usage info", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
        {
          "usage_list": [
            {
              "id": 5,
              "subscriberNo": "string",
              "month": "JANUARY",
              "year": 2025,
              "type": "PHONE",
              "amount": 200
            },
            {
              "id": 6,
              "subscriberNo": "string",
              "month": "JANUARY",
              "year": 2025,
              "type": "INTERNET",
              "amount": 200
            },
            {
              "id": 7,
              "subscriberNo": "string",
              "month": "JANUARY",
              "year": 2025,
              "type": "INTERNET",
              "amount": 550
            }
          ],
          "usage_list_size": 10,
          "usage_list_page": 1,
          "bill": {
            "total": 50,
            "is_paid": true,
            "phone_minutes_used": "0.0 Minutes",
            "internet_MB_used": "50.0 MB"
          }
        }
        """))
            )
    })
    public ResponseEntity<HashMap<String, Object>> getBillDetailed(
            @Parameter(description = "Subscriber number, month, and year for the bill")
            @ModelAttribute BillDTO billDTO,

            @Parameter(description = "Page number (starts from 1)")
            @RequestParam(name = "page", defaultValue = "1") Integer page,

            @Parameter(description = "Number of usage entries per page")
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        HashMap<String, Object> response = billService.queryBillDetailed(billDTO, pageRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/pay")
    public ResponseEntity<HashMap<String, Object>> payBill(@RequestBody PayBillDTO payBillDTO) {
        Bill bill = billService.payBill(payBillDTO);

        HashMap<String, Object> response = new HashMap<>();
        response.put("payment_status", bill.getIsPaid() ? "Successful" : "Error");
        response.put("message", bill.getIsPaid()
                ? "Payment successful"
                : (bill.getInternet_bill_amount() + bill.getPhone_bill_amount() - bill.getAmount_paid()) + "$ more is needed");


        return ResponseEntity.ok(response);
    }
}
