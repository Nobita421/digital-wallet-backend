package com.wallet.service;

import com.wallet.dto.BudgetRequest;
import com.wallet.dto.BudgetResponse;
import com.wallet.entity.Budget;
import com.wallet.entity.BudgetPeriod;
import com.wallet.entity.Transaction;
import com.wallet.entity.TransactionType;
import com.wallet.entity.User;
import com.wallet.repository.BudgetRepository;
import com.wallet.repository.TransactionRepository;
import com.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Page<BudgetResponse> getBudgetsByUserId(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Budget> budgets = budgetRepository.findByUser(user, pageable);

        return budgets.map(this::convertToResponse);
    }

    public BudgetResponse createBudget(Long userId, BudgetRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setName(request.getName());
        budget.setAmount(request.getAmount());
        budget.setCurrency(request.getCurrency());
        budget.setCategory(request.getCategory());
        budget.setPeriod(request.getPeriod());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate() != null ? request.getEndDate() : calculateEndDate(request.getStartDate(), request.getPeriod()));
        budget.setSpentAmount(BigDecimal.ZERO);
        budget.setIsActive(true);

        Budget savedBudget = budgetRepository.save(budget);
        return convertToResponse(savedBudget);
    }

    public BudgetResponse updateBudget(Long budgetId, Long userId, BudgetRequest request) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("Budget does not belong to user");
        }

        budget.setName(request.getName());
        budget.setAmount(request.getAmount());
        budget.setCurrency(request.getCurrency());
        budget.setCategory(request.getCategory());
        budget.setPeriod(request.getPeriod());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate() != null ? request.getEndDate() : calculateEndDate(request.getStartDate(), request.getPeriod()));

        Budget savedBudget = budgetRepository.save(budget);
        return convertToResponse(savedBudget);
    }

    public void deleteBudget(Long budgetId, Long userId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("Budget does not belong to user");
        }

        budgetRepository.delete(budget);
    }

    @Transactional
    public void updateBudgetSpending(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Budget> activeBudgets = budgetRepository.findActiveBudgetsForDate(user, LocalDate.now());

        for (Budget budget : activeBudgets) {
            // Calculate spending for this budget's category in the current period
            BigDecimal spent = calculateSpentAmount(user, budget.getCategory(), budget.getStartDate(), budget.getEndDate());
            budget.setSpentAmount(spent);
            budgetRepository.save(budget);
        }
    }

    private BigDecimal calculateSpentAmount(User user, String category, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByUserAndTypeAndCreatedAtBetween(
            user, TransactionType.SEND, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        return transactions.stream()
                .filter(t -> category == null || category.equals(t.getDescription()) ||
                           (t.getDescription() != null && t.getDescription().contains(category)))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LocalDate calculateEndDate(LocalDate startDate, BudgetPeriod period) {
        return switch (period) {
            case DAILY -> startDate.plusDays(1);
            case WEEKLY -> startDate.plusWeeks(1);
            case MONTHLY -> startDate.plusMonths(1);
            case QUARTERLY -> startDate.plusMonths(3);
            case YEARLY -> startDate.plusYears(1);
        };
    }

    private BudgetResponse convertToResponse(Budget budget) {
        return new BudgetResponse(
            budget.getId(),
            budget.getName(),
            budget.getAmount(),
            budget.getCurrency(),
            budget.getCategory(),
            budget.getPeriod(),
            budget.getStartDate(),
            budget.getEndDate(),
            budget.getSpentAmount(),
            budget.getIsActive(),
            budget.getCreatedAt(),
            budget.getUpdatedAt()
        );
    }
}