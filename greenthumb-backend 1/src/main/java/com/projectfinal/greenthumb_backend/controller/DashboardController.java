package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.DashboardDTO;
import com.projectfinal.greenthumb_backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<DashboardDTO> getMetrics() {
        DashboardDTO metrics = dashboardService.getDashboardMetrics();
        return ResponseEntity.ok(metrics);
    }
}