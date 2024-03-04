package io.leasingninja.sales.api;

public record RiskRequestDto(String number, String lessee, String car, Double buyingPriceCar, Double installment, String currency){}
