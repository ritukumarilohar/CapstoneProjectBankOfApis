package com.bank_api.cardService.dto;

import java.math.BigDecimal;

public class PaymentRequestDTO {
    private Long cardId;
    private BigDecimal amount;
    private Long fromAccountId;
    private String description;
    
    // Constructors
    public PaymentRequestDTO() {}
    
    public PaymentRequestDTO(Long cardId, BigDecimal amount, Long fromAccountId, String description) {
        this.cardId = cardId;
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.description = description;
    }
    
    // Getters and setters
    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}