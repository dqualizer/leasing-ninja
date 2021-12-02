package io.leasingninja.sales.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record LeaseTerm(int noOfMonths) {
    public static LeaseTerm ofMonths(int noOfMonths) {
        return new LeaseTerm(noOfMonths);
    }
}
