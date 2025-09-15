package com.bank_api.transferService.controller;

import com.bank_api.transferService.dto.TransferDTO;
import com.bank_api.transferService.dto.TransferRequestDTO;
import com.bank_api.transferService.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    
    @Autowired
    private TransferService transferService;
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @PostMapping
    public ResponseEntity<TransferDTO> initiateTransfer(@RequestBody TransferRequestDTO transferRequest,
                                                       @RequestHeader("X-User-ID") Long userId) {
        TransferDTO transfer = transferService.initiateTransfer(transferRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(transfer);
    }
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @GetMapping("/{id}")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable Long id,
                                                      @RequestHeader("X-User-ID") Long userId) {
        TransferDTO transfer = transferService.getTransferById(id, userId);
        return ResponseEntity.ok(transfer);
    }
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @GetMapping
    public ResponseEntity<List<TransferDTO>> getUserTransfers(@RequestHeader("X-User-ID") Long userId) {
        List<TransferDTO> transfers = transferService.getUserTransfers(userId);
        return ResponseEntity.ok(transfers);
    }
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransferDTO>> getTransfersByAccount(@PathVariable Long accountId,
                                                                  @RequestHeader("X-User-ID") Long userId) {
        List<TransferDTO> transfers = transferService.getTransfersByAccount(accountId, userId);
        return ResponseEntity.ok(transfers);
    }
}