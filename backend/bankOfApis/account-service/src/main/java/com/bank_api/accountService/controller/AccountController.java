package com.bank_api.accountService.controller;

import com.bank_api.accountService.dto.AccountDTO;
import com.bank_api.accountService.dto.StatementDTO;
import com.bank_api.accountService.dto.TransactionDTO;
import com.bank_api.accountService.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getUserAccounts(@RequestHeader("X-User-ID") Long userId) {
        List<AccountDTO> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }
    
    // CHANGE: Use X-User-ID header and remove token parameter
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountDetails(@PathVariable Long id, 
                                                      @RequestHeader("X-User-ID") Long userId) {
        AccountDTO account = accountService.getAccountById(id, userId);
        return ResponseEntity.ok(account);
    }
    
    // CHANGE: Use X-User-ID header and remove token parameter
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAccountTransactions(@PathVariable Long id,
                                                                     @RequestHeader("X-User-ID") Long userId) {
        List<TransactionDTO> transactions = accountService.getAccountTransactions(id, userId);
        return ResponseEntity.ok(transactions);
    }
    
    // CHANGE: Use X-User-ID header and remove token parameter
    @GetMapping("/{id}/statements")
    public ResponseEntity<List<TransactionDTO>> getAccountStatements(@PathVariable Long id,
                                                                    @RequestParam String startDate,
                                                                    @RequestParam String endDate,
                                                                    @RequestHeader("X-User-ID") Long userId) {
        List<TransactionDTO> statements = accountService.getAccountStatements(id, startDate, endDate, userId);
        return ResponseEntity.ok(statements);
    }
    
    // CHANGE: Use X-User-ID header and remove token parameter
    @GetMapping("/{id}/statement-history")
    public ResponseEntity<List<StatementDTO>> getAccountStatementHistory(@PathVariable Long id,
                                                                        @RequestHeader("X-User-ID") Long userId) {
        List<StatementDTO> statements = accountService.getAccountStatementHistory(id, userId);
        return ResponseEntity.ok(statements);
    }
    
    // CHANGE: Use X-User-ID header and remove token parameter
    @PostMapping("/{id}/generate-statement")
    public ResponseEntity<StatementDTO> generateStatement(@PathVariable Long id,
                                                         @RequestParam String startDate,
                                                         @RequestParam String endDate,
                                                         @RequestHeader("X-User-ID") Long userId) {
        StatementDTO statement = accountService.generateStatement(id, startDate, endDate, userId);
        return ResponseEntity.ok(statement);
    }
    
    // CHANGE: Use X-User-ID header and remove token parameter
    @GetMapping("/statements/{statementId}/download")
    public ResponseEntity<String> downloadStatement(@PathVariable Long statementId,
                                                   @RequestParam(defaultValue = "pdf") String format,
                                                   @RequestHeader("X-User-ID") Long userId) {
        String downloadUrl = accountService.downloadStatement(statementId, format, userId);
        return ResponseEntity.ok(downloadUrl);
    }
    
    // CHANGE: Use X-User-ID header and remove token parameter
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO,
                                                   @RequestHeader("X-User-ID") Long userId) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }
    
    // CHANGE: Use X-User-ID header and remove token parameter
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO,
                                                           @RequestHeader("X-User-ID") Long userId) {
        TransactionDTO createdTransaction = accountService.createTransaction(transactionDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
}