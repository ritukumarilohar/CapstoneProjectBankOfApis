package com.bank_api.accountService.repository;

import com.bank_api.accountService.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findByAccountId(Long accountId);
    List<Statement> findByAccountIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
        Long accountId, LocalDate startDate, LocalDate endDate);
    Optional<Statement> findByAccountIdAndStatementPeriod(Long accountId, String statementPeriod);
    List<Statement> findByAccountIdAndStatus(Long accountId, String status);
}