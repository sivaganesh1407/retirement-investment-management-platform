package com.financial.platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Public welcome endpoint so opening the root URL in a browser does not return 403.
 */
@RestController
public class WelcomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> welcome() {
        return ResponseEntity.ok(Map.of(
                "application", "Retirement Investment Management Platform",
                "status", "running",
                "docs", "Use POST /auth/register or POST /auth/login to get a JWT. Health: GET /actuator/health"
        ));
    }
}
