package io.leasingninja.riskapi.domain;

import io.leasingninja.sales.domain.Amount;
import io.leasingninja.sales.domain.Car;
import io.leasingninja.sales.domain.ContractNumber;
import io.leasingninja.sales.domain.Customer;

public class RiskRequest {

    private final ContractNumber number;

    private final Customer lessee;
    private final Car car;
    private final Amount buyingPriceCar;

    private final Amount installment;

    public RiskRequest(ContractNumber number, Customer lessee, Car car, Amount buyingPriceCar, Amount installment) {
        this.number = number;
        this.lessee = lessee;
        this.car = car;
        this.buyingPriceCar = buyingPriceCar;
        this.installment = installment;
    }

    public ContractNumber getNumber() {
        return number;
    }

    public Customer getLessee() {
        return lessee;
    }

    public Car getCar() {
        return car;
    }

    public Amount getBuyingPriceCar() {
        return buyingPriceCar;
    }

    public Amount getInstallment() {
        return installment;
    }
}
