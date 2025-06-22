package com.example.etl.controller;

import com.example.etl.service.EtlJobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/etl")
public class JobController {

    @Autowired
    private EtlJobService jobService;

    @PostMapping("/run-etl-job")
    public ResponseEntity<Map<String, Object>> runEtl() {
        Map<String, Object> res = jobService.runJob();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getJobStatus() {
        boolean isRunning = jobService.isJobRunning();
        Map<String, Object> response = new HashMap<>();
        response.put("status", isRunning ? "running" : "idle");
        return ResponseEntity.ok(response);
    }
}
