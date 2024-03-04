package io.leasingninja.sales.api;

public record SalesApiResponseDto(String number, String lessee, String car, int leaseTerms, double installation, String voteResult) {
}
