package com.financial.platform.controller;

import com.financial.platform.model.dto.RetirementProjectionResponse;
import com.financial.platform.service.RetirementProjectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for retirement projections. Requires authentication.
 * GET /retirement/projection/{customerId}
 */
@RestController
@RequestMapping("/retirement")
public class RetirementController {

    private final RetirementProjectionService retirementProjectionService;

    public RetirementController(RetirementProjectionService retirementProjectionService) {
        this.retirementProjectionService = retirementProjectionService;
    }

    @GetMapping("/projection/{customerId}")
    public ResponseEntity<RetirementProjectionResponse> getProjection(@PathVariable Long customerId) {
        RetirementProjectionResponse response = retirementProjectionService.getProjection(customerId);
        return ResponseEntity.ok(response);
    }
}
