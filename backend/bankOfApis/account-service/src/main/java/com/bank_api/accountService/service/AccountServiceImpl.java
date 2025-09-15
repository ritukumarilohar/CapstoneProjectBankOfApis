package com.bank_api.accountService.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank_api.accountService.dto.AccountDTO;
import com.bank_api.accountService.dto.StatementDTO;
import com.bank_api.accountService.dto.TransactionDTO;
import com.bank_api.accountService.model.Account;
import com.bank_api.accountService.model.Statement;
import com.bank_api.accountService.model.Transaction;
import com.bank_api.accountService.repository.AccountRepository;
import com.bank_api.accountService.repository.StatementRepository;
import com.bank_api.accountService.repository.TransactionRepository;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private StatementRepository statementRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Override
    public List<AccountDTO> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findByCustomerId(userId);
        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AccountDTO getAccountById(Long id, Long userId) {
        Account account = accountRepository.findByIdAndCustomerId(id, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return convertToDTO(account);
    }
    
    @Override
    public List<TransactionDTO> getAccountTransactions(Long accountId, Long userId) {
        Account account = accountRepository.findByIdAndCustomerId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionDTO> getAccountStatements(Long accountId, String startDate, String endDate, Long userId) {
        Account account = accountRepository.findByIdAndCustomerId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
        
        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetween(accountId, start, end);
        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<StatementDTO> getAccountStatementHistory(Long accountId, Long userId) {
        Account account = accountRepository.findByIdAndCustomerId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        List<Statement> statements = statementRepository.findByAccountId(accountId);
        return statements.stream()
                .map(this::convertToStatementDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public StatementDTO generateStatement(Long accountId, String startDate, String endDate, Long userId) {
        Account account = accountRepository.findByIdAndCustomerId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        
        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetween(
            accountId, 
            start.atStartOfDay(), 
            end.atTime(23, 59, 59)
        );
        
        BigDecimal openingBalance = calculateOpeningBalance(accountId, start);
        BigDecimal totalCredits = calculateTotal(transactions, "CREDIT");
        BigDecimal totalDebits = calculateTotal(transactions, "DEBIT");
        BigDecimal closingBalance = openingBalance.add(totalCredits).subtract(totalDebits);
        
        String statementPeriod = start.getMonth().toString() + " " + start.getYear();
        Statement statement = new Statement(
            accountId, statementPeriod, start, end, 
            openingBalance, closingBalance, totalCredits, totalDebits, "GENERATED"
        );
        
        Statement savedStatement = statementRepository.save(statement);
        return convertToStatementDTO(savedStatement);
    }
    
    @Override
    public String downloadStatement(Long statementId, String format, Long userId) {
        Statement statement = statementRepository.findById(statementId)
                .orElseThrow(() -> new RuntimeException("Statement not found"));
        
        Account account = accountRepository.findByIdAndCustomerId(statement.getAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        return "/api/statements/" + statementId + "/download." + format.toLowerCase();
    }
    
//    @Override
//    public AccountDTO createAccount(AccountDTO accountDTO, Long userId) {
//        Account account = new Account();
//        account.setAccountNumber(accountDTO.getAccountNumber());
//        account.setAccountType(accountDTO.getAccountType());
//        account.setBalance(accountDTO.getBalance());
//        account.setStatus(accountDTO.getStatus());
//        account.setBanknName(accountDTO.getBankName());
//        account.setCustomerId(userId);
//        account.setBankId(1L);
//        
//        Account savedAccount = accountRepository.save(account);
//        return convertToDTO(savedAccount);
//    }
    @Override
    public AccountDTO createAccount(AccountDTO accountDTO, Long userId) {
        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        account.setStatus(accountDTO.getStatus());
        account.setBankName(accountDTO.getBankName());
        account.setCustomerId(userId);
        
        // Use a transaction to ensure thread safety
        Long nextBankId = getNextBankIdForUser(userId);
        account.setBankId(nextBankId);

        Account savedAccount = accountRepository.save(account);
        return convertToDTO(savedAccount);
    }

    @Transactional
    private Long getNextBankIdForUser(Long userId) {
        Long maxBankId = accountRepository.findMaxBankIdByCustomerId(userId);
        return maxBankId != null ? maxBankId + 1 : 1L;
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId) {
        Account account = accountRepository.findByIdAndCustomerId(
            transactionDTO.getAccountId(), userId)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setDate(LocalDateTime.now());
        transaction.setStatus("COMPLETED");
        transaction.setAccountId(transactionDTO.getAccountId());
        transaction.setToAccountId(transactionDTO.getToAccountId());
        transaction.setCustomerId(userId);
        transaction.setDescription(transactionDTO.getDescription());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        updateAccountBalance(account, transactionDTO);
        
        return convertToTransactionDTO(savedTransaction);
    }
    
    // ============ HELPER METHODS ============
    
    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        dto.setBankName(account.getBankName());
        return dto;
    }
    
    private TransactionDTO convertToTransactionDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setDate(transaction.getDate());
        dto.setStatus(transaction.getStatus());
        dto.setDescription(transaction.getDescription());
        
        dto.setAccountId(transaction.getAccountId());
        dto.setToAccountId(transaction.getToAccountId());
        
        dto.setAccountNumber("ACC" + transaction.getAccountId());
        if (transaction.getToAccountId() != null) {
            dto.setToAccountNumber("ACC" + transaction.getToAccountId());
        }
        
        return dto;
    }
    
    private StatementDTO convertToStatementDTO(Statement statement) {
        StatementDTO dto = new StatementDTO();
        dto.setId(statement.getId());
        dto.setStatementPeriod(statement.getStatementPeriod());
        dto.setStartDate(statement.getStartDate());
        dto.setEndDate(statement.getEndDate());
        dto.setOpeningBalance(statement.getOpeningBalance());
        dto.setClosingBalance(statement.getClosingBalance());
        dto.setTotalCredits(statement.getTotalCredits());
        dto.setTotalDebits(statement.getTotalDebits());
        dto.setStatus(statement.getStatus());
        
        dto.setAccountNumber("ACC" + statement.getAccountId());
        
        if ("GENERATED".equals(statement.getStatus()) && statement.getFilePath() != null) {
            dto.setDownloadUrl("/api/statements/" + statement.getId() + "/download");
        }
        
        return dto;
    }
    
    private BigDecimal calculateOpeningBalance(Long accountId, LocalDate startDate) {
        return BigDecimal.valueOf(1000.00);
    }

    private BigDecimal calculateTotal(List<Transaction> transactions, String type) {
        return transactions.stream()
                .filter(t -> type.equals(t.getTransactionType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private void updateAccountBalance(Account account, TransactionDTO transactionDTO) {
        BigDecimal newBalance = account.getBalance();
        
        switch (transactionDTO.getTransactionType()) {
            case "CREDIT":
                newBalance = newBalance.add(transactionDTO.getAmount());
                break;
            case "DEBIT":
            case "TRANSFER":
                newBalance = newBalance.subtract(transactionDTO.getAmount());
                break;
        }
        
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}