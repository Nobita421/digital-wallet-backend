package com.wallet.dto;

import com.wallet.entity.BudgetPeriod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BudgetResponse {
    private Long id;
    private String name;
    private BigDecimal amount;
    private String currency;
    private String category;
    private BudgetPeriod period;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal spentAmount;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Computed fields
    private BigDecimal remainingAmount;
    private double spentPercentage;

    public BudgetResponse() {}

    // Constructor for mapping from entity
    public BudgetResponse(Long id, String name, BigDecimal amount, String currency, String category,
                         BudgetPeriod period, LocalDate startDate, LocalDate endDate, BigDecimal spentAmount,
                         Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
        this.spentAmount = spentAmount;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.remainingAmount = amount.subtract(spentAmount);
        this.spentPercentage = amount.compareTo(BigDecimal.ZERO) == 0 ? 0.0 :
            spentAmount.divide(amount, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BudgetPeriod getPeriod() { return period; }
    public void setPeriod(BudgetPeriod period) { this.period = period; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getSpentAmount() { return spentAmount; }
    public void setSpentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }

    public double getSpentPercentage() { return spentPercentage; }
    public void setSpentPercentage(double spentPercentage) { this.spentPercentage = spentPercentage; }
}