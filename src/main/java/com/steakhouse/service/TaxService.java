package com.steakhouse.service;

import com.steakhouse.exception.ResourceNotFoundException;
import com.steakhouse.model.Tax;
import com.steakhouse.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaxService {

    @Autowired
    private TaxRepository taxRepository;

//    public BigDecimal calculateTax(BigDecimal amount, String region) {
//        Tax tax = taxRepository.findByRegion(region)
//                .orElseThrow(() -> new ResourceNotFoundException("Tax rate not found for region " + region));
//
//        return amount.multiply(tax.getTaxRate().divide(new BigDecimal(100)));
//    }
}
