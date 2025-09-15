package com.bank_api.cardService.controller;

import com.bank_api.cardService.dto.CardDTO;
import com.bank_api.cardService.dto.CardTransactionDTO;
import com.bank_api.cardService.dto.PaymentRequestDTO;
import com.bank_api.cardService.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    
    @Autowired
    private CardService cardService;
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @GetMapping
    public ResponseEntity<List<CardDTO>> getUserCards(@RequestHeader("X-User-ID") Long userId) {
        List<CardDTO> cards = cardService.getUserCards(userId);
        return ResponseEntity.ok(cards);
    }
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long id,
                                              @RequestHeader("X-User-ID") Long userId) {
        CardDTO card = cardService.getCardById(id, userId);
        return ResponseEntity.ok(card);
    }
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<CardTransactionDTO>> getCardTransactions(@PathVariable Long id,
                                                                       @RequestHeader("X-User-ID") Long userId) {
        List<CardTransactionDTO> transactions = cardService.getCardTransactions(id, userId);
        return ResponseEntity.ok(transactions);
    }
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @GetMapping("/{id}/statements")
    public ResponseEntity<List<CardTransactionDTO>> getCardStatements(@PathVariable Long id,
                                                                     @RequestParam String startDate,
                                                                     @RequestParam String endDate,
                                                                     @RequestHeader("X-User-ID") Long userId) {
        List<CardTransactionDTO> statements = cardService.getCardStatements(id, startDate, endDate, userId);
        return ResponseEntity.ok(statements);
    }
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @PostMapping("/payments")
    public ResponseEntity<CardTransactionDTO> makePayment(@RequestBody PaymentRequestDTO paymentRequest,
                                                         @RequestHeader("X-User-ID") Long userId) {
        CardTransactionDTO payment = cardService.makePayment(paymentRequest, userId);
        return ResponseEntity.ok(payment);
    }
    
    // CHANGE: Use X-User-ID header instead of Authorization
    @PostMapping
    public ResponseEntity<CardDTO> addCard(@RequestBody CardDTO cardDTO,
                                          @RequestHeader("X-User-ID") Long userId) {
        CardDTO newCard = cardService.addCard(cardDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCard);
    }
}