package com.sparta.basic6.infrastructure.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticConfig extends ElasticsearchConfiguration {

    private final String hostUrl;
    private final String username;
    private final String password;

    public ElasticConfig(@Value("${spring.data.elasticsearch.host}") String hostUrl,
                         @Value("${spring.data.elasticsearch.username}") String username,
                         @Value("${spring.data.elasticsearch.password}") String password) {
        this.hostUrl = hostUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    @SneakyThrows
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(hostUrl)
                .withBasicAuth(username, password)
                .build();
    }
}
