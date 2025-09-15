package com.bank_api.cardService.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private LocalDate expiryDate;
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private BigDecimal outstandingAmount;
    private LocalDate dueDate;
    private BigDecimal minimumDue;
    private String status;
    private String bankName;
    private String maskedCardNumber;
    
    // Constructors
    public CardDTO() {}
    
    public CardDTO(Long id, String cardNumber, String cardHolderName, LocalDate expiryDate,
                  BigDecimal creditLimit, BigDecimal availableCredit, BigDecimal outstandingAmount,
                  LocalDate dueDate, BigDecimal minimumDue, String status, String bankName) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.creditLimit = creditLimit;
        this.availableCredit = availableCredit;
        this.outstandingAmount = outstandingAmount;
        this.dueDate = dueDate;
        this.minimumDue = minimumDue;
        this.status = status;
        this.bankName = bankName;
        this.maskedCardNumber = maskCardNumber(cardNumber);
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { 
        this.cardNumber = cardNumber;
        this.maskedCardNumber = maskCardNumber(cardNumber);
    }
    
    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    
    public BigDecimal getAvailableCredit() { return availableCredit; }
    public void setAvailableCredit(BigDecimal availableCredit) { this.availableCredit = availableCredit; }
    
    public BigDecimal getOutstandingAmount() { return outstandingAmount; }
    public void setOutstandingAmount(BigDecimal outstandingAmount) { this.outstandingAmount = outstandingAmount; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public BigDecimal getMinimumDue() { return minimumDue; }
    public void setMinimumDue(BigDecimal minimumDue) { this.minimumDue = minimumDue; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    
    public String getMaskedCardNumber() { return maskedCardNumber; }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 12) {
            return cardNumber;
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}