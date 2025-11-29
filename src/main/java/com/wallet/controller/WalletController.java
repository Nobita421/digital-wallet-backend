package com.wallet.controller;

import com.wallet.dto.WalletResponse;
import com.wallet.service.WalletService;
import com.wallet.repository.UserRepository;
import com.wallet.entity.User;
import com.wallet.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        try {
            WalletResponse wallet = walletService.getWalletByUserId(user.getId());
            return ResponseEntity.ok(wallet);
        } catch (RuntimeException e) {
            // If wallet not found, create a new one for the user
            Wallet newWallet = new Wallet(user, BigDecimal.ZERO, "USD");
            walletService.save(newWallet);
            return ResponseEntity.ok(new WalletResponse(newWallet.getBalance(), newWallet.getCurrency()));
        }
    }
}