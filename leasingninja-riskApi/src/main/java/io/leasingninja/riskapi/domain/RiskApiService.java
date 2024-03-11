package io.leasingninja.riskapi.domain;

import io.leasingninja.riskapi.domain.RiskRequest;
import io.leasingninja.riskmanagement.domain.CreditRating;
import io.leasingninja.riskmanagement.domain.VoteResult;
import io.leasingninja.sales.domain.Amount;
import io.leasingninja.sales.domain.Car;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* All calculations are based on fantasy and do not depict real domain knowledge
* */
@Service
public class RiskApiService {
    public VoteResult calculateVoteResult(RiskRequest riskRequest) {

        CreditRating creditRating = calculateCreditRating(riskRequest.getInstallment(), riskRequest.getCar());
        Amount resaleValue = calculateResaleValue(riskRequest.getBuyingPriceCar(), riskRequest.getCar());

        if (creditRating.value() >= 7 && resaleValue.amount() <= 10000) {
            return VoteResult.ACCEPTED;
        } else if (creditRating.value() >= 5 && creditRating.value() < 7 && resaleValue.amount() <= 20000) {
            return VoteResult.ACCEPTED_WITH_OBLIGATIONS;
        } else {
            return VoteResult.REJECTED;
        }

    }

    private CreditRating calculateCreditRating(Amount installmentRate, Car car) {

        double rating = (installmentRate.amount() / 1000) * car.toString().length();
        rating = Math.max(1, Math.min(10, rating));
        int roundedRating = (int) Math.round(rating);

        return CreditRating.of(roundedRating);
    }

    // would make sense to additionally consider the amount of lease terms
    private Amount calculateResaleValue(Amount price, Car car){

        List<String> expensiveCars = List.of("mercedes", "lexus", "jaguar", "ferrari", "maserati");
        List<String> inExpensiveCars = List.of("toyota", "fiat", "dacia", "opel", "seat");

        double multiplier = 1.5;
        if (expensiveCars.stream().anyMatch(car.toString()::contains)){
            multiplier = 3;
        } else if (inExpensiveCars.stream().anyMatch(car.toString()::contains)){
            multiplier = 1.25;
        }

        Amount resaleValue = Amount.of(car.toString().length() * multiplier, price.currency());

        return resaleValue;
    }





}
