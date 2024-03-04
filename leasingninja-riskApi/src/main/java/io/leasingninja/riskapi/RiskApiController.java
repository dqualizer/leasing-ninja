package io.leasingninja.riskapi;

import io.leasingninja.riskmanagement.domain.VoteResult;
import io.leasingninja.sales.api.RiskApiResponseDto;
import io.leasingninja.sales.api.RiskRequestDto;
import io.leasingninja.sales.domain.Amount;
import io.leasingninja.sales.domain.Car;
import io.leasingninja.sales.domain.ContractNumber;
import io.leasingninja.sales.domain.Currency;
import io.leasingninja.sales.domain.Customer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RiskApiController {

    private RiskApiService riskApiService;

    public RiskApiController(RiskApiService riskApiService) {
        this.riskApiService = riskApiService;
    }

    @PostMapping(value = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RiskApiResponse voteContractAutomated(@RequestBody ContractModel contractDto){

        System.out.println("test test test");
        RiskRequest riskRequest = riskRequestDtoToRiskRequest(riskRequestDto);
        VoteResult isContractVoted = riskApiService.calculateVoteResult(riskRequest);

        RiskApiResponse votedResultResponse = new RiskApiResponse(contract.getNumber().toInt(), isContractVoted.toString());

        return votedResultResponse;

    }



    private RiskRequest riskRequestDtoToRiskRequest(RiskRequestDto riskRequestDto){

        ContractNumber contractNumber = ContractNumber.of(riskRequestDto.number());
        Customer customer = Customer.of(riskRequestDto.lessee());
        Car car = Car.of(riskRequestDto.car());
        Amount price = Amount.of(riskRequestDto
            .buyingPriceCar(), Currency.valueOf(riskRequestDto.currency()));
        Amount installment = Amount.of(riskRequestDto
            .installment(), Currency.valueOf(riskRequestDto.currency()));

        RiskRequest riskRequest = new RiskRequest(contractNumber, customer, car, price, installment);
        return riskRequest;
    }
}
