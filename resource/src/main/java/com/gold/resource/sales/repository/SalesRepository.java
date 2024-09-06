package com.gold.resource.sales.repository;

import com.gold.resource.sales.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByUserId(Long userId);
}
