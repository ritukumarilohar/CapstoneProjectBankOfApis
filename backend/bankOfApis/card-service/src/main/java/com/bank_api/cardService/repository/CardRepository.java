package com.bank_api.cardService.repository;

import com.bank_api.cardService.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findByCustomerId(Long customerId);
    Optional<CreditCard> findByIdAndCustomerId(Long id, Long customerId);
    List<CreditCard> findByStatus(String status);
}