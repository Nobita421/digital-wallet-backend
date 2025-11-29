package com.wallet.service;

import com.wallet.dto.TransferRequest;
import com.wallet.entity.*;
import com.wallet.repository.TransferRepository;
import com.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    public Transfer createTransfer(TransferRequest request) {
        // Validate users exist
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Validate sender has sufficient balance
        Wallet senderWallet = walletService.findByUserId(request.getSenderId());
        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Create transfer record
        Transfer transfer = new Transfer(sender, receiver, request.getAmount(), 
                                       request.getCurrency(), request.getDescription());
        transfer = transferRepository.save(transfer);

        try {
            // Update sender wallet
            senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
            walletService.save(senderWallet);

            // Update receiver wallet
            Wallet receiverWallet = walletService.findByUserId(request.getReceiverId());
            receiverWallet.setBalance(receiverWallet.getBalance().add(request.getAmount()));
            walletService.save(receiverWallet);

            // Create transaction records
            transactionService.createTransaction(sender, TransactionType.SEND, 
                    request.getAmount(), request.getCurrency(), 
                    "Transfer to " + receiver.getName());

            transactionService.createTransaction(receiver, TransactionType.RECEIVE, 
                    request.getAmount(), request.getCurrency(), 
                    "Transfer from " + sender.getName());

            // Update transfer status
            transfer.setStatus(TransferStatus.COMPLETED);
            transfer = transferRepository.save(transfer);

        } catch (Exception e) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }

        return transfer;
    }
}