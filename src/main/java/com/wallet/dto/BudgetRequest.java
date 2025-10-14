package com.wallet.dto;

import com.wallet.entity.BudgetPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetRequest {
    @NotBlank(message = "Budget name is required")
    private String name;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String currency = "USD";
    private String category;

    @NotNull(message = "Period is required")
    private BudgetPeriod period = BudgetPeriod.MONTHLY;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    // Constructors
    public BudgetRequest() {}

    public BudgetRequest(String name, BigDecimal amount, String currency, String category,
                        BudgetPeriod period, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and setters
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
}