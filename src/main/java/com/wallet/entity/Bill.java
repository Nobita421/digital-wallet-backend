package com.wallet.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    private String currency = "USD";

    @Column(name = "due_date")
    private LocalDate dueDate;

    private String category;
    private String description;

    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    @Column(name = "recurring_period")
    private String recurringPeriod; // MONTHLY, WEEKLY, YEARLY

    @Enumerated(EnumType.STRING)
    private BillStatus status = BillStatus.PENDING;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Bill() {}

    public Bill(User user, String name, BigDecimal amount, String currency, LocalDate dueDate,
                String category, String description, Boolean isRecurring, String recurringPeriod) {
        this.user = user;
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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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