package com.drinks.BenGodwin.repository;

import com.drinks.BenGodwin.dto.MonthlyGain;
import com.drinks.BenGodwin.entity.Batch;
import com.drinks.BenGodwin.entity.Brand;
import com.drinks.BenGodwin.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s WHERE s.date BETWEEN :startOfMonth AND :endOfMonth")
    List<Sale> findByMonth(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT s FROM Sale s WHERE s.batch = :batch")
    List<Sale> findByBatch(@Param("batch") Batch batch);

    @Query("SELECT s FROM Sale s WHERE s.brand = :brand AND s.date BETWEEN :startOfMonth AND :endOfMonth")
    List<Sale> findByBrandAndMonth(@Param("brand") Brand brand, @Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth);

}
