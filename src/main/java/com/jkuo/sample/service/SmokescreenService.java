package com.jkuo.sample.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import reactor.core.publisher.Mono;

@Service
public class SmokescreenService {

    private final WebClient webClient;

    public SmokescreenService() {
        HttpClient httpClient = HttpClient.create()
            .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
            .host("smokescreen")
            .port(4750)); 

        this.webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    public Mono<String> retrieveUrl(String url) {
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(error -> Mono.just("Failed to fetch data: " + error.getMessage()));
    }
}
