package com.drinks.BenGodwin.repository;

import com.drinks.BenGodwin.entity.Batch;
import com.drinks.BenGodwin.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findByRemainingQuantityLessThan(int quantity);

    Optional<Batch> findFirstByBrandOrderByCreatedAtAsc(Brand brand);

}
