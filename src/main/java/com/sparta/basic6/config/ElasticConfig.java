package com.sparta.basic6.config;

import lombok.SneakyThrows;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", password));

        return ClientConfiguration.builder()
                .connectedTo(hostUrl)
                .withBasicAuth(username, password)
                .withHeaders(() -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("currentTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    return headers;
                })
                .build();
    }
}
