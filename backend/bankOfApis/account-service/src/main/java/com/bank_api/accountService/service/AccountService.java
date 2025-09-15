package com.bank_api.accountService.service;

import com.bank_api.accountService.dto.AccountDTO;
import com.bank_api.accountService.dto.StatementDTO;
import com.bank_api.accountService.dto.TransactionDTO;

import java.util.List;

public interface AccountService {
    // REMOVE all token parameters, use userId directly
    List<AccountDTO> getAccountsByUserId(Long userId);
    AccountDTO getAccountById(Long id, Long userId); // Changed from String token
    List<TransactionDTO> getAccountTransactions(Long accountId, Long userId); // Changed
    List<TransactionDTO> getAccountStatements(Long accountId, String startDate, String endDate, Long userId); // Changed
    List<StatementDTO> getAccountStatementHistory(Long accountId, Long userId); // Changed
    StatementDTO generateStatement(Long accountId, String startDate, String endDate, Long userId); // Changed
    String downloadStatement(Long statementId, String format, Long userId); // Changed
    AccountDTO createAccount(AccountDTO accountDTO, Long userId); // Changed
    TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId); // Changed
    
    // REMOVE THIS METHOD - Gateway handles JWT now
    // Long extractUserIdFromToken(String token);
}