package com.wallet.service;

import com.wallet.dto.WalletResponse;
import com.wallet.entity.Wallet;
import com.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public WalletResponse getWalletByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
        
        return new WalletResponse(wallet.getBalance(), wallet.getCurrency());
    }

    public Wallet findByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
    }

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public void updateBalance(Long userId, java.math.BigDecimal amount) {
        Wallet wallet = findByUserId(userId);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }
}