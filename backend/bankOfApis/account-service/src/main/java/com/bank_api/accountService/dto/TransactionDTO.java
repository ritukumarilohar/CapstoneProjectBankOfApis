package com.bank_api.accountService.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime date;
    private String status;
    private String description;
    private String accountNumber;
    private String toAccountNumber;
    private Long accountId;
    private Long toAccountId; // Add this field
    
    // Constructors
    public TransactionDTO() {}
    
    public TransactionDTO(Long id, BigDecimal amount, String transactionType, 
                         LocalDateTime date, String status, String description, 
                         String accountNumber, String toAccountNumber, Long accountId, Long toAccountId) {
        this.id = id;
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
        this.status = status;
        this.description = description;
        this.accountNumber = accountNumber;
        this.toAccountNumber = toAccountNumber;
        this.accountId = accountId;
        this.toAccountId = toAccountId;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getToAccountNumber() { return toAccountNumber; }
    public void setToAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; }
    
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    
    public Long getToAccountId() { return toAccountId; } // Add this getter
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; } // Add this setter
}