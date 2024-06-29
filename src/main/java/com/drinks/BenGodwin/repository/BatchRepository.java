package com.drinks.BenGodwin.repository;

import com.drinks.BenGodwin.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findByRemainingQuantityLessThan(int quantity);

}
