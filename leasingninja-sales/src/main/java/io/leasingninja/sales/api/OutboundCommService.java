package io.leasingninja.sales.api;

import io.leasingninja.riskmanagement.domain.VoteResult;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.ConnectException;

@Service
public class OutboundCommService {

    public OutboundCommService() {
    }

    //@Retryable only works when method is called by different object (see https://stackoverflow.com/questions/38212471/springboot-retryable-not-retrying)
    //@Retryable(maxAttempts = 4, backoff = @Backoff(delay = 200))
    VoteResult getVoteResultFromRiskApi(RiskRequestDto riskRequestDto) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Header", "header1");

        // TODO change url if running inside docker, make port configurable
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:7081/api/vote");

        HttpEntity<RiskRequestDto> entity = new HttpEntity<>(riskRequestDto, headers);

        HttpEntity<RiskApiResponseDto> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.POST,
            entity,
            RiskApiResponseDto.class);

        return VoteResult.valueOf(response.getBody().voteResult());

    }
}
