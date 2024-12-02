package com.steakhouse.repository;

import com.steakhouse.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStartDateBeforeAndEndDateAfter(LocalDateTime now1, LocalDateTime now2);
}
