package com.bank_api.accountService.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(name = "transaction_type")
    private String transactionType; // Credit/Debit/Transfer
    
    private LocalDateTime date;
    
    private String status;
    
    @Column(name = "account_id")
    private Long accountId;
    
    @Column(name = "to_account_id")
    private Long toAccountId;
    
    @Column(name = "customer_id") // ADD THIS FIELD
    private Long customerId;
    
    private String description;
    
    // Constructors
    public Transaction() {}
    
    public Transaction(BigDecimal amount, String transactionType, LocalDateTime date, 
                      String status, Long accountId, Long toAccountId, Long customerId, String description) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
        this.status = status;
        this.accountId = accountId;
        this.toAccountId = toAccountId;
        this.customerId = customerId; // ADD TO CONSTRUCTOR
        this.description = description;
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
    
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    
    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
    
    public Long getCustomerId() { return customerId; } // ADD GETTER
    public void setCustomerId(Long customerId) { this.customerId = customerId; } // ADD SETTER
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}