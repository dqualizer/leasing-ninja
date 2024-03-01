package io.leasingninja.riskmanagement.domain;

public record ResaleValue(float value) {

    public ResaleValue {
        assert isValid(value);

    }
}
