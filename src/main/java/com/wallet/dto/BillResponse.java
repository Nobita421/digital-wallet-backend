package com.wallet.dto;

import com.wallet.entity.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BillResponse {
    private Long id;
    private String name;
    private BigDecimal amount;
    private String currency;
    private LocalDate dueDate;
    private String category;
    private String description;
    private Boolean isRecurring;
    private String recurringPeriod;
    private BillStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BillResponse() {}

    // Constructor for mapping from entity
    public BillResponse(Long id, String name, BigDecimal amount, String currency, LocalDate dueDate,
                       String category, String description, Boolean isRecurring, String recurringPeriod,
                       BillStatus status, LocalDateTime paidAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.dueDate = dueDate;
        this.category = category;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurringPeriod = recurringPeriod;
        this.status = status;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }

    public String getRecurringPeriod() { return recurringPeriod; }
    public void setRecurringPeriod(String recurringPeriod) { this.recurringPeriod = recurringPeriod; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}