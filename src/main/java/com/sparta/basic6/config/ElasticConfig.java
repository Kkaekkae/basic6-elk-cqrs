package com.sparta.basic6.config;

import co.elastic.clients.elasticsearch.nodes.Client;
import lombok.SneakyThrows;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.aot.AbstractAotProcessor;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.support.HttpHeaders;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ElasticConfig extends ElasticsearchConfiguration {

    private final String hostUrl;
    private final String username;
    private final String password;
    private final String apiKey;

    public ElasticConfig(@Value("${spring.data.elasticsearch.host}") String hostUrl,
                         @Value("${spring.data.elasticsearch.username}") String username,
                         @Value("${spring.data.elasticsearch.password}") String password,
                         @Value("${spring.data.elasticsearch.api-key}") String apiKey) {
        this.hostUrl = hostUrl;
        this.username = username;
        this.password = password;
        this.apiKey = apiKey;
    }

    @Override
    @SneakyThrows
    public ClientConfiguration clientConfiguration() {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", password));

        SSLContextBuilder sslBuilder = SSLContexts.custom()
                .loadTrustMaterial(null, (x509Certificates, s) -> true);
        final SSLContext sslContext = sslBuilder.build();

        return ClientConfiguration.builder()
                .connectedTo(hostUrl)
//                .usingSsl(apiKey)
                .withBasicAuth(username, password)
                .withClientConfigurer(
                        ElasticsearchClients.ElasticsearchHttpClientConfigurationCallback.from(clientBuilder -> {
                            // ...
                            clientBuilder.setSSLContext(sslContext);
                            return clientBuilder;
                        }))
                .withHeaders(() -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("currentTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    return headers;
                })
                .build();
    }
}
