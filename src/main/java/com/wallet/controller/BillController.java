package com.wallet.controller;

import com.wallet.dto.BillRequest;
import com.wallet.dto.BillResponse;
import com.wallet.service.BillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getBills(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        try {
            Long userIdLong = Long.parseLong(userId);
            Page<BillResponse> bills = billService.getBillsByUserId(userIdLong, page, limit);

            Map<String, Object> response = new HashMap<>();
            response.put("bills", bills.getContent());

            Map<String, Object> pagination = new HashMap<>();
            pagination.put("total", bills.getTotalElements());
            pagination.put("limit", limit);
            pagination.put("offset", page * limit);
            pagination.put("hasMore", bills.hasNext());
            response.put("pagination", pagination);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<BillResponse> createBill(
            @RequestParam String userId,
            @Valid @RequestBody BillRequest request) {

        try {
            Long userIdLong = Long.parseLong(userId);
            BillResponse bill = billService.createBill(userIdLong, request);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<BillResponse> payBill(
            @RequestParam String billId,
            @RequestParam String userId) {

        try {
            Long billIdLong = Long.parseLong(billId);
            Long userIdLong = Long.parseLong(userId);
            BillResponse bill = billService.payBill(billIdLong, userIdLong);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueBills(@RequestParam String userId) {
        try {
            Long userIdLong = Long.parseLong(userId);
            var bills = billService.getOverdueBills(userIdLong);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingBills(
            @RequestParam String userId,
            @RequestParam(defaultValue = "7") int days) {
        try {
            Long userIdLong = Long.parseLong(userId);
            var bills = billService.getUpcomingBills(userIdLong, days);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}