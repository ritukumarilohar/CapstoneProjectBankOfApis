package com.bank_api.transferService.repository;

import com.bank_api.transferService.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByCustomerId(Long customerId);
    List<Transfer> findByFromAccountId(Long fromAccountId);
    List<Transfer> findByToAccountId(Long toAccountId);
    List<Transfer> findByStatus(String status);
}