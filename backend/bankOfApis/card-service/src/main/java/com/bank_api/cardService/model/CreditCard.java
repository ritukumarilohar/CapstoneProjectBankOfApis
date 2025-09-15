package com.bank_api.cardService.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_cards")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "card_number", nullable = false)
    private String cardNumber;
    
    @Column(name = "card_holder_name")
    private String cardHolderName;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "cvv")
    private String cvv; // Should be encrypted in production
    
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    
    @Column(name = "available_credit")
    private BigDecimal availableCredit;
    
    @Column(name = "outstanding_amount")
    private BigDecimal outstandingAmount;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "minimum_due")
    private BigDecimal minimumDue;
    
    private String status; // ACTIVE, BLOCKED, EXPIRED
    
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "bank_id")
    private Long bankId;
    
    // Constructors
    public CreditCard() {}
    
    public CreditCard(String cardNumber, String cardHolderName, LocalDate expiryDate, 
                     String cvv, BigDecimal creditLimit, BigDecimal availableCredit,
                     BigDecimal outstandingAmount, LocalDate dueDate, BigDecimal minimumDue,
                     String status, Long customerId, Long bankId) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.creditLimit = creditLimit;
        this.availableCredit = availableCredit;
        this.outstandingAmount = outstandingAmount;
        this.dueDate = dueDate;
        this.minimumDue = minimumDue;
        this.status = status;
        this.customerId = customerId;
        this.bankId = bankId;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    
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
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }
}