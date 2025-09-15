package com.bank_api.transferService.service;

import com.bank_api.transferService.dto.TransferDTO;
import com.bank_api.transferService.dto.TransferRequestDTO;

import java.util.List;

public interface TransferService {
    // CHANGE: Remove token parameters, use userId directly
    TransferDTO initiateTransfer(TransferRequestDTO transferRequest, Long userId);
    TransferDTO getTransferById(Long id, Long userId);
    List<TransferDTO> getUserTransfers(Long userId);
    List<TransferDTO> getTransfersByAccount(Long accountId, Long userId);
    
    // REMOVE THIS METHOD - Gateway handles JWT now
    // Long extractUserIdFromToken(String token);
}