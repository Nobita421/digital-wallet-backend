package com.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BillRequest {
    @NotBlank(message = "Bill name is required")
    private String name;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String currency = "USD";

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private String category;
    private String description;
    private Boolean isRecurring = false;
    private String recurringPeriod;

    // Constructors
    public BillRequest() {}

    public BillRequest(String name, BigDecimal amount, String currency, LocalDate dueDate,
                      String category, String description, Boolean isRecurring, String recurringPeriod) {
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.dueDate = dueDate;
        this.category = category;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurringPeriod = recurringPeriod;
    }

    // Getters and setters
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
}