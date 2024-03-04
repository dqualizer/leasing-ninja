package io.leasingninja.sales.api;

import io.leasingninja.riskmanagement.domain.VoteResult;
import io.leasingninja.sales.domain.Amount;
import io.leasingninja.sales.domain.Car;
import io.leasingninja.sales.domain.Contract;
import io.leasingninja.sales.domain.ContractNumber;
import io.leasingninja.sales.domain.Currency;
import io.leasingninja.sales.domain.Customer;
import io.leasingninja.sales.domain.Interest;
import io.leasingninja.sales.domain.LeaseTerm;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class SalesApiController {

    public SalesApiController() {
    }

    @PostMapping(value = "/contract", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesApiResponseDto tryToCreateContractOfferAutomated(@RequestBody ContractRequestDto contractRequestDto){

        // assumes contractDto is sent to API without price, which needs to be calculated here
        // TODO define own API Dto instead

        Contract contract = contractRequestDtoToContract(contractRequestDto);
        Interest interest = Interest.of(4.5);
        contract.calculateInstallmentFor(LeaseTerm.ofMonths(contractRequestDto.leaseTearms()), interest);
        VoteResult voteResultFromApi = getVoteResultFromRiskApi(contractRequestToRiskRequestDto(contract));

        //TODO save contract to Repo
        return new SalesApiResponseDto(contract.number().number(), contract.lessee().toString(), contract.car().toString(), contract.leaseTerm().noOfMonths(), contract.installment().amount(), voteResultFromApi.name());
    }

    private VoteResult getVoteResultFromRiskApi(RiskRequestDto riskRequestDto) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Header", "header1");

        // TODO change url if running inside docker, make port configurable
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/api/vote");

        HttpEntity<RiskRequestDto> entity = new HttpEntity<>(riskRequestDto, headers);

        HttpEntity<RiskApiResponse> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.POST,
            entity,
            RiskApiResponse.class);

        return VoteResult.valueOf(response.getBody().voteResult());

    }

    private RiskRequestDto contractRequestToRiskRequestDto(Contract contract) {
        RiskRequestDto riskRequestDto = new RiskRequestDto(contract.number().toString(),
            contract.lessee().toString(),
            contract.car().toString(),
            contract.price().amount(),
            contract.installment().amount(),
            contract.getCurrency().toString());
        return riskRequestDto;
    }

    private Contract contractRequestDtoToContract(ContractRequestDto contractDto){

        ContractNumber contractNumber = ContractNumber.of(contractDto.number());
        Customer customer = Customer.of(contractDto.lessee());
        Car car = Car.of(contractDto.car());
        Amount price = Amount.of(contractDto
            .carPrice(), Currency.valueOf(contractDto.currency()));

        return new Contract(contractNumber, customer, car, price);
    }
}
