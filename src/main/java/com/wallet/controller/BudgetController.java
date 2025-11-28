package com.wallet.controller;

import com.wallet.dto.BudgetRequest;
import com.wallet.dto.BudgetResponse;
import com.wallet.service.BudgetService;
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
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserRepository userRepository;

    private User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getBudgets(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        try {
            User user = getUser(authentication);
            Page<BudgetResponse> budgets = budgetService.getBudgetsByUserId(user.getId(), page, limit);

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
            Authentication authentication,
            @Valid @RequestBody BudgetRequest request) {

        try {
            User user = getUser(authentication);
            BudgetResponse budget = budgetService.createBudget(user.getId(), request);
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable String budgetId,
            Authentication authentication,
            @Valid @RequestBody BudgetRequest request) {

        try {
            Long budgetIdLong = Long.parseLong(budgetId);
            User user = getUser(authentication);
            BudgetResponse budget = budgetService.updateBudget(budgetIdLong, user.getId(), request);
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<?> deleteBudget(
            @PathVariable String budgetId,
            Authentication authentication) {

        try {
            Long budgetIdLong = Long.parseLong(budgetId);
            User user = getUser(authentication);
            budgetService.deleteBudget(budgetIdLong, user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/update-spending")
    public ResponseEntity<?> updateBudgetSpending(Authentication authentication) {
        try {
            User user = getUser(authentication);
            budgetService.updateBudgetSpending(user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}