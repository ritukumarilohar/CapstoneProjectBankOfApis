package com.bank_api.cardService.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardTransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String description;
    private String merchantName;
    private LocalDateTime transactionDate;
    private String transactionType;
    private String status;
    private String category;
    private String maskedCardNumber;
    
    // Constructors
    public CardTransactionDTO() {}
    
    public CardTransactionDTO(Long id, BigDecimal amount, String description, 
                             String merchantName, LocalDateTime transactionDate,
                             String transactionType, String status, String category,
                             String maskedCardNumber) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.merchantName = merchantName;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.status = status;
        this.category = category;
        this.maskedCardNumber = maskedCardNumber;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.maskedCardNumber = merchantName; }
    
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getMaskedCardNumber() { return maskedCardNumber; }
    public void setMaskedCardNumber(String maskedCardNumber) { this.maskedCardNumber = maskedCardNumber; }
}