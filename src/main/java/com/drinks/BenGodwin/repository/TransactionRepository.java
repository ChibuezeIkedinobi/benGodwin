package com.drinks.BenGodwin.repository;

import com.drinks.BenGodwin.entity.Customers;
import com.drinks.BenGodwin.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDate(LocalDate date);

    List<Transaction> findByCustomersAndDateBetween(Customers customers, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByCustomers(Customers customers);

}
