package com.nttdata.mpasive.utilities.config;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@RequiredArgsConstructor
@Configuration
public class ReactiveCircuitBreakerConfig {

    @Bean
    public ReactiveCircuitBreaker customersServiceReactiveCircuitBreaker(ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2))
                .build();

        reactiveCircuitBreakerFactory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig), "client-service");

        return reactiveCircuitBreakerFactory.create("client-service");
    }
}
