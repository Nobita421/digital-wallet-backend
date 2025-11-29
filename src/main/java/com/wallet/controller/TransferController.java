package com.wallet.controller;

import com.wallet.dto.TransferRequest;
import com.wallet.entity.Transfer;
import com.wallet.entity.User;
import com.wallet.repository.UserRepository;
import com.wallet.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createTransfer(
            Authentication authentication,
            @Valid @RequestBody TransferRequest request) {
        try {
            String email = authentication.getName();
            User sender = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Force sender ID to be the authenticated user
            request.setSenderId(sender.getId());
            
            Transfer transfer = transferService.createTransfer(request);
            return ResponseEntity.ok(transfer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    private static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}