package com.bank_api.cardService.service;

import com.bank_api.cardService.dto.CardDTO;
import com.bank_api.cardService.dto.CardTransactionDTO;
import com.bank_api.cardService.dto.PaymentRequestDTO;

import java.util.List;

public interface CardService {
    // CHANGE: Remove token parameters, use userId directly
    List<CardDTO> getUserCards(Long userId);
    CardDTO getCardById(Long id, Long userId);
    List<CardTransactionDTO> getCardTransactions(Long cardId, Long userId);
    List<CardTransactionDTO> getCardStatements(Long cardId, String startDate, String endDate, Long userId);
    CardTransactionDTO makePayment(PaymentRequestDTO paymentRequest, Long userId);
    CardDTO addCard(CardDTO cardDTO, Long userId);
    
    // REMOVE THIS METHOD - Gateway handles JWT now
    // Long extractUserIdFromToken(String token);
}