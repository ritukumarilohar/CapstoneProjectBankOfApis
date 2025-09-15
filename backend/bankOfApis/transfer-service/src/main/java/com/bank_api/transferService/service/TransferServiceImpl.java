package com.bank_api.transferService.service;

import com.bank_api.transferService.dto.TransferDTO;
import com.bank_api.transferService.dto.TransferRequestDTO;
import com.bank_api.transferService.model.Transfer;
import com.bank_api.transferService.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransferServiceImpl implements TransferService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);
    
    @Autowired
    private TransferRepository transferRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String ACCOUNT_SERVICE_URL = "http://account-service/api/accounts";
    
    @Override
    public TransferDTO initiateTransfer(TransferRequestDTO transferRequest, Long userId) {
        logger.info("Initiating transfer from account {} to account {} for amount {}", 
                   transferRequest.getFromAccountId(), transferRequest.getToAccountId(), transferRequest.getAmount());
        
        // 1. Validate accounts and balances
        validateTransfer(transferRequest, userId);
        
        // 2. Create transfer record
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(transferRequest.getFromAccountId());
        transfer.setToAccountId(transferRequest.getToAccountId());
        transfer.setAmount(transferRequest.getAmount());
        transfer.setDescription(transferRequest.getDescription());
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus("COMPLETED");
        transfer.setReferenceNumber(generateReferenceNumber());
        transfer.setCustomerId(userId);
        
        Transfer savedTransfer = transferRepository.save(transfer);
        
        // 3. Update account balances
        updateAccountBalances(transferRequest, userId);
        
        logger.info("Transfer completed successfully with reference: {}", savedTransfer.getReferenceNumber());
        
        return convertToDTO(savedTransfer);
    }
    
    private void validateTransfer(TransferRequestDTO transferRequest, Long userId) {
        // Check if fromAccount belongs to user and has sufficient balance
        ResponseEntity<AccountDTO> fromAccountResponse = restTemplate.exchange(
            ACCOUNT_SERVICE_URL + "/" + transferRequest.getFromAccountId(),
            HttpMethod.GET,
            createEntityWithUserId(userId),
            AccountDTO.class
        );
        
        if (!fromAccountResponse.getStatusCode().is2xxSuccessful() || fromAccountResponse.getBody() == null) {
            throw new RuntimeException("Source account not found or access denied");
        }
        
        AccountDTO fromAccount = fromAccountResponse.getBody();
        
        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance in source account");
        }
        
        // Check if toAccount exists
        try {
            ResponseEntity<AccountDTO> toAccountResponse = restTemplate.exchange(
                ACCOUNT_SERVICE_URL + "/" + transferRequest.getToAccountId(),
                HttpMethod.GET,
                createEntityWithUserId(userId),
                AccountDTO.class
            );
            
            if (!toAccountResponse.getStatusCode().is2xxSuccessful() || toAccountResponse.getBody() == null) {
                throw new RuntimeException("Destination account not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Destination account not found or inaccessible");
        }
    }
    
    private void updateAccountBalances(TransferRequestDTO transferRequest, Long userId) {
        try {
            // Debit from source account
            TransactionRequest debitTransaction = new TransactionRequest();
            debitTransaction.setAccountId(transferRequest.getFromAccountId());
            debitTransaction.setToAccountId(transferRequest.getToAccountId());
            debitTransaction.setAmount(transferRequest.getAmount());
            debitTransaction.setTransactionType("DEBIT");
            debitTransaction.setDescription("Transfer to account " + transferRequest.getToAccountId() + 
                                          (transferRequest.getDescription() != null ? " - " + transferRequest.getDescription() : ""));
            
            ResponseEntity<String> debitResponse = restTemplate.postForEntity(
                ACCOUNT_SERVICE_URL + "/transactions",
                createEntityWithBody(debitTransaction, userId),
                String.class
            );
            
            if (!debitResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to debit source account");
            }
            
            // Credit to destination account
            TransactionRequest creditTransaction = new TransactionRequest();
            creditTransaction.setAccountId(transferRequest.getToAccountId());
            creditTransaction.setToAccountId(transferRequest.getFromAccountId());
            creditTransaction.setAmount(transferRequest.getAmount());
            creditTransaction.setTransactionType("CREDIT");
            creditTransaction.setDescription("Transfer from account " + transferRequest.getFromAccountId() + 
                                           (transferRequest.getDescription() != null ? " - " + transferRequest.getDescription() : ""));
            
            ResponseEntity<String> creditResponse = restTemplate.postForEntity(
                ACCOUNT_SERVICE_URL + "/transactions",
                createEntityWithBody(creditTransaction, userId),
                String.class
            );
            
            if (!creditResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to credit destination account");
            }
            
            logger.info("Account balances updated successfully");
            
        } catch (Exception e) {
            logger.error("Failed to update account balances: {}", e.getMessage());
            // Mark transfer as failed if balance update fails
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }
    }
    
    private HttpEntity<?> createEntityWithUserId(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-ID", userId.toString());
        return new HttpEntity<>(headers);
    }
    
    private HttpEntity<?> createEntityWithBody(Object body, Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-ID", userId.toString());
        headers.set("Content-Type", "application/json");
        return new HttpEntity<>(body, headers);
    }
    
    @Override
    public TransferDTO getTransferById(Long id, Long userId) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));
        
        if (!transfer.getCustomerId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        
        return convertToDTO(transfer);
    }
    
    @Override
    public List<TransferDTO> getUserTransfers(Long userId) {
        List<Transfer> transfers = transferRepository.findByCustomerId(userId);
        return transfers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransferDTO> getTransfersByAccount(Long accountId, Long userId) {
        List<Transfer> outgoing = transferRepository.findByFromAccountId(accountId);
        List<Transfer> incoming = transferRepository.findByToAccountId(accountId);
        
        List<Transfer> allTransfers = outgoing;
        allTransfers.addAll(incoming);
        
        return allTransfers.stream()
                .filter(transfer -> transfer.getCustomerId().equals(userId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private String generateReferenceNumber() {
        return "TF" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    
    private TransferDTO convertToDTO(Transfer transfer) {
        TransferDTO dto = new TransferDTO();
        dto.setId(transfer.getId());
        dto.setFromAccountId(transfer.getFromAccountId());
        dto.setToAccountId(transfer.getToAccountId());
        dto.setAmount(transfer.getAmount());
        dto.setDescription(transfer.getDescription());
        dto.setTransferDate(transfer.getTransferDate());
        dto.setStatus(transfer.getStatus());
        dto.setReferenceNumber(transfer.getReferenceNumber());
        
        dto.setFromAccountNumber("ACC" + transfer.getFromAccountId());
        dto.setToAccountNumber("ACC" + transfer.getToAccountId());
        
        return dto;
    }
    
    // Internal DTO classes for service communication
    private static class AccountDTO {
        private Long id;
        private BigDecimal balance;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public BigDecimal getBalance() { return balance; }
        public void setBalance(BigDecimal balance) { this.balance = balance; }
    }
    
    private static class TransactionRequest {
        private Long accountId;
        private Long toAccountId;
        private BigDecimal amount;
        private String transactionType;
        private String description;
        
        public Long getAccountId() { return accountId; }
        public void setAccountId(Long accountId) { this.accountId = accountId; }
        public Long getToAccountId() { return toAccountId; }
        public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getTransactionType() { return transactionType; }
        public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}