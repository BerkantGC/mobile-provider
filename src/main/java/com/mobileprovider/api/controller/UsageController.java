package com.mobileprovider.api.controller;

import com.mobileprovider.api.dto.SigninDTO;
import com.mobileprovider.api.dto.TransactionStatus;
import com.mobileprovider.api.dto.UsageDTO;
import com.mobileprovider.api.model.Usage;
import com.mobileprovider.api.service.UsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@Tag(name = "Usage", description = "API for usage operations")
@RequestMapping("/api/v1/usage")
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    @PostMapping("/add")
    @Operation(description = "Add usage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "422")
    })
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<TransactionStatus> addUsage(@RequestBody UsageDTO usageDTO) {
        Usage usage = usageService.addUsage(usageDTO);

        TransactionStatus transactionStatus = new TransactionStatus();
        transactionStatus.setSuccess(true);
        transactionStatus.setMessage("Successfully added usage to the database");
        transactionStatus.setTransaction_id(usage.getId());

        return ResponseEntity.ok(transactionStatus);
    }
}
