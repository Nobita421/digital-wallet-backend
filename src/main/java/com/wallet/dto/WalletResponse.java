package com.wallet.dto;

import java.math.BigDecimal;

public class WalletResponse {
    private BigDecimal balance;
    private String currency;

    public WalletResponse() {}

    public WalletResponse(BigDecimal balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}