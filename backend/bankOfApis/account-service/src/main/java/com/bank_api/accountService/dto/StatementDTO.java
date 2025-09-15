package com.bank_api.accountService.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StatementDTO {
    private Long id;
    private String statementPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private String status;
    private String downloadUrl;
    private String accountNumber;
    
    // Constructors
    public StatementDTO() {}
    
    public StatementDTO(Long id, String statementPeriod, LocalDate startDate, 
                       LocalDate endDate, BigDecimal openingBalance, BigDecimal closingBalance,
                       BigDecimal totalCredits, BigDecimal totalDebits, String status, 
                       String downloadUrl, String accountNumber) {
        this.id = id;
        this.statementPeriod = statementPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
        this.totalCredits = totalCredits;
        this.totalDebits = totalDebits;
        this.status = status;
        this.downloadUrl = downloadUrl;
        this.accountNumber = accountNumber;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getStatementPeriod() { return statementPeriod; }
    public void setStatementPeriod(String statementPeriod) { this.statementPeriod = statementPeriod; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }
    
    public BigDecimal getClosingBalance() { return closingBalance; }
    public void setClosingBalance(BigDecimal closingBalance) { this.closingBalance = closingBalance; }
    
    public BigDecimal getTotalCredits() { return totalCredits; }
    public void setTotalCredits(BigDecimal totalCredits) { this.totalCredits = totalCredits; }
    
    public BigDecimal getTotalDebits() { return totalDebits; }
    public void setTotalDebits(BigDecimal totalDebits) { this.totalDebits = totalDebits; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
}