package com.bank_api.accountService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank_api.accountService.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
    Optional<Account> findByIdAndCustomerId(Long id, Long customerId);
    Optional<Account> findByAccountNumber(String accountNumber);
    @Query("SELECT MAX(a.bankId) FROM Account a WHERE a.customerId = :customerId")
    Long findMaxBankIdByCustomerId(@Param("customerId") Long customerId);
}