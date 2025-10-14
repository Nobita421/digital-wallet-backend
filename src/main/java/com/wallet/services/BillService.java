package com.wallet.service;

import com.wallet.dto.BillRequest;
import com.wallet.dto.BillResponse;
import com.wallet.entity.Bill;
import com.wallet.entity.BillStatus;
import com.wallet.entity.User;
import com.wallet.repository.BillRepository;
import com.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    public Page<BillResponse> getBillsByUserId(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> bills = billRepository.findByUser(user, pageable);

        return bills.map(this::convertToResponse);
    }

    public BillResponse createBill(Long userId, BillRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bill bill = new Bill();
        bill.setUser(user);
        bill.setName(request.getName());
        bill.setAmount(request.getAmount());
        bill.setCurrency(request.getCurrency());
        bill.setDueDate(request.getDueDate());
        bill.setCategory(request.getCategory());
        bill.setDescription(request.getDescription());
        bill.setIsRecurring(request.getIsRecurring());
        bill.setRecurringPeriod(request.getRecurringPeriod());
        bill.setStatus(BillStatus.PENDING);

        Bill savedBill = billRepository.save(bill);
        return convertToResponse(savedBill);
    }

    @Transactional
    public BillResponse payBill(Long billId, Long userId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (!bill.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bill does not belong to user");
        }

        if (bill.getStatus() == BillStatus.PAID) {
            throw new RuntimeException("Bill is already paid");
        }

        // Check if user has sufficient balance
        var wallet = walletService.getWalletByUserId(userId);
        if (wallet.getBalance().compareTo(bill.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct from wallet
        walletService.updateBalance(userId, bill.getAmount().negate());

        // Create transaction record
        transactionService.createTransaction(
            bill.getUser(),
            com.wallet.entity.TransactionType.BILL_PAYMENT,
            bill.getAmount(),
            bill.getCurrency(),
            "Payment for " + bill.getName()
        );

        // Update bill status
        bill.setStatus(BillStatus.PAID);
        bill.setPaidAt(LocalDateTime.now());
        Bill savedBill = billRepository.save(bill);

        return convertToResponse(savedBill);
    }

    public List<Bill> getOverdueBills(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return billRepository.findByUserAndDueDateBeforeAndStatus(
            user, LocalDate.now(), BillStatus.PENDING);
    }

    public List<Bill> getUpcomingBills(Long userId, int daysAhead) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(daysAhead);

        return billRepository.findByUserAndDueDateBetween(user, startDate, endDate);
    }

    private BillResponse convertToResponse(Bill bill) {
        return new BillResponse(
            bill.getId(),
            bill.getName(),
            bill.getAmount(),
            bill.getCurrency(),
            bill.getDueDate(),
            bill.getCategory(),
            bill.getDescription(),
            bill.getIsRecurring(),
            bill.getRecurringPeriod(),
            bill.getStatus(),
            bill.getPaidAt(),
            bill.getCreatedAt(),
            bill.getUpdatedAt()
        );
    }
}