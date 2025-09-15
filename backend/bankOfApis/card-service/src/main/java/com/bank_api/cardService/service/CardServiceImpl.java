package com.bank_api.cardService.service;

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

import com.bank_api.cardService.dto.CardDTO;
import com.bank_api.cardService.dto.CardTransactionDTO;
import com.bank_api.cardService.dto.PaymentRequestDTO;
import com.bank_api.cardService.model.CardTransaction;
import com.bank_api.cardService.model.CreditCard;
import com.bank_api.cardService.repository.CardRepository;
import com.bank_api.cardService.repository.CardTransactionRepository;

@Service
public class CardServiceImpl implements CardService {
    
    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private CardTransactionRepository cardTransactionRepository;
    
    // REMOVE JwtUtil autowired
    // @Autowired
    // private JwtUtil jwtUtil;
    
    @Override
    public List<CardDTO> getUserCards(Long userId) {
        // USE userId DIRECTLY FROM HEADER
        logger.info("Fetching cards for user ID: {}", userId);
        List<CreditCard> cards = cardRepository.findByCustomerId(userId);
        
        return cards.stream()
                .map(this::convertToCardDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CardDTO getCardById(Long id, Long userId) {
        // USE userId DIRECTLY FROM HEADER
        CreditCard card = cardRepository.findByIdAndCustomerId(id, userId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        return convertToCardDTO(card);
    }
    
    @Override
    public List<CardTransactionDTO> getCardTransactions(Long cardId, Long userId) {
        // USE userId DIRECTLY FROM HEADER
        // Verify the card belongs to the user
        CreditCard card = cardRepository.findByIdAndCustomerId(cardId, userId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        List<CardTransaction> transactions = cardTransactionRepository.findByCardId(cardId);
        
        return transactions.stream()
                .map(transaction -> convertToTransactionDTO(transaction, card.getCardNumber()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CardTransactionDTO> getCardStatements(Long cardId, String startDate, String endDate, Long userId) {
        // USE userId DIRECTLY FROM HEADER
        // Verify the card belongs to the user
        CreditCard card = cardRepository.findByIdAndCustomerId(cardId, userId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
        
        List<CardTransaction> transactions = cardTransactionRepository.findByCardIdAndTransactionDateBetween(cardId, start, end);
        
        return transactions.stream()
                .map(transaction -> convertToTransactionDTO(transaction, card.getCardNumber()))
                .collect(Collectors.toList());
    }
    
    @Override
    public CardTransactionDTO makePayment(PaymentRequestDTO paymentRequest, Long userId) {
        // USE userId DIRECTLY FROM HEADER
        logger.info("Processing payment for card ID: {}, amount: {}", paymentRequest.getCardId(), paymentRequest.getAmount());
        
        // Verify the card belongs to the user
        CreditCard card = cardRepository.findByIdAndCustomerId(paymentRequest.getCardId(), userId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        // Create payment transaction
        CardTransaction payment = new CardTransaction();
        payment.setCardId(paymentRequest.getCardId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setDescription(paymentRequest.getDescription() != null ? paymentRequest.getDescription() : "Credit Card Payment");
        payment.setMerchantName("BANK PAYMENT");
        payment.setTransactionType("PAYMENT");
        payment.setStatus("COMPLETED");
        payment.setCategory("PAYMENT");
        payment.setCustomerId(userId); // Use userId from header
        
        CardTransaction savedPayment = cardTransactionRepository.save(payment);
        
        // Update card outstanding amount
        BigDecimal newOutstanding = card.getOutstandingAmount().subtract(paymentRequest.getAmount());
        card.setOutstandingAmount(newOutstanding);
        cardRepository.save(card);
        
        logger.info("Payment processed successfully. New outstanding amount: {}", newOutstanding);
        
        return convertToTransactionDTO(savedPayment, card.getCardNumber());
    }
    
    @Override
    public CardDTO addCard(CardDTO cardDTO, Long userId) {
        // USE userId DIRECTLY FROM HEADER
        logger.info("Adding new card for user ID: {}", userId);
        
        // Create new credit card
        CreditCard card = new CreditCard();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardHolderName(cardDTO.getCardHolderName());
        card.setExpiryDate(cardDTO.getExpiryDate());
        card.setCvv("123"); // In real app, this should be encrypted
        card.setCreditLimit(cardDTO.getCreditLimit());
        card.setAvailableCredit(cardDTO.getCreditLimit());
        card.setOutstandingAmount(BigDecimal.ZERO);
        card.setDueDate(LocalDate.now().plusDays(30));
        card.setMinimumDue(BigDecimal.ZERO);
        card.setStatus("ACTIVE");
        card.setCustomerId(userId); // Use userId from header
        card.setBankId(1L);
        
        CreditCard savedCard = cardRepository.save(card);
        logger.info("Card added successfully with ID: {}", savedCard.getId());
        
        return convertToCardDTO(savedCard);
    }
    
    // REMOVE JWT-RELATED METHODS
    // public Long extractUserIdFromToken(String token) { ... }
    
    // KEEP HELPER METHODS
    private CardDTO convertToCardDTO(CreditCard card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setCardNumber(card.getCardNumber());
        dto.setCardHolderName(card.getCardHolderName());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setCreditLimit(card.getCreditLimit());
        dto.setAvailableCredit(card.getAvailableCredit());
        dto.setOutstandingAmount(card.getOutstandingAmount());
        dto.setDueDate(card.getDueDate());
        dto.setMinimumDue(card.getMinimumDue());
        dto.setStatus(card.getStatus());
        dto.setBankName("Bank " + card.getBankId());
        return dto;
    }
    
    private CardTransactionDTO convertToTransactionDTO(CardTransaction transaction, String cardNumber) {
        CardTransactionDTO dto = new CardTransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setMerchantName(transaction.getMerchantName());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setStatus(transaction.getStatus());
        dto.setCategory(transaction.getCategory());
        
        // Mask the card number
        if (cardNumber != null && cardNumber.length() >= 12) {
            dto.setMaskedCardNumber("****-****-****-" + cardNumber.substring(cardNumber.length() - 4));
        }
        
        return dto;
    }
}