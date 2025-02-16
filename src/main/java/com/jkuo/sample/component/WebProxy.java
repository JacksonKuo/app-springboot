package com.jkuo.sample.component;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import java.net.InetSocketAddress;

import reactor.core.publisher.Mono;

@Component
public class WebProxy {

    @Autowired
    private final WebClient webClient;

    public WebProxy(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> retrieveUrl(String url) {
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(error -> Mono.just("Failed to fetch data: " + error.getMessage()));
    }

}