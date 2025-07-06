
package com.agentic.cicd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MonitorController {

    record RunRecord(String timestamp, int failures, String status, String conclusion, String diagnosis) {}

    @GetMapping("/cicd-history")
    public List<RunRecord> getHistory() {
        return List.of(
                new RunRecord("2025-06-19T06:00:00Z", 3, "completed", "failure", "Expired secret key in GitHub Actions"),
                new RunRecord("2025-06-18T06:00:00Z", 1, "completed", "success", "N/A"),
                new RunRecord("2025-06-17T06:00:00Z", 2, "completed", "failure", "Dependency version conflict")
        );
    }
}
