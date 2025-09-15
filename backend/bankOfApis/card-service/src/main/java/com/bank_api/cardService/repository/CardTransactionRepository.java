package com.bank_api.cardService.repository;

import com.bank_api.cardService.model.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {
    List<CardTransaction> findByCardId(Long cardId);
    List<CardTransaction> findByCardIdAndTransactionDateBetween(Long cardId, LocalDateTime startDate, LocalDateTime endDate);
    List<CardTransaction> findByCustomerId(Long customerId);
}