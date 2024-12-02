package com.steakhouse;

import com.steakhouse.model.Discount;
import com.steakhouse.repository.DiscountRepository;
import com.steakhouse.service.DiscountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.math.RoundingMode;
class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountService discountService;

    DiscountServiceTest() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void calculateDiscount() {
        Discount discount = new Discount();
        discount.setDiscountType("percentage");
        discount.setValue(new BigDecimal("10")); // 10%

        when(discountRepository.findByStartDateBeforeAndEndDateAfter(any(), any())).thenReturn(List.of(discount));

        BigDecimal subtotal = new BigDecimal("100.00");
        BigDecimal totalDiscount = discountService.calculateDiscount(subtotal);

        // Set the scale to 2 decimal places to match expected precision
        assertEquals(new BigDecimal("10.00").setScale(2, RoundingMode.HALF_UP), totalDiscount.setScale(2, RoundingMode.HALF_UP));
    }

}
