package com.drinks.BenGodwin.repository;

import com.drinks.BenGodwin.entity.Customer;
import com.drinks.BenGodwin.entity.Transaction;
import com.drinks.BenGodwin.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);

}
