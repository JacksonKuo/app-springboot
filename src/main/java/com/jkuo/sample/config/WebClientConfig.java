package com.jkuo.sample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

@Configuration
public class WebClientConfig {

    @Value("${smokescreen.host}")
    private String smokescreenHost;

    @Value("${smokescreen.port}")
    private int smokescreenPort;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                        .host(smokescreenHost)
                        .port(smokescreenPort));
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}