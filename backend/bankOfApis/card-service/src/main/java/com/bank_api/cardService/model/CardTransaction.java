package com.bank_api.cardService.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "card_transactions")
public class CardTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "card_id", nullable = false)
    private Long cardId;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    private String description;
    
    @Column(name = "merchant_name")
    private String merchantName;
    
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    
    @Column(name = "transaction_type")
    private String transactionType; // PURCHASE, PAYMENT, REFUND, CASH_ADVANCE
    
    private String status; // PENDING, COMPLETED, FAILED
    
    @Column(name = "category")
    private String category; // GROCERY, SHOPPING, TRAVEL, etc.
    
    @Column(name = "customer_id")
    private Long customerId;
    
    // Constructors
    public CardTransaction() {}
    
    public CardTransaction(Long cardId, BigDecimal amount, String description, 
                          String merchantName, String transactionType, String status,
                          String category, Long customerId) {
        this.cardId = cardId;
        this.amount = amount;
        this.description = description;
        this.merchantName = merchantName;
        this.transactionDate = LocalDateTime.now();
        this.transactionType = transactionType;
        this.status = status;
        this.category = category;
        this.customerId = customerId;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}