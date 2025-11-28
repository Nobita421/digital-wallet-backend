package com.wallet.controller;

import com.wallet.dto.BillRequest;
import com.wallet.dto.BillResponse;
import com.wallet.service.BillService;
import com.wallet.repository.UserRepository;
import com.wallet.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private UserRepository userRepository;

    private User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getBills(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        try {
            User user = getUser(authentication);
            Page<BillResponse> bills = billService.getBillsByUserId(user.getId(), page, limit);

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
            Authentication authentication,
            @Valid @RequestBody BillRequest request) {

        try {
            User user = getUser(authentication);
            BillResponse bill = billService.createBill(user.getId(), request);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<BillResponse> payBill(
            @RequestParam String billId,
            Authentication authentication) {

        try {
            Long billIdLong = Long.parseLong(billId);
            User user = getUser(authentication);
            BillResponse bill = billService.payBill(billIdLong, user.getId());
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueBills(Authentication authentication) {
        try {
            User user = getUser(authentication);
            var bills = billService.getOverdueBills(user.getId());
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingBills(
            Authentication authentication,
            @RequestParam(defaultValue = "7") int days) {
        try {
            User user = getUser(authentication);
            var bills = billService.getUpcomingBills(user.getId(), days);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}