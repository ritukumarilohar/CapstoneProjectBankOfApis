package com.bank_api.accountService.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "statements")
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    
    @Column(name = "statement_period")
    private String statementPeriod; 
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "opening_balance")
    private BigDecimal openingBalance;
    
    @Column(name = "closing_balance")
    private BigDecimal closingBalance;
    
    @Column(name = "total_credits")
    private BigDecimal totalCredits;
    
    @Column(name = "total_debits")
    private BigDecimal totalDebits;
    
    private String status; 
    
    @Column(name = "file_path")
    private String filePath; 
    
   
    public Statement() {}
    
    public Statement(Long accountId, String statementPeriod, LocalDate startDate, 
                    LocalDate endDate, BigDecimal openingBalance, BigDecimal closingBalance,
                    BigDecimal totalCredits, BigDecimal totalDebits, String status) {
        this.accountId = accountId;
        this.statementPeriod = statementPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
        this.totalCredits = totalCredits;
        this.totalDebits = totalDebits;
        this.status = status;
    }
    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    
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
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}