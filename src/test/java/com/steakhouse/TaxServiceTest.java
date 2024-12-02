package com.steakhouse;

import com.steakhouse.exception.ResourceNotFoundException;
import com.steakhouse.model.Tax;
import com.steakhouse.repository.TaxRepository;
import com.steakhouse.service.TaxService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaxServiceTest {

    @Mock
    private TaxRepository taxRepository;

    @InjectMocks
    private TaxService taxService;

    TaxServiceTest() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void calculateTax_RegionFound_ReturnsTaxAmount() {
        Tax tax = new Tax();
        tax.setTaxRate(new BigDecimal("10")); // 10%
        when(taxRepository.findByRegion(anyString())).thenReturn(Optional.of(tax));

        BigDecimal amount = new BigDecimal("100");
        BigDecimal expectedTax = new BigDecimal("10.00");
        BigDecimal actualTax = taxService.calculateTax(amount, "Region");

        assertEquals(expectedTax.setScale(2, RoundingMode.HALF_UP), actualTax.setScale(2, RoundingMode.HALF_UP));
    }


    @Test
    void calculateTax_RegionNotFound_ThrowsException() {
        when(taxRepository.findByRegion(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taxService.calculateTax(new BigDecimal("100"), "Unknown"));
    }
}
