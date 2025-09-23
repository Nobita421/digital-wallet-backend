package com.wallet.controller;

import com.wallet.dto.TransactionResponse;
import com.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTransactions(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            Page<TransactionResponse> transactions = transactionService.getTransactionsByUserId(userId, page, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("transactions", transactions.getContent());
            
            Map<String, Object> pagination = new HashMap<>();
            pagination.put("total", transactions.getTotalElements());
            pagination.put("limit", limit);
            pagination.put("offset", page * limit);
            pagination.put("hasMore", transactions.hasNext());
            response.put("pagination", pagination);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}