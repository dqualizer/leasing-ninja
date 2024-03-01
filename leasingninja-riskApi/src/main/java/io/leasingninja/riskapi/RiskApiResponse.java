package io.leasingninja.riskapi;

import io.leasingninja.riskmanagement.domain.VoteResult;

public class RiskApiResponse {

    private int contractNumber;
    private String voteResult;

    public RiskApiResponse(int contractNumber, String voteResult) {
        this.contractNumber = contractNumber;
        this.voteResult = voteResult;
    }

    public int getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }


}
