package com.wallet.controller;

import com.wallet.dto.BudgetRequest;
import com.wallet.dto.BudgetResponse;
import com.wallet.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getBudgets(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        try {
            Long userIdLong = Long.parseLong(userId);
            Page<BudgetResponse> budgets = budgetService.getBudgetsByUserId(userIdLong, page, limit);

            Map<String, Object> response = new HashMap<>();
            response.put("budgets", budgets.getContent());

            Map<String, Object> pagination = new HashMap<>();
            pagination.put("total", budgets.getTotalElements());
            pagination.put("limit", limit);
            pagination.put("offset", page * limit);
            pagination.put("hasMore", budgets.hasNext());
            response.put("pagination", pagination);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @RequestParam String userId,
            @Valid @RequestBody BudgetRequest request) {

        try {
            Long userIdLong = Long.parseLong(userId);
            BudgetResponse budget = budgetService.createBudget(userIdLong, request);
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable String budgetId,
            @RequestParam String userId,
            @Valid @RequestBody BudgetRequest request) {

        try {
            Long budgetIdLong = Long.parseLong(budgetId);
            Long userIdLong = Long.parseLong(userId);
            BudgetResponse budget = budgetService.updateBudget(budgetIdLong, userIdLong, request);
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<?> deleteBudget(
            @PathVariable String budgetId,
            @RequestParam String userId) {

        try {
            Long budgetIdLong = Long.parseLong(budgetId);
            Long userIdLong = Long.parseLong(userId);
            budgetService.deleteBudget(budgetIdLong, userIdLong);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/update-spending")
    public ResponseEntity<?> updateBudgetSpending(@RequestParam String userId) {
        try {
            Long userIdLong = Long.parseLong(userId);
            budgetService.updateBudgetSpending(userIdLong);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}