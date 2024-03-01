package io.leasingninja.riskapi;

import io.leasingninja.riskmanagement.domain.VoteResult;

public record RiskApiResponse(int contractNumber, String voteResult) {

}
