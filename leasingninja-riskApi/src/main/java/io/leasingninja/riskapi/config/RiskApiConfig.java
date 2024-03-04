package io.leasingninja.riskapi.config;

import io.leasingninja.riskapi.domain.RiskApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RiskApiConfig {

    @Bean
    public RiskApiService riskApiService(){ return new RiskApiService();}

}
