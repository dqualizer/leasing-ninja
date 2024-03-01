package io.leasingninja.riskapi;

import io.leasingninja.riskmanagement.domain.CreditRating;
import io.leasingninja.riskmanagement.domain.VoteResult;
import io.leasingninja.sales.domain.Amount;
import io.leasingninja.sales.domain.Car;
import io.leasingninja.sales.domain.Contract;

import java.util.List;

public class RiskApiService {
    public VoteResult calculateVoteResult(Contract contract) {

        CreditRating creditRating = calculateCreditRating(contract.price(), contract.car());
        Amount resaleValue = calculateResaleValue(contract.price(), contract.car());

        if (creditRating.value() >= 7 && resaleValue.amount() <= 10000) {
            return VoteResult.ACCEPTED;
        } else if (creditRating.value() >= 5 && creditRating.value() < 7 && resaleValue.amount() <= 20000) {
            return VoteResult.ACCEPTED_WITH_OBLIGATIONS;
        } else {
            return VoteResult.REJECTED;
        }

    }

    private CreditRating calculateCreditRating(Amount price, Car car) {

        double rating = (price.amount() / 10000) * car.toString().length();
        rating = Math.max(1, Math.min(10, rating));
        int roundedRating = (int) Math.round(rating);

        return CreditRating.of(roundedRating);
    }

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
