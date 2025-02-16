package com.jkuo.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import java.net.InetSocketAddress;

@Configuration
public class WebClientConfig {
    
    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
        .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
        .address(new InetSocketAddress("smokescreen", 4750))); 
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

}