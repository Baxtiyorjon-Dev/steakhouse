package com.steakhouse.service;

import com.steakhouse.model.Discount;
import com.steakhouse.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountService {


    @Autowired
    private DiscountRepository discountRepository;

    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        List<Discount> discounts = discountRepository.findByStartDateBeforeAndEndDateAfter(LocalDateTime.now(), LocalDateTime.now());

        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (Discount discount : discounts) {
            if ("percentage".equalsIgnoreCase(discount.getDiscountType())) {
                totalDiscount = totalDiscount.add(subtotal.multiply(discount.getValue().divide(new BigDecimal(100))));
            } else if ("fixed".equalsIgnoreCase(discount.getDiscountType())) {
                totalDiscount = totalDiscount.add(discount.getValue());
            }
        }

        return totalDiscount;
    }

    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }
}
