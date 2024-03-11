package io.leasingninja.sales.api;

import io.leasingninja.riskmanagement.domain.VoteResult;
import io.leasingninja.sales.domain.Amount;
import io.leasingninja.sales.domain.Car;
import io.leasingninja.sales.domain.Contract;
import io.leasingninja.sales.domain.ContractNumber;
import io.leasingninja.sales.domain.Contracts;
import io.leasingninja.sales.domain.Currency;
import io.leasingninja.sales.domain.Customer;
import io.leasingninja.sales.domain.Interest;
import io.leasingninja.sales.domain.LeaseTerm;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.ConnectException;

@RestController
@RequestMapping("/api")
public class SalesApiController {

    private final OutboundCommService outboundCommService;
    // this probably does not adhere to intended architecture of LeasingNinja
    private final Contracts contractRepository;
    public SalesApiController(OutboundCommService outboundCommService, Contracts contractRepository) {
        this.outboundCommService = outboundCommService;
        this.contractRepository = contractRepository;
    }

    @PostMapping(value = "/contract", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesApiResponseDto tryToCreateContractOfferAutomated(@RequestBody ContractRequestDto contractRequestDto, @RequestHeader HttpHeaders headers){

        System.out.println("Received Request");


        System.out.println("Request Body: " + contractRequestDto);
        System.out.println("Request Headers:");
        headers.forEach((key, value) -> System.out.println(key + ":" + value));


        Contract contract = contractRequestDtoToContract(contractRequestDto);
        Interest interest = Interest.of(4.5);
        contract.calculateInstallmentFor(LeaseTerm.ofMonths(contractRequestDto.leaseTerms()), interest);
        VoteResult voteResultFromApi = outboundCommService.getVoteResultFromRiskApi(contractRequestToRiskRequestDto(contract));

        if (voteResultFromApi.equals(VoteResult.ACCEPTED)){
            contractRepository.save(contract);
        }

        System.out.println("Responding to Request");
        return new SalesApiResponseDto(contract.number().number(), contract.lessee().toString(), contract.car().toString(), contract.leaseTerm().noOfMonths(), contract.installment().amount(), voteResultFromApi.name());

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

    private Contract contractRequestDtoToContract(ContractRequestDto contractRequestDto){

        ContractNumber contractNumber = ContractNumber.of(contractRequestDto.number());
        Customer customer = Customer.of(contractRequestDto.lessee());
        Car car = Car.of(contractRequestDto.car());
        Amount buyingPriceCar = Amount.of(contractRequestDto
            .buyingPriceCar(), Currency.valueOf(contractRequestDto.currency()));

        return new Contract(contractNumber, customer, car, buyingPriceCar);
    }
}
