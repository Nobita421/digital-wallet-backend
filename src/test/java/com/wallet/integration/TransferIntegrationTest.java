package com.wallet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.TransferRequest;
import com.wallet.entity.User;
import com.wallet.entity.Wallet;
import com.wallet.repository.UserRepository;
import com.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class TransferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        // Create test users
        sender = new User("sender@test.com", "Sender User", "+1234567890", "password123");
        sender = userRepository.save(sender);

        receiver = new User("receiver@test.com", "Receiver User", "+0987654321", "password123");
        receiver = userRepository.save(receiver);

        // Create wallets
        Wallet senderWallet = new Wallet(sender, new BigDecimal("1000.00"), "USD");
        walletRepository.save(senderWallet);

        Wallet receiverWallet = new Wallet(receiver, new BigDecimal("500.00"), "USD");
        walletRepository.save(receiverWallet);
    }

    @Test
    void testSuccessfulTransfer() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setSenderId(sender.getId());
        request.setReceiverId(receiver.getId());
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("USD");
        request.setDescription("Test transfer");

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void testInsufficientFundsTransfer() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setSenderId(sender.getId());
        request.setReceiverId(receiver.getId());
        request.setAmount(new BigDecimal("2000.00")); // More than sender's balance
        request.setCurrency("USD");
        request.setDescription("Test transfer");

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Insufficient funds"));
    }
}