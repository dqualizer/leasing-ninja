package io.leasingninja.sales.api;

public record ContractRequestDto(String number, String lessee, String car, Double buyingPriceCar, String currency, int leaseTerms){}
