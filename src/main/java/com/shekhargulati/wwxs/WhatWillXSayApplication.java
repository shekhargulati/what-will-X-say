package com.shekhargulati.wwxs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class WhatWillXSayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatWillXSayApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> route(SuggestionHandler suggestionHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/v1/suggest"), suggestionHandler::suggest);
    }

    @Component
    static class CustomWebFilter implements WebFilter {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            if (exchange.getRequest().getURI().getPath().equals("/")) {
                return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().path("/index.html").build()).build());
            }
            return chain.filter(exchange);
        }
    }
}

